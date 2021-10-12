package nmGames.gobletgobblersclone;

import javax.microedition.khronos.opengles.GL10;

public class Game {
    static final int WIDTH = 500;
    static final int WAKU_LENGTH = 80;
    static final int WAKU_AREA = 100;
    static final int BANMEN_AREA = 400;
    static final int HEIGHT = WAKU_AREA*2+BANMEN_AREA;
    float radius;
    int flag; // 物を掴んでない 0 いる 1
    int e; // 掴んでいる駒
    int old; // 掴んだ駒の元の位置
    float xx,yy; // 掴んでいるものの位置
    int d[];
    P pd[];
    Game(){
        radius = WAKU_LENGTH/2;
        flag = 0;
        e = 0;
        old = 0;
        yy = -10000;
        xx = -10000;
        d = new int[21];
        pd = new P[21];
        for(int i = 0; i < 21; i++){
            pd[i] = new P();
        }
        init();
    }
    void init(){
        // 駒を初期位置に配置
        for(int i = 0; i < 6; i++){
            d[i] = (1<<(i/2)*2);
        }
        for(int i = 0; i < 6; i++){
            d[6+i] = (1<<((5-i)/2)*2+1);
        }
        for(int i = 0; i < 9; i++){
            d[12+i] = 0;
        }
    }
    void setPosition(float y1, float x1, float y2, float x2){
        float w = x2-x1;
        float h = y2-y1;
        // red枠の作成
        float ay;
        float ax;
        ay = (WAKU_AREA-WAKU_LENGTH)/2;
        ax = (WIDTH-WAKU_LENGTH*6)/2;
        for(int i = 0; i < 6; i++){
            pd[i].set(0, ay, ax + i * WAKU_LENGTH);
            pd[i].set(1, ay, ax + (i+1) * WAKU_LENGTH);
            pd[i].set(2, ay+WAKU_LENGTH, ax + (i+1) * WAKU_LENGTH);
            pd[i].set(3, ay+WAKU_LENGTH, ax + i * WAKU_LENGTH);
        }
        // blue枠の作成
        ay = WAKU_AREA+BANMEN_AREA+(WAKU_AREA-WAKU_LENGTH)/2;
        ax = (WIDTH-WAKU_LENGTH*6)/2;
        for(int i = 0; i < 6; i++){
            pd[6+i].set(0, ay, ax + i * WAKU_LENGTH);
            pd[6+i].set(1, ay, ax + (i+1) * WAKU_LENGTH);
            pd[6+i].set(2, ay+WAKU_LENGTH, ax + (i+1) * WAKU_LENGTH);
            pd[6+i].set(3, ay+WAKU_LENGTH, ax + i * WAKU_LENGTH);
        }
        // 盤面の作成
        ay = WAKU_AREA+(BANMEN_AREA-WAKU_LENGTH*3)/2;
        ax = (WIDTH-WAKU_LENGTH*3)/2;
        for(int i = 0; i < 9; i++){
            int r = i/3;
            int c = i%3;
            pd[12+i].set(0, ay + c*WAKU_LENGTH, ax + r * WAKU_LENGTH);
            pd[12+i].set(1, ay + c*WAKU_LENGTH, ax + (r + 1) * WAKU_LENGTH);
            pd[12+i].set(2, ay + (c+1)*WAKU_LENGTH, ax + (r + 1) * WAKU_LENGTH);
            pd[12+i].set(3, ay + (c+1)*WAKU_LENGTH, ax + r * WAKU_LENGTH);
        }
        // 変換
        for(int i = 0; i < 21; i++){
            for(int j = 0; j < 4; j++){
                pd[i].y[j] *= h/HEIGHT;
                pd[i].y[j] += y1;
                pd[i].y[j] *= -1;
                pd[i].x[j] *= w/WIDTH;
                pd[i].x[j] += x1;
            }
        }
        radius *= h/HEIGHT;
    }
    void drawCircle(GL10 gl, float y, float x, float radius ,int c){
        gl.glPushMatrix();
        float vertices[] = new float[182*3];
        vertices[0] = x;
        vertices[1] = y;
        vertices[2] = 0.0f;
        for(int i = 0; i < 181; i++){
            vertices[(i+1)*3+0] = x+(float)Math.cos(2*Math.PI/180*i)*radius;
            vertices[(i+1)*3+1] = y+(float)Math.sin(2*Math.PI/180*i)*radius;
            vertices[(i+1)*3+2] = 0.0f;
        }
        if(c==0) {
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        }
        else{
            gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
        }
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glLineWidth(10.0f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, GLRenderer.makeFloatBuffer(vertices));
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 182);
        gl.glPopMatrix();
    }
    void draw(GL10 gl){
        gl.glPushMatrix();
        float vertices[] = new float[15];
        gl.glColor4f(0.0f,0.0f,0.0f,1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glLineWidth(10.0f);
        // 枠の描画
        for(int i = 0; i < 21; i++){
            for(int j = 0; j < 5; j++){
                vertices[j*3+0] = pd[i].x[j%4];
                vertices[j*3+1] = pd[i].y[j%4];
                vertices[j*3+2] = 0.0f;
            }
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, GLRenderer.makeFloatBuffer(vertices));
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 5);
        }
        // 駒の描画
        for(int i = 0; i < 21; i++) {
            float py = (pd[i].y[1]+pd[i].y[2])/2;
            float px = (pd[i].x[0]+pd[i].x[1])/2;
            drawCircle(gl, py, px, getKomaSize(d[i]), getKomaColor(d[i]));
        }
        if(flag==1){
            drawCircle(gl, yy, xx, getKomaSize(e), getKomaColor(e));
        }
        gl.glPopMatrix();
    }
    float getKomaSize(int x){
        if((x&(1<<5))!=0||(x&(1<<4))!=0)return radius * 0.8f;
        if((x&(1<<3))!=0||(x&(1<<2))!=0)return radius * 0.6f;
        if((x&(1<<1))!=0||(x&(1<<0))!=0)return radius * 0.4f;
        return 0;
    }
    int getKomaColor(int x){
        if((x&(1<<5))!=0)return 1;
        if((x&(1<<4))!=0)return 0;
        if((x&(1<<3))!=0)return 1;
        if((x&(1<<2))!=0)return 0;
        if((x&(1<<1))!=0)return 1;
        if((x&(1<<0))!=0)return 0;
        return 0;
    }

    int checkTopKoma(int x){
        if((x&(1<<5))!=0)return 1<<5;
        if((x&(1<<4))!=0)return 1<<4;
        if((x&(1<<3))!=0)return 1<<3;
        if((x&(1<<2))!=0)return 1<<2;
        if((x&(1<<1))!=0)return 1<<1;
        if((x&(1<<0))!=0)return 1<<0;
        return 0;
    }

    void touchDown(float y, float x){
        for(int i = 0; i < 21; i++){
            if(pd[i].checkIn(y,x)==1){
                int k = checkTopKoma(d[i]);
                if(k!=0){
                    flag = 1;
                    yy = y;
                    xx = x;
                    d[i] ^= k;
                    e = k;
                    old = i;
                    return;
                }
            }
        }
        e = 0;
    }

    void touchMove(float y, float x){
        this.yy = y;
        this.xx = x;
    }

    void touchUp(float y, float x){
        flag = 0;
        yy = -10000;
        xx = -10000;
        for(int i = 0; i < 21; i++){
            if(pd[i].checkIn(y,x)==1){
                int k = checkTopKoma(d[i]);
                int c = getKomaColor(k);
                if(c==0)k = k<<1;
                if(e>k){
                    d[i] |= e;
                    return;
                }
            }
        }
        d[old] |= e;
    }

    // 左上 0、右上 1、右下 2、左下 3
    class P{
        public float y[];
        public float x[];
        P(){
            y = new float[4];
            x = new float[4];
        }
        void set(int p, float y, float x){
            if(p<4)this.y[p] = y;
            if(p<4)this.x[p] = x;
        }
        // y,xが枠の中かどうかをチェック
        int checkIn(float y, float x){
            if(this.x[0]<x&&x<this.x[1]&&this.y[2]<y&&y<this.y[1]){
                return 1;
            }
            return 0;
        }
    }
}
