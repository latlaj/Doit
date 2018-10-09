package team.tts.doit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 新建与编辑界面
 */
public class NewNoteActivity extends AppCompatActivity {
    //SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase("date/data/com.example.a64929.mynote/databases/Notes.db",null);
    private boolean have_dialog=true;
    private boolean hc=false;
    private int note_id=0;
    Data app=null;
    long noteTime=0;
    int view_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        app=(Data)getApplication();
        Intent intent=getIntent();
        hc=intent.getBooleanExtra("have_content",false);
        if(hc){
            int id=intent.getIntExtra("note_id",1);
            view_id=intent.getIntExtra("view_id",0);
            note_id=id;
            display(id);
            EditText et = (EditText)findViewById(R.id.editText2);
            et.requestFocus();
            et.setSelection(et.getText().length());
        }else{
            noteTime=System.currentTimeMillis();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        have_dialog = sharedPref.getBoolean(getString(R.string.pref_new_dialog_display_key),true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if(have_dialog){
                    showExitDialog();
                }else {
                    save();
                    finish();
                }
                break;
            case R.id.action_delete:
                Intent intent=new Intent();
                intent.putExtra("view_id",view_id);
                intent.putExtra("delete",true);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(have_dialog){
            showExitDialog();
        }else{
            save();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu,menu);
        return true;
    }



    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_save)
                .setMessage(R.string.save_or_not)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        save();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },100);

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },100);
                    }
                })
                .show();
    }

    private void save(){
        EditText title0 = (EditText) findViewById(R.id.editText);
        EditText content0 = (EditText) findViewById(R.id.editText2);
        String title = title0.getText().toString();
        String content = content0.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        if (hc) {
            MyDatabaseHelper.update(note_id,title,content);
            intent.putExtra("view_id",view_id);
            intent.putExtra("id",note_id);
        }else {

            Cursor cursor=MyDatabaseHelper.insert(title,noteTime,content);
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            intent.putExtra("time",noteTime);
            intent.putExtra("id", id);
        }
        setResult(RESULT_OK, intent);
    }

    private void display(int id){
        String ID=String.valueOf(id);
        Cursor cursor=MyDatabaseHelper.select(ID);
        String title=cursor.getString(cursor.getColumnIndex("title"));
        noteTime=cursor.getLong(cursor.getColumnIndex("time"));
        String content=cursor.getString(cursor.getColumnIndex("content"));
        EditText title0=(EditText)findViewById(R.id.editText);
        title0.setText(title);
        EditText content0=(EditText)findViewById(R.id.editText2);
        content0.setText(content);
    }
}
