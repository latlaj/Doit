package team.tts.doit;

public class Timer {
    public static int TYPE_TIMER=0;
    String ID;
    private int type;
    private String title;
    private String content;
    public boolean isEnable;
    Timer(String ID,String title,String introduction,int type){
        this.ID=ID;
        this.title=title;
        this.content=introduction;
        this.type=type;
    }
}
