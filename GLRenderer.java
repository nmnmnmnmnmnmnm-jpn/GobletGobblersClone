package nmGames.gobletgobblersclone;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import java.util.Random;

public class GLRenderer implements Renderer {
    private static final int SIZEOF_FLOAT = Float.SIZE / 8;
    GLView glv;
    Game ga;

    float vertices[] = {
            -540.0f, -865.0f, 0.0f,
            100.0f, -100.0f, 0.0f,
            -100.0f, 100.0f, 0.0f,
            100.0f, 100.0f, 0.0f
    };

    public GLRenderer(GLView glv, Game ga) {
        this.glv = glv;
        this.ga = ga;
    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -1.0f);
        ga.draw(gl);
        gl.glFinish();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(width*600>500*height) {
            // 横長
            float w = 1.0f*height/6*5;
            ga.setPosition(-height/2,(width-w)/2-width/2, height-height/2, (width+w)/2-width/2);
        }
        else {
            // 縦長
            float h = 1.0f*width/5*6;
            ga.setPosition((height-h)/2-height/2, -width/2, (height+h)/2-height/2, width/2);
        }
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-width/2,width/2,-height/2,height/2,1.0f,2.0f);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL10.GL_LIGHTING);
    }

    public static final FloatBuffer makeFloatBuffer( float[] arr )
    {
        ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * SIZEOF_FLOAT );
        bb.order( ByteOrder.nativeOrder() );
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put( arr );
        fb.position( 0 );
        return fb;
    }
}

