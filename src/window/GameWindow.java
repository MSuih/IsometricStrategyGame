package window;

import game.Game;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameWindow extends JFrame {
    private Game game = new Game();
    private int cameraX = 5, cameraY = 5;

    private GameWindow() {
        this.addKeyListener(new CustomKeyListener(KeyBindings.getDefaultBinds()));
        this.addMouseListener(new CustomMouseListener());
    }

    @SuppressWarnings("SleepWhileInLoop")
    private void drawLoop() {
        BufferStrategy strategy = createAndReturnBufferStrategy();

        long timeLast = System.nanoTime();
        while (true) {
            long timeNow = System.nanoTime();
            double diff = (timeNow - timeLast) * 0.000000001d;
            timeLast = timeNow;
            do {
                do {
                    Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
                    g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
                    drawWindow(g2d);

                    //DEBUG
                    g2d.setColor(Color.red);
                    g2d.drawString(Double.toString(diff), 50, 50);

                    g2d.dispose();
                } while (strategy.contentsRestored());
                strategy.show();
            } while (strategy.contentsLost());

            //Sleep to limit framerate -> reduce processor use
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                //Do nothing
            }
        }
    }
    //"andReturn" added to avoid name conflicts with JFrame.createBufferStrategy()
    private BufferStrategy createAndReturnBufferStrategy() {
        final int buffers = 2;
        try {
            //API docs say BufferStrategy should only be created within AWT EDT.
            //So that's what we're going to do. InvokeAndWait executes in EDT.
            SwingUtilities.invokeAndWait(() -> {
                try {
                    BufferCapabilities bufCap = new BufferCapabilities(
                            new ImageCapabilities(true),
                            new ImageCapabilities(true),
                            BufferCapabilities.FlipContents.BACKGROUND);
                    this.createBufferStrategy(buffers, bufCap);
                } catch (AWTException ex) {
                    System.err.println("Could not create accelerated buffers!");
                    try {
                        BufferCapabilities noAcceleration = new BufferCapabilities(
                                new ImageCapabilities(false),
                                new ImageCapabilities(false),
                                BufferCapabilities.FlipContents.BACKGROUND);
                        this.createBufferStrategy(buffers, noAcceleration);
                        System.err.println("Created unaccelerated buffers instead");
                    } catch (AWTException ex1) {
                        System.err.println("Could not create unaccelerated buffers either");
                        System.err.println("Falling back to non-specific buffer, "
                                + "page flipping will not work properly");
                        this.createBufferStrategy(buffers);
                    }
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            //Something interrupted our invokeAndWait or created an exception that wasnt caught
            //This should never happen
            throw new Error(
                    "Some bizzare error happened while trying to create buffer strategy: " + ex.getMessage(), ex);
        }
        return this.getBufferStrategy();
    }
    private void drawWindow(Graphics2D g2d) {
        Insets insets = this.getInsets();
        double startX = 0 + insets.left;
        double startY = 0 + insets.top;
        double sideX = this.getWidth() - insets.left - insets.right;
        double sideY = this.getHeight() - insets.top - insets.bottom;
        double centerX = startX + sideX / 2d;
        double centerY = startY + sideY / 2d;

        AffineTransform defaultAff = g2d.getTransform();

        g2d.setTransform(defaultAff);
        drawSquares(g2d, startX, startY, sideX, sideY);
        g2d.fillRect((int) centerX - 10, (int) centerY - 10, 10, 10);
    }
    private void drawSquares(Graphics2D g2d, double startX, double startY, double width, double height) {
        final int tilesize = 100;
        final int halftile = tilesize / 2;
        double tilesX = width / tilesize + 1;
        double tilesY = height / halftile * 2 + 0;
        int tileOffsetX = (int) (width / 2 - cameraX) / halftile;
        int tileOffsetY = (int) (height / 2 - cameraY) / halftile * 2;

        AffineTransform defaultAff = g2d.getTransform();
        Shape tile = new Path2D.Double() {
            {
                moveTo(halftile, 0);
                lineTo(tilesize, halftile);
                lineTo(halftile, tilesize);
                lineTo(0, halftile);
                lineTo(halftile, 0);
            }
        };
        //x1 & y1 = top-leftmost square
        int x1 = 0, y1 = 0;
        Point p = IsometricUtilities.coord2iso(cameraX, cameraY);
        x1 = p.x;
        y1 = p.y;
        //TODO: init default values based on camera location
        boolean b = false;
        g2d.setColor(Color.GREEN);
        for (int y = 0; y < tilesY; y++) {
            AffineTransform aff = (AffineTransform) defaultAff.clone();
            aff.translate(startX, startY);
            aff.scale(1, 0.5);
            //start at center of tile
            aff.translate(-halftile, -halftile);
            //set initial x square to -1 because we increase it next
            aff.translate(-tilesize, halftile * y);
            if (y % 2 != 0) aff.translate(halftile, 0);
            int x2 = x1, y2 = y1;
            for (int x = 0; x < tilesX; x++) {
                aff.translate(tilesize, 0);
                //TODO: use camera & tileOffset x/y variables
                if (game.squareExists(x2, y2)) {
                    g2d.setTransform(aff);
                    g2d.setColor(Color.GREEN);
                    g2d.draw(tile);
                    g2d.drawString(x + "," + y, 35, 50);
                    g2d.setColor(Color.YELLOW);
                    g2d.drawString(x2 + "," + y2, 35, 70);
                }
                x2 --;
                y2 ++;
            }
            if (b) x1++;
            else y1++;
            b = !b;
        }
        g2d.setTransform(defaultAff);
        g2d.setColor(Color.red);
        g2d.drawString(cameraX + " " + cameraY, 100, 100);
    }

    private void inputLoop() {

    }
    private void setCameraLocation(int x, int y) {
        cameraX = x;
        cameraY = y;
    }

    public static void launchNewWindow(int sizeX, int sizeY) {
        GameWindow w = new GameWindow();
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setTitle("Isometric Strategy Thing");
        w.setSize(sizeX, sizeY);
        w.setLocationRelativeTo(null);
        w.setIgnoreRepaint(true);
        w.setBackground(Color.BLACK);
        w.setVisible(true);

        Thread drawThread = new Thread(() -> w.drawLoop(), "DrawThread");
        Thread moveThread = new Thread(() -> w.inputLoop(), "InputThread");

        moveThread.setPriority(Thread.MAX_PRIORITY - 1);
        drawThread.setPriority(Thread.MAX_PRIORITY);
        drawThread.start();
        moveThread.start();
    }

    private class CustomKeyListener extends KeyAdapter {
        private final KeyBindings binds;
        public CustomKeyListener(KeyBindings bindings) {
            binds = bindings;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (binds.getActionFor(e.getKeyCode())) {
                case CAMERA_UP:
                    cameraX--;
                    break;
                case CAMERA_DOWN:
                    cameraX++;
                    break;
                case CAMERA_LEFT:
                    cameraY--;
                    break;
                case CAMERA_RIGHT:
                    cameraY++;
                    break;
            }
        }
    }
    private class CustomMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            //Todo: convert to tiles & remove insets

            Point p = IsometricUtilities.coord2iso(x, y);
            setCameraLocation(p.x, p.y);
        }
    }
}