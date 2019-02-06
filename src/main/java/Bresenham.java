import com.jogamp.opengl.GL2;

import static com.jogamp.opengl.GL.GL_POINTS;

class Bresenham extends Drawer {

    @Override
    void drawLine(int x1, int y1, int x2, int y2, GL2 gl, boolean field) {
        if (field) {
            x1 = x1 + 780 - 370;
            x2 = x2 + 780 - 370;
        }
        int d = 0;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int dx2 = 2 * dx;
        int dy2 = 2 * dy;

        int ix = x1 < x2 ? 1 : -1;
        int iy = y1 < y2 ? 1 : -1;

        int x = x1;
        int y = y1;
        gl.glBegin(GL_POINTS);
        if (dx >= dy) {
            while (true) {
                gl.glVertex2d(x, y);
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                gl.glVertex2d(x, y);
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
        gl.glEnd();
    }

    @Override
    void drawCircle(int xc, int yc, int r, GL2 gl, int quadrant, boolean part, boolean field) {
        if (field) {
            xc = xc + 780 - 370;
        }
        int x = 0, y = r;
        int d = 3 - 2 * r;
        circle(xc, yc, x, y, gl, quadrant, part);

        while (y >= x) {
            x++;

            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else
                d = d + 4 * x + 6;

            circle(xc, yc, x, y, gl, quadrant, part);
        }
    }

    private void circle(int xc, int yc, int x, int y, GL2 gl, int quadrant, boolean part) {
        gl.glBegin(GL_POINTS);
        if (part) {
            if (quadrant == 1) {
                gl.glVertex2d(xc + x, yc + y);
                gl.glVertex2d(xc + y, yc + x);

            } else if (quadrant == 2) {
                gl.glVertex2d(xc - x, yc + y);
                gl.glVertex2d(xc - y, yc + x);

            } else if (quadrant == 3) {
                gl.glVertex2d(xc - x, yc - y);
                gl.glVertex2d(xc - y, yc - x);

            } else if (quadrant == 4) {
                gl.glVertex2d(xc + y, yc - x);
                gl.glVertex2d(xc + x, yc - y);
            }
        } else {

            gl.glVertex2d(xc + x, yc + y);
            gl.glVertex2d(xc + y, yc + x);
            gl.glVertex2d(xc - x, yc + y);
            gl.glVertex2d(xc - y, yc + x);
            gl.glVertex2d(xc - x, yc - y);
            gl.glVertex2d(xc - y, yc - x);
            gl.glVertex2d(xc + y, yc - x);
            gl.glVertex2d(xc + x, yc - y);

        }
        gl.glEnd();
    }
}