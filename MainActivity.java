package nmGames.gobletgobblersclone;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;

public class MainActivity extends Activity {
    private GLView myGLView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGLView = new GLView(this);
        setContentView(myGLView);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final Rect rect = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        myGLView.setStatesBarSize(rect.top);
    }

    protected void onResume(){
        super.onResume();
        myGLView.onResume();
    }

    protected void onPause(){
        super.onPause();
        myGLView.onPause();
    }
}
