package team.tts.doit;


import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import static android.support.v4.app.NotificationCompat.*;

/**
 * 这个类随app存在而存在
 * 放入全局变量或方法
 */
public class Data extends Application {
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
    public static String KEY_TEXT_REPLY="key_text_reply";

    @TargetApi(Build.VERSION_CODES.O)
    public void addNotification(String s){
        String replyLabel = "添加内容";
        String channelID="1";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //创建一个远程输入（既：通知栏的快捷回复）
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        //点击快速回复中发送按钮的时候，会发送一个广播给GetMessageReceiver
        Intent intent = new Intent(this,GetMessageReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,
                PendingIntent.FLAG_ONE_SHOT);

        //创建快速回复的动作，并添加remoteInut
        Action replyAction = new Action.Builder(
                R.drawable.ic_event_add,
                "添加", pendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        //创建一个Notification，并设置title，content，icon等内容
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String channelName = "添加事件";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"string4")
                .setChannelId(channelID)
                .setSmallIcon(R.drawable.ic_event_add)
                .setContentTitle(getString(R.string.title))
                .setContentText(s)
                .addAction(replyAction)
                .setOngoing(true);
        //发出通知
        notificationManager.notify(1,builder.build());
    }
    public void updateNotification(){

    }

}
