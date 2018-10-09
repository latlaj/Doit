package team.tts.doit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 数据库辅助类
 * 数据库操作
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static String CREATE_BOOK;

    private Context myContent;

    public MyDatabaseHelper(Context context, String name,String table, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myContent=context;
        CREATE_BOOK="CREATE TABLE "+table
                + " ( id  integer PRIMARY KEY Autoincrement ,"
                + "title text ,"
                + "time integer ,"
                + "content text )";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);

        Toast.makeText(myContent, R.string.database_finish, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 创建新的note记录，自动记录创建时间。
     * @param title
     * @param time
     * @param content
     * @return
     */
    public static Cursor insert(String title, long time, String content){
        String time0=String.valueOf(time);
        /**
         * SQL插入语句:
         * INSERT INTO Book(name,author,pages,price) VALUES
         * ("The Da Vinci Code" ,"Dan Brown",454,16.96);
         */
        Data app=(Data)Data.getAppContext();
        SQLiteDatabase db=app.getDbHelper().getWritableDatabase();
        db.execSQL("INSERT INTO notes (title,time,content) VALUES(?,?,?)",
                new String[]{title,time0,content});
        Cursor cursor=db.query(app.getTable(),new String[]{"id"},"time=?",new String[]{time0},null,null,null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    /**
     * 通过id查找note记录
     * @param ID
     * @return
     */
    public static Cursor select(String ID) {
        Data app=(Data)Data.getAppContext();
        SQLiteDatabase db=app.getDbHelper().getWritableDatabase();
        Cursor cursor=db.query(app.getTable(),new String[]{"title","time","content"},"id=?",new String[]{ID},null,null,null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    /**
     * 查找全部记录
     * @param strings
     * @param order
     * @return
     */
    public static Cursor check(String[] strings,String order){
        Data app=(Data)Data.getAppContext();
        SQLiteDatabase db=app.getDbHelper().getReadableDatabase();
        Cursor cursor = db.query(app.getTable(), strings, null, null, null, null, order);
        return cursor;
    }

    public static Cursor check(){
        Data app=(Data)Data.getAppContext();
        SQLiteDatabase db=app.getDbHelper().getReadableDatabase();
        Cursor c = db.rawQuery("select * from notes", null);
        return c;
    }

    /**
     * 通过ID删除
     * @param ID
     */
    public static void delete_id(String ID){
        Data app=(Data)Data.getAppContext();
        SQLiteDatabase db=app.getDbHelper().getWritableDatabase();
        db.delete(app.getTable(), "id = ?", new String[]{ID});
    }

    /**
     * 更新note的内容
     * @param id
     * @param title
     * @param content
     */
    public static void update(int id,String title,String content){
        Data app=(Data)Data.getAppContext();
        SQLiteDatabase db=app.getDbHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("content",content);
        //?是一个占位符，通过字符串数组为每个占位符指定相应的内容
        db.update(app.getTable(), values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
