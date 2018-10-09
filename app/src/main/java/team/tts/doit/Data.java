package team.tts.doit;

import android.app.Application;
import android.content.Context;

/**
 * 这个类随app存在而存在
 * 放入全局变量或方法
 */
public class Data extends Application{
    @Override
    public void onCreate() {
        context=this;
        dbHelper=new MyDatabaseHelper(this,database_name,table,null,1);
        super.onCreate();
    }

    /**
     * 数据库名
     */
    private  String database_name="Notes.db";
    /**
     * 表名
     */
    private String table="notes";

    private static Context context;

    public String getTable() {
        return table;
    }

    private MyDatabaseHelper dbHelper;

    public MyDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public static Context getAppContext() {
        return Data.context;
    }
}
