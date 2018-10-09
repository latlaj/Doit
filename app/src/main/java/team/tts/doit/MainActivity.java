package team.tts.doit;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Note> list = new ArrayList<Note>();
    ListView listView = null;
    Data app;
    ListViewAdapter lva;
    TextView no_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        no_list = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                //showDeleteDialog(i);
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivityForResult(intent,3);
                return true;
            }
        });
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
        lva = new ListViewAdapter(list, this);
        listView.setAdapter(lva);
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

    private void showDeleteDialog(final int view_id) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete)
                .setMessage(R.string.delete_or_not)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNote(view_id);
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
}
