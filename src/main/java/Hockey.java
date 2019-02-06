import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

public class Hockey extends GLCanvas implements GLEventListener, MouseListener, MouseMotionListener, ActionListener {

    private static String TITLE = "JOGL 2.0 Setup (GLCanvas)";
    private static final int CANVAS_WIDTH = 1300;
    private static final int CANVAS_HEIGHT = 780;
    private static final int FPS = 60;
    private static int firstClickX = -1;
    private static int firstClickY = -1;
    private static final int TICK_MIN = 0;
    private static final int TICK_INIT = 2;
    private static final int TICK_MAX = 10;
    private ArrayList<Line> lines = new ArrayList<>();
    private Bresenham bresenham = new Bresenham();
    private Naive naive = new Naive();

    private static float thickness = 1;
    private static int algorithimSelected = 0;
    private static float red = 1, green = 1, blue = 1;


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            GLCanvas canvas = new Hockey();
            canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

            final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    new Thread(() -> {
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                    }).start();
                }
            });
            JPanel p = new JPanel();
            p.setLayout(new FlowLayout());
            frame.setTitle(TITLE);
            JPanel botoes = new JPanel();

            JRadioButton retaButton = new JRadioButton("Naive");
            JRadioButton bressButton = new JRadioButton("Bresenham");

            retaButton.addActionListener(e -> {
                if (retaButton.isSelected()) {
                    algorithimSelected = 0;
                }
            });

            bressButton.addActionListener(e -> {
                if (bressButton.isSelected()) {
                    algorithimSelected = 1;
                }
            });

            retaButton.setSelected(true);

            JSlider espessura = new JSlider(JSlider.HORIZONTAL, TICK_MIN, TICK_MAX, TICK_INIT);

            espessura.addChangeListener(e -> thickness = espessura.getValue());

            JLabel lOpcao = new JLabel("Algoritmo");
            JLabel lEspessura = new JLabel("Espessura");

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(retaButton);
            buttonGroup.add(bressButton);

            JButton botaoCor = new JButton("Cor");
            botaoCor.setOpaque(true);
            botaoCor.addActionListener(e -> {
                Color color = JColorChooser.showDialog(null, "Escolha a cor para as retas", Color.BLUE);
                System.out.println(color);

                if (color != null) {
                    botaoCor.setBackground(color);
                    red = color.getRed() / 255;
                    green = color.getGreen() / 255;
                    blue = color.getBlue() / 255;
                }
            });


            botoes.setLayout(new BoxLayout(botoes, BoxLayout.Y_AXIS));
            botoes.add(botaoCor);
            botoes.add(lOpcao);
            botoes.add(retaButton);
            botoes.add(bressButton);
            botoes.add(lEspessura);
            botoes.add(espessura);

            Container pane = frame.getContentPane();
            pane.add(botoes, BorderLayout.LINE_START);
            pane.add(canvas);
            frame.pack();
            frame.setVisible(true);
            animator.start();
        });
    }

    public Hockey() {
        this.addGLEventListener(this);
        addMouseListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();
        glu.gluOrtho2D(0, CANVAS_WIDTH, 0, CANVAS_HEIGHT);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glColor3f(red, green, blue);
        gl.glPointSize(thickness);

        Drawer dw;
        if (algorithimSelected == 1)
            dw = this.bresenham;
        else
            dw = this.naive;

        Line debug = new Line(0, 0, 0, 0);
        for (Line line : lines) {
            dw.drawLine(line.x1, line.y1, line.x2, line.y2, gl, false);
            debug = line;
        }
        if (firstClickX != -1)
            System.out.println(" x1: " + debug.x1 + " y1: " + debug.y1 + " x2: " + debug.x2 + " y2: " + debug.y2);

        // Edges
        dw.drawLine(100, 100, 100, 700, gl, true);
        dw.drawLine(450, 100, 450, 700, gl, true);
        dw.drawLine(150, 750, 400, 750, gl, true);
        dw.drawLine(150, 50, 400, 50, gl, true);

        // Center
        dw.drawLine(100, 400, 450, 400, gl, true);
        dw.drawCircle(275, 400, 50, gl, 0, false, true);

        dw.drawLine(100, 475, 450, 475, gl, true);
        dw.drawCircle(200, 460, 1, gl, 0, false, true);
        dw.drawCircle(350, 460, 1, gl, 0, false, true);

        dw.drawLine(100, 325, 450, 325, gl, true);
        dw.drawCircle(200, 340, 1, gl, 0, false, true);
        dw.drawCircle(350, 340, 1, gl, 0, false, true);

        // Top
        dw.drawLine(100, 675, 450, 675, gl, true);
        dw.drawCircle(275, 675, 15, gl, 3, true, true);
        dw.drawCircle(275, 675, 15, gl, 4, true, true);

        dw.drawCircle(200, 600, 45, gl, 0, false, true);
        dw.drawCircle(200, 600, 1, gl, 0, false, true);
        dw.drawLine(145, 607, 155, 607, gl, true);
        dw.drawLine(145, 593, 155, 593, gl, true);
        dw.drawLine(245, 607, 255, 607, gl, true);
        dw.drawLine(245, 593, 255, 593, gl, true);

        dw.drawCircle(350, 600, 45, gl, 0, false, true);
        dw.drawCircle(350, 600, 1, gl, 0, false, true);
        dw.drawLine(295, 607, 305, 607, gl, true);
        dw.drawLine(295, 593, 305, 593, gl, true);
        dw.drawLine(395, 607, 405, 607, gl, true);
        dw.drawLine(395, 593, 405, 593, gl, true);

        // Down
        dw.drawLine(100, 125, 450, 125, gl, true);
        dw.drawCircle(275, 125, 15, gl, 1, true, true);
        dw.drawCircle(275, 125, 15, gl, 2, true, true);

        dw.drawCircle(200, 215, 45, gl, 0, false, true);
        dw.drawCircle(200, 215, 1, gl, 0, false, true);
        dw.drawLine(145, 222, 155, 222, gl, true);
        dw.drawLine(145, 208, 155, 208, gl, true);
        dw.drawLine(245, 222, 255, 222, gl, true);
        dw.drawLine(245, 208, 255, 208, gl, true);

        dw.drawCircle(350, 215, 45, gl, 0, false, true);
        dw.drawCircle(350, 215, 1, gl, 0, false, true);
        dw.drawLine(295, 222, 305, 222, gl, true);
        dw.drawLine(295, 208, 305, 208, gl, true);
        dw.drawLine(395, 222, 405, 222, gl, true);
        dw.drawLine(395, 208, 405, 208, gl, true);

        // Rounded edges
        dw.drawCircle(400, 700, 50, gl, 1, true, true);
        dw.drawCircle(150, 700, 50, gl, 2, true, true);
        dw.drawCircle(150, 100, 50, gl, 3, true, true);
        dw.drawCircle(400, 100, 50, gl, 4, true, true);

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) { }

    @Override
    public void dispose(GLAutoDrawable drawable) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        y = Math.abs(y - CANVAS_HEIGHT);
        System.out.println("x: " + x + " y: " + y);
        if (firstClickX == -1 && firstClickY == -1) {
            firstClickX = x;
            firstClickY = y;
        } else {
            lines.add(new Line(firstClickX, firstClickY, x, y));
            firstClickX = -1;
            firstClickY = -1;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    public void mouseDragged(MouseEvent e) { }

    public void mouseMoved(MouseEvent e) { }

}