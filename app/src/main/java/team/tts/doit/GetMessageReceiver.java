package team.tts.doit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GetMessageReceiver extends BroadcastReceiver {

    private Context context;
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("my","my");
        String replyContent = getMessageText(intent).toString();
        String replyLabel = "添加内容";
        String channelID="1";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //创建一个远程输入（既：通知栏的快捷回复）
        android.support.v4.app.RemoteInput remoteInput = new android.support.v4.app.RemoteInput.Builder(Data.KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        //点击快速回复中发送按钮的时候，会发送一个广播给GetMessageReceiver
        Intent intentTo = new Intent(context,GetMessageReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intentTo,
                PendingIntent.FLAG_ONE_SHOT);

        //创建快速回复的动作，并添加remoteInut
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"string4")
                .setChannelId(channelID)
                .setSmallIcon(R.drawable.ic_event_add)
                .setContentTitle("已添加")
                .setContentText(replyContent)
                .addAction(replyAction)
                .setOngoing(true);
        //发出通知
        notificationManager.notify(1,builder.build());
    }

    //获取快捷回复中用户输入的字符串
    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(Data.KEY_TEXT_REPLY);//通过KEY_TEXT_REPLY来获取输入的内容
        }
        return null;
    }
}