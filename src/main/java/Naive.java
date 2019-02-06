import com.jogamp.opengl.GL2;

import static com.jogamp.opengl.GL.GL_POINTS;

class Naive extends Drawer {
    @Override
    void drawLine(int x1, int y1, int x2, int y2, GL2 gl, boolean field) {
        if (field) {
            x1 = x1 + 780 - 370;
            x2 = x2 + 780 - 370;
        }
        boolean inverted = Math.abs(y2 - y1) > Math.abs(x2 - x1);

        if (inverted) {
            int aux = x1;
            x1 = y1;
            y1 = aux;
            aux = x2;
            x2 = y2;
            y2 = aux;
        }

        double m = (y2 - y1) / ((x2 - x1) * 1.0);
        int x, y;

        gl.glBegin(GL_POINTS);
        for (x = x1; x < x2; x++) {
            y = (int) (y1 + m * (x - x1));

            if (inverted)
                gl.glVertex2i(y, x);
            else
                gl.glVertex2i(x, y);
        }
        gl.glEnd();
    }

    @Override
    void drawCircle(int x, int y, int r, GL2 gl, int quadrant, boolean part, boolean field) {
        if (field) {
            x = x + 780 - 370;
        }
        int Frac_Circ = 400;
        double xx, yy, xxx, yyy;
        gl.glBegin(GL_POINTS);
        for (int i = 0; i < 100 + 1; i++) {
            xx = x + r * Math.cos(2.0 * Math.PI * i / Frac_Circ);
            yy = y + r * Math.sin(2.0 * Math.PI * i / Frac_Circ);
            xxx = x - r * Math.cos(2.0 * Math.PI * i / Frac_Circ);
            yyy = y - r * Math.sin(2.0 * Math.PI * i / Frac_Circ);
            if (part) {
                if (quadrant == 1) {
                    gl.glVertex2d(xx, yy);
                } else if (quadrant == 2) {
                    gl.glVertex2d(xxx, yy);
                } else if (quadrant == 3) {
                    gl.glVertex2d(xxx, yyy);
                } else if (quadrant == 4) {
                    gl.glVertex2d(xx, yyy);
                }
            } else {
                gl.glVertex2d(xx, yy);
                gl.glVertex2d(xxx, yy);
                gl.glVertex2d(xxx, yyy);
                gl.glVertex2d(xx, yyy);
            }
        }
        gl.glEnd();
    }
}