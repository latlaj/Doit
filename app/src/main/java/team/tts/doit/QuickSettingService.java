package team.tts.doit;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class QuickSettingService extends TileService {
    String LOG_TAG="my";
    //Icon icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_event_add);
    boolean notification_switch=false;
    //当用户从Edit栏添加到快速设定中调用
    @Override
    public void onTileAdded() {
        Log.d(LOG_TAG, "onTileAdded");

    }
    //当用户从快速设定栏中移除的时候调用
    @Override
    public void onTileRemoved() {
        Log.d(LOG_TAG, "onTileRemoved");
    }
    // 点击的时候
    @Override
    public void onClick() {
        Log.d(LOG_TAG, "onClick");
    }
    // 打开下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    //在TleAdded之后会调用一次
    @Override
    public void onStartListening () {
        Log.d(LOG_TAG, "onStartListening");
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        notification_switch=sharedPref.getBoolean(getString(R.string.pref_title_new_message_notifications),false);
        if(notification_switch){

        }
        getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态

        getQsTile().setState(Tile.STATE_ACTIVE);//更改成活跃状态
        //getQsTile().setIcon(icon);//设置图标
        getQsTile().updateTile();//更新Tile
    }
    // 关闭下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    // 在onTileRemoved移除之前也会调用移除
    @Override
    public void onStopListening () {
        Log.d(LOG_TAG, "onStopListening");
    }

}