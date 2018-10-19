package team.tts.doit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    List<Note> list;
    LayoutInflater inflater=null;
    ListView listView;
    Context context;
    public ListViewAdapter(List<Note> list,Context context){
        this.list=list;
        this.inflater= LayoutInflater.from(context);
        this.context=context;
        this.listView=((MainActivity)context).listView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View visible=null;
        if(view==null){
            visible=inflater.inflate(R.layout.list_item,null);
        }else{
            visible=view;
        }
        TextView title=(TextView)visible.findViewById(R.id.ItemTitle);
        TextView content=(TextView)visible.findViewById(R.id.ItemText);
        TextView time=(TextView)visible.findViewById(R.id.ItemTime);
        title.setText(list.get(i).getTitle());
        time.setText(list.get(i).getTime());
        content.setText(list.get(i).getContent());
        updateBackground(i , visible);
        return visible;
    }
    public void updateBackground(int i, View view) {
        if (listView.isItemChecked(i)) {
            view.setBackground(ContextCompat.getDrawable(context,R.drawable.list_selected_holo));
        } else {
            view.setBackground(ContextCompat.getDrawable(context,R.drawable.list_routine_holo));
        }
    }
}
