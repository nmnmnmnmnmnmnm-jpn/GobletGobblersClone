package nmGames.gobletgobblersclone;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.view.View;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

public class GLView extends GLSurfaceView {
    GLRenderer myRenderer;
    Game ga;

    private Timer timer1;
    private Handler handler1;
    int count = 0;
    int sbs = 0;

    public GLView(Context context) {
        super(context);
        ga = new Game();
        myRenderer = new GLRenderer(this, ga);
        setRenderer(myRenderer);
        setOnTouchListener(new SampleTouchListener());
        setOnLongClickListener(new SampleLongClickListener());
        /*
        timer1 = new Timer();
        handler1 = new Handler();
        timer1.schedule(new TimerTask() {
            public void run() {
                handler1.post(new Runnable() {
                    public void run() {
                        count++;
                    }
                });
            }
        }, 1000, 33);
         */
    }

    class SampleTouchListener implements OnTouchListener
    {
        public boolean onTouch(View v, MotionEvent e)
        {
            float w = v.getWidth();
            float h = v.getHeight();
            float x = e.getRawX();
            float y = e.getRawY();
            if(e.getAction() == MotionEvent.ACTION_DOWN) {
                ga.touchDown(sbs+h/2-y,x-w/2);
                return true;
            }
            else if(e.getAction() == MotionEvent.ACTION_UP){
                ga.touchUp(sbs+h/2-y,x-w/2);
                return true;
            }
            else if(e.getAction() == MotionEvent.ACTION_MOVE){
                ga.touchMove(sbs+h/2-y,x-w/2);
                return true;
            }
            else{
                return false;
            }
        }
    }

    public void setStatesBarSize(int sbs){
        this.sbs = sbs;
    }

    class SampleLongClickListener implements OnLongClickListener
    {
        public boolean onLongClick(View v)
        {
            return true;
        }
    }

    public int getCount(){
        return count;
    }
}

