package team.tts.doit;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    List<Note> list = new ArrayList<Note>();
    ListView listView = null;
    Data app;
    ListViewAdapter lva;
    TextView no_list;
    MainListViewModeListener mainListViewModeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        no_list = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        lva = new ListViewAdapter(list, this);
        listView.setAdapter(lva);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
                createNewNote(view);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        app = (Data) getApplication();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                intent.putExtra("have_content", true);
                intent.putExtra("note_id", list.get(i).getId());
                intent.putExtra("view_id", i);
                startActivityForResult(intent, 2);
            }
        });
        /*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivityForResult(intent,3);
                return true;
            }
        });
        */
        mainListViewModeListener=new MainListViewModeListener();
        listView.setMultiChoiceModeListener(mainListViewModeListener);
        Cursor c = MyDatabaseHelper.check();
        if (c.getCount() != 0) {
            String[] strings={"id", "title", "time", "content"};
            String order="id";
            Cursor cursor = MyDatabaseHelper.check(strings,order);
            SimpleDateFormat sdFormatter = new SimpleDateFormat(getString(R.string.date_format));
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = sdFormatter.format(cursor.getLong(cursor.getColumnIndex("time")));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                list.add(new Note(id, title, time, content));
            }
            cursor.close();
        }
        c.close();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (list.size() == 0) {
            listView.setVisibility(View.GONE);
            no_list.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            no_list.setVisibility(View.GONE);
        }
        lva.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.action_edit){
            if(list.size()>0){
                listView.setItemChecked(0,true);
                listView.clearChoices();
            }
            app.addNotification("快速添加事件");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: //请求码
                if (resultCode == RESULT_OK) {
                    if (!data.getBooleanExtra("delete", false)) {
                        String title = data.getStringExtra("title");
                        SimpleDateFormat sdFormatter = new SimpleDateFormat(getString(R.string.date_format));
                        String time = sdFormatter.format(data.getLongExtra("time", 1998));
                        String content = data.getStringExtra("content");
                        int id = data.getIntExtra("id", 1);
                        list.add(new Note(id, title, time, content));
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    if (data.getBooleanExtra("delete", false)) {
                        deleteNote(data.getIntExtra("view_id", 0));
                    } else {
                        String title = data.getStringExtra("title");
                        String content = data.getStringExtra("content");
                        Note note = list.get(data.getIntExtra("view_id", 0));
                        int id = data.getIntExtra("id", 1);
                        note.setId(id);
                        note.setTitle(title);
                        note.setContent(content);
                    }
                }
        }
    }

    public void createNewNote(View view) {
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra("have_content", false);
        startActivityForResult(intent, 1);
    }

    private void showDeleteDialog(final ArrayList<Integer> view_ids) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete)
                .setMessage(R.string.delete_or_not)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNote(view_ids);
                        listView.clearChoices();
                        mainListViewModeListener.finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void deleteNote(int view_id) {
        String ID=String.valueOf(list.get(view_id).getId());
        MyDatabaseHelper.delete_id(ID);
        list.remove(view_id);
        lva.notifyDataSetChanged();
        if (list.size() == 0) {
            no_list.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }
    private void deleteNote(ArrayList<Integer> view_ids){
        Collections.sort(view_ids);
        int l=view_ids.size();
        for(int i=l-1;i>-1;--i){
            String ID=String.valueOf(list.get(view_ids.get(i)).getId());
            MyDatabaseHelper.delete_id(ID);
            list.remove((int)view_ids.get(i));
        }
        lva.notifyDataSetChanged();
        if (list.size() == 0) {
            no_list.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    /**
     * ActionMode的监听内部类
     */
    private class MainListViewModeListener implements ListView.MultiChoiceModeListener {
        private ActionMode actionMode;
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.actionMode=actionMode;
            actionMode.setTitle(R.string.action_edit);
            actionMode.setSubtitle(getSubtitle());
            getMenuInflater().inflate(R.menu.menu_multi_choice, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.action_delete:
                    SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                    ArrayList<Integer> integerArrayList=new ArrayList<>();
                    for (int x = 0; x < checkedItemPositions.size(); x++) {
                        int key = checkedItemPositions.keyAt(x);
                        boolean b = checkedItemPositions.get(key);
                        if (b) {
                            integerArrayList.add(key);
                        }
                    }
                    showDeleteDialog(integerArrayList);
                    break;
                case R.id.action_choose_all:
                    if(listView.getCheckedItemCount()==lva.getCount()){
                        unSelectedAll();
                        actionMode.setSubtitle(getSubtitle());
                        lva.notifyDataSetChanged();
                    }else{
                        selectedAll();
                    }
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
        }
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
            lva.notifyDataSetChanged();
            actionMode.setSubtitle(getSubtitle());
        }
        public void selectedAll(){
            for(int i= 0; i< lva.getCount(); i++){
                listView.setItemChecked(i, true);
            }
        }
        public void unSelectedAll(){
            listView.clearChoices();
        }
        public String getSubtitle(){
            String s0=MainActivity.this.getString(R.string.action_title_sub1);
            String s1=String.valueOf(listView.getCheckedItemCount());
            String s2;
            if(listView.getCheckedItemCount()>1){
                s2=MainActivity.this.getString(R.string.action_title_sub3);
            }else{
                s2=MainActivity.this.getString(R.string.action_title_sub2);
            }
            return s0+" "+s1+" "+s2;
        }
        public void finish(){
            actionMode.setSubtitle(getSubtitle());
            if(list.size()==0)actionMode.finish();
        }
    }
}
