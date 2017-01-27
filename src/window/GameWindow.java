package window;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class GameWindow extends JFrame {
    private GameWindow() {

    }

    @SuppressWarnings("SleepWhileInLoop")
    private void drawLoop() {
        BufferCapabilities bufCap = new BufferCapabilities(
                new ImageCapabilities(true),
                new ImageCapabilities(true),
                BufferCapabilities.FlipContents.BACKGROUND);
        /*TODO: bufferStrategy should be created from AWT EDT via invokeLater()
         * maybe figure out how and where to do this
        createAndReturnBufferStrategy(bufCap); -> wait -> notify -> ??*/
        BufferStrategy strategy = createAndReturnBufferStrategy(bufCap);

        long timeLast = System.nanoTime();
        while (true) {
            long timeNow = System.nanoTime();
            double diff = (timeNow - timeLast) * 0.000000001d;
            timeLast = timeNow;
            do {
                do {
                    Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
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
    private BufferStrategy createAndReturnBufferStrategy(BufferCapabilities bufCap) {
        try {
            this.createBufferStrategy(2, bufCap);
        } catch (AWTException ex) {
            System.err.println("Could not create accelerated buffers!");
            try {
                bufCap = new BufferCapabilities(
                    new ImageCapabilities(false),
                    new ImageCapabilities(false),
                    BufferCapabilities.FlipContents.BACKGROUND);
                this.createBufferStrategy(2, bufCap);
                System.err.println("Created unaccelerated buffers instead");
            } catch (AWTException ex1) {
                this.createBufferStrategy(2);
                System.err.println("Could not create unaccelerated buffers either");
                System.err.println("Falling back to non-specific buffer, page flipping will not work properly");
            }
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
    }
    private void drawSquares(Graphics2D g2d, double startX, double startY, double width, double height) {
        final int tilesize = 100;
        final int halftile = tilesize / 2;
        double tilesX = width / tilesize + 1;
        double tilesY = height / halftile * 2;
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
        g2d.setColor(Color.GREEN);
        for (int y = 0; y < tilesY; y++) {
            AffineTransform aff = (AffineTransform) defaultAff.clone();
            aff.translate(startX, startY);
            aff.scale(1, 0.5);
            //start at center of tile
            aff.translate(-halftile, -halftile);
            //set initial x square to -1 because we increase it next
            aff.translate(-tilesize, halftile * y);
            if (y % 2 == 0) aff.translate(0, halftile);
            for (int x = 0; x < tilesX; x++) {
                aff.translate(tilesize, 0);
                g2d.setTransform(aff);
                g2d.draw(tile);
            }
        }
        g2d.setTransform(defaultAff);
    }

    private void inputLoop() {

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
}