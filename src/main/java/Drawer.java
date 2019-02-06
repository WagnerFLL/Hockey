import com.jogamp.opengl.GL2;

abstract class Drawer {
    abstract void drawLine(int a, int b, int c, int d, GL2 gl, boolean field);
    abstract void drawCircle(int a, int b, int c, GL2 gl, int d, boolean e, boolean field);
}