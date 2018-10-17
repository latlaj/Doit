package team.tts.doit;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.view.ActionMode;

public class MainListViewModeListener implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.setTitle("This is Title");
        actionMode.setSubtitle("This is SubTitle");
        //getMenuInflater().inflate(R.menu.menu_item_callback, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
}
