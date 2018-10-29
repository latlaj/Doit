package team.tts.doit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimerAddActivity extends AppCompatActivity {
    List<Timer> timerList=new ArrayList<>();
    TextView textView;
    ListView listView;
    ListViewAdapterForTimer lvaft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_add);
        textView=findViewById(R.id.textViewTimer);
        listView=findViewById(R.id.listViewTimer);
        lvaft=new ListViewAdapterForTimer();
    }
}
