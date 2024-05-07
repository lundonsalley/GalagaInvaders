//-----enhancements----------\\

/*
-High Score is shown on game over screen

-lives are added for every 500 points
-stars reused/change color
-ship changes to a random color when a life is gained
-if the player has more than 3 lives, the player shoots at a faster speed and the lasers have a rainbow effect
-if ship is hit, player gain invincibility for a bit
-boss invader appears when the player reaches a score of 1,000
-enemies start speeding up after each boss kill
-bonus ship appears when you kill the boss, shoots lasers at the same time as the player
-boss explodes into small triangles if killed
 */

package space_invaders_start;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class SPACE_INVADERS_Start extends JFrame implements Runnable {

    static final int WINDOW_WIDTH = 500;
    static final int WINDOW_HEIGHT = 800;
    final int YBORDER = 40;
    final int XBORDER = 20;
    final int YTITLE = 25;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    ////////////===================================================////////////
    // Custom Colors
    Color grassGreen = new Color(30, 130, 60);
    Color darkBlue = new Color(20, 50, 120);
    Color purple = new Color(160, 70, 160);
    Color ocean = new Color(110, 150, 200);
    Color lightBlue = new Color(180, 210, 250);
    Color pumkinOrange = new Color(200, 125, 0);
    Color yellowish = new Color(255, 255, 200);
    // Opeque Yelowish Color - Last, 4th value can be 0-255 for transparency

    //////////====================================================/////////////
    // ADD NEW VARIABLES
    //stars
    int numStars = 100;
    int xStars[] = new int[numStars];
    int yStars[] = new int[numStars];
    int red[] = new int[numStars];
    int green[] = new int[numStars];
    int blue[] = new int[numStars];

    //Command
    boolean GameOver;
    boolean laserWall;
    int red2;
    int green2;
    int blue2;
    int fade2;
    int lives;
    int timer;
    int wait;
    boolean flashing;
    int movey;
    int movex;
    int score;
    int bonus;
    boolean lifeup;
    int fade;
    int highscore = 0;
    boolean scoreup;
    boolean bossbattle;
    boolean powerup;
    int level;

    //EXPLOSION
    boolean boom;
    boolean repeat;
    int explosionfade;
    int numShards = 50;
    int xShards[] = new int[numShards];
    int yShards[] = new int[numShards];

    // SHIP
    int SeeShip;
    int normalShip;
    int xCannon;
    int yCannon;
    int redship;
    int greenship;
    int blueship;

    //mini ship
    int xMini;
    int yMini;

    // LASER
    int numLasers = 200;
    int currentLaser;
    int xLaser[] = new int[numLasers];
    int yLaser[] = new int[numLasers];
    boolean SeeLaser[] = new boolean[numLasers];
    int redlaser;
    int greenlaser;
    int bluelaser;

    // INVADERS--should be 60 wide
    int deltaInvaders;
    int numInvaders = 10;
    int xInvader[] = new int[numInvaders];
    int yInvader[] = new int[numInvaders];
    boolean visibleInvader[] = new boolean[numInvaders];
    int scoreInvader[] = new int[numInvaders];
    int xposInvader;

    //boss
    int xboss;
    int yboss;
    int bosshealth;
    int bossfightstart;
    double bar;

    //strings
    int Float;

    ///////////===================================================/////////////
////////////////////////////////////////////////////////////////
    static SPACE_INVADERS_Start frame;

    public static void main(String[] args) {
        frame = new SPACE_INVADERS_Start();
        frame.setTitle("Galaga (the lower-quality edition)");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public SPACE_INVADERS_Start() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    // CANNONBALL
                    if (currentLaser < numLasers) {
                        xLaser[currentLaser] = xCannon;
                        yLaser[currentLaser] = yCannon;
                        SeeLaser[currentLaser] = true;
                        currentLaser++;
                    } else {
                        currentLaser = 0;
                    }

                    if (powerup && yMini > (yCannon - 25)) {
                        if (currentLaser + 25 < numLasers) {
                            xLaser[currentLaser + 25] = xMini;
                            yLaser[currentLaser + 25] = yMini;
                            SeeLaser[currentLaser + 25] = true;
                            currentLaser++;
                        } else {
                            currentLaser = 0;
                        }
                    }

                }
                if (e.BUTTON3 == e.getButton()) //right button
                {
                    reset();
                }

                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {

                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                // WHEN MOUSE MOVES - CANNON
                if (!GameOver) {
                    xCannon = e.getX() - getX(0);
                }

                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                } else if (e.VK_DOWN == e.getKeyCode()) {
                } else if (e.VK_LEFT == e.getKeyCode()) {
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                } else if (e.VK_SPACE == e.getKeyCode()) {
                    if (currentLaser < numLasers) {
                        xLaser[currentLaser] = xCannon;
                        yLaser[currentLaser] = yCannon;
                        SeeLaser[currentLaser] = true;
                        currentLaser++;
                    } else {
                        currentLaser = 0;
                    }

                    if (powerup && yMini > (yCannon - 25)) {
                        if (currentLaser + 25 < numLasers) {
                            xLaser[currentLaser + 25] = xMini;
                            yLaser[currentLaser + 25] = yMini;
                            SeeLaser[currentLaser + 25] = true;
                            currentLaser++;
                        } else {
                            currentLaser = 0;
                        }
                    }
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////

    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////

    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////

    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.black);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.black);
        g.fillPolygon(x, y, 4);
// draw border

        Color border = new Color(red2, green2, blue2);
        g.setColor(border);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        // CALL DRAW METHODS HERE =====================================================

        //stars
        for (int i = 0; i < numStars; i++) {
            red[i] = (int) (Math.random() * (255 - 1) + 1);
            green[i] = (int) (Math.random() * (255 - 1) + 1);
            blue[i] = (int) (Math.random() * (255 - 1) + 1);
            Color star = new Color(red[i], green[i], blue[i], 150);
            g.setColor(star);

            for (int a = 0; a < numStars; a++) {
                DrawStars(xStars[a], yStars[a], 0, 0.75, 0.75, star);
            }
        }

        //highscore
        if (score > highscore) {
            highscore = score;
        }

        //game over
        if (GameOver) {
            StringTransform("Game Over", Font.MONOSPACED, getWidth2() / 2, getHeight2() / 2, 0, 8, 8);
            StringTransform("High Score:" + highscore, Font.MONOSPACED, getWidth2() / 2, (getHeight2() / 2) - 60, 0, 5, 5);
            StringTransform("Right-Click to try again", Font.MONOSPACED, getWidth2() / 2, (getHeight2() / 2) - 150, 0, 3, 3);
        }

        //boss hp
        if (bossbattle) {
            g.setColor(Color.red);
            RectTransform(xboss, yboss + 50, 0, bar, 0.75, true);
            g.setColor(Color.blue);
            StringTransform("" + bosshealth, Font.MONOSPACED, xboss, yboss + 50, 0, 1.5, 1.5);
        }

        //score
        Color blue = new Color(48, 238, 254);
        g.setColor(blue);
        if (score >= 0) {
            StringTransform("Score:" + score, Font.MONOSPACED, getWidth2() / 2, getHeight2() + 12, 0, 2.5, 2.5);
        } else if (score < 0) {
            StringTransform("Score:0", Font.MONOSPACED, getWidth2() / 2, getHeight2() + 12, 0, 2.5, 2.5);
        }

        //lives
        StringTransform("Lives " + lives, Font.MONOSPACED, 50, getHeight2() + 12, 0, 2, 2);

        //add life
        Color green = new Color(11, 232, 61, fade);
        g.setColor(green);
        if (lifeup) {
            StringTransform("+1 Life", Font.MONOSPACED, xCannon + 50, Float, 0, 2, 2);
        }

        // LASER 
        
        for (int i = 0; i < numLasers; i++) {
            if (SeeLaser[i]) {
                if(!powerup){
                    Color laser = new Color(redlaser, greenlaser, bluelaser);
                    DrawLaser(xLaser[i], yLaser[i], 0, 0.5, 0.5, laser);
                }else{
                    Color laser = new Color(redlaser, greenlaser, bluelaser);
                    DrawLaser(xLaser[i], yLaser[i], 0, 0.5, 0.5, laser);
                }
            }
        }

        // SHIP
        Color ship = new Color(redship, blueship, greenship);
        if (flashing == true) {
            if (timer % 4 == 0) {
                SeeShip = 1;
            } else if (timer % 2 == 0 && !(timer % 4 == 0)) {
                SeeShip = 0;
            }
        } else {
            DrawShip(xCannon, yCannon, 0, 0.5, 0.5, ship);
        }

        if (SeeShip == 1) {
            DrawShip(xCannon, yCannon, 0, 0.5, 0.5, ship);
        } else if (SeeShip == 0) {

        }

        //MINI SHIP
        DrawShip(xMini, yMini, 0, 0.3, 0.3, ship);

        // INVADERS
        for (int i = 0; i < numInvaders; i++) {
            if (visibleInvader[i]) {
                DrawInvaders(xInvader[i], yInvader[i], 0, .5, .5, Color.black);
            }
        }

        //scores on invaders
        g.setColor(Color.black);
        for (int i = 0; i < numInvaders; i++) {
            StringTransform("" + scoreInvader[i], "MONOSPACED BOLD", xInvader[i], yInvader[i], 0, 1.5, 1.5);
        }

        //BOSS INVADER
        if (bossbattle) {
            DrawInvadersBoss(xboss, yboss, 0, 0.5, 0.5, Color.black);
        }

        //EXPLOSION
        for (int i = 0; i < numShards; i++) {
            if (boom) {
                Color orange = new Color(219, (int) (Math.random() * ((210 - 30)) + 30), 26, explosionfade);
                DrawShard(xShards[i], yShards[i], (Math.random() * (361 - 1) + 1), 2, 2, orange);
            }
        }

        // DO NOT EDIT CODE BELLOW   ==================================================
        gOld.drawImage(image, 0, 0, null);
    }

////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////  
    public void RectTransform(int xpos, int ypos, double rot, double xscale, double yscale, boolean fill) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        if (fill) {
            g.fillRect(-10, -10, 20, 20);
        } else {
            g.drawRect(-10, -10, 20, 20);
        }
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void OvalTransform(int xpos, int ypos, double rot, double xscale, double yscale, boolean fill) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        if (fill) {
            g.fillOval(-10, -10, 20, 20);
        } else {
            g.drawOval(-10, -10, 20, 20);
        }
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void ArcTransform(int start, int sweep, int xpos, int ypos, double rot, double xscale, double yscale, boolean fill) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        if (fill) {
            g.fillArc(-10, -10, 20, 20, start, sweep);
        } else {
            g.drawArc(-10, -10, 20, 20, start, sweep);
        }
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void StringTransform(String text, String font, int xpos, int ypos, double rot, double xscale, double yscale) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        g.setFont(new Font(font, Font.PLAIN, 10));
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        g.drawString(text, -width / 2, height / 4);

        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

////////////////////////////////////////////////////////////////////////////////
    public void RectCentered(int xpos, int ypos, int width, int height, boolean fill) {
        xpos = xpos - width / 2;
        ypos = ypos + height / 2;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        if (fill) {
            g.fillRect(xpos, ypos, width, height);
        } else {
            g.drawRect(xpos, ypos, width, height);
        }
    }

    public void OvalCentered(int xpos, int ypos, int width, int height, boolean fill) {
        xpos = xpos - width / 2;
        ypos = ypos + height / 2;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        if (fill) {
            g.fillOval(xpos, ypos, width, height);
        } else {
            g.drawOval(xpos, ypos, width, height);
        }
    }

    public void ArcCentered(int xpos, int ypos, int width, int height, int start, int sweep, boolean fill) {
        xpos = xpos - width / 2;
        ypos = ypos + height / 2;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        if (fill) {
            g.fillArc(xpos, ypos, width, height, start, sweep);
        } else {
            g.drawArc(xpos, ypos, width, height, start, sweep);
        }
    }

    public void StringCentered(int xpos, int ypos, String text, String font, int size) {
        g.setFont(new Font(font, Font.PLAIN, size));
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        xpos = xpos - width / 2;
        ypos = ypos - height / 4;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        g.drawString(text, xpos, ypos);
    }

////////////////////////////////////////////////////////////////////////////////
    public void RectOriginCentered(int xpos, int ypos, int width, int height, boolean fill) {
        xpos = xpos - width / 2;
        ypos = -(ypos + height / 2);
        if (fill) {
            g.fillRect(xpos, ypos, width, height);
        } else {
            g.drawRect(xpos, ypos, width, height);
        }
    }

    public void OvalOriginCentered(int xpos, int ypos, int width, int height, boolean fill) {
        xpos = xpos - width / 2;
        ypos = -(ypos + height / 2);
        if (fill) {
            g.fillOval(xpos, ypos, width, height);
        } else {
            g.drawOval(xpos, ypos, width, height);
        }
    }

    public void ArcOriginCentered(int xpos, int ypos, int width, int height, int start, int sweep, boolean fill) {
        xpos = xpos - width / 2;
        ypos = -(ypos + height / 2);
        if (fill) {
            g.fillArc(xpos, ypos, width, height, start, sweep);
        } else {
            g.drawArc(xpos, ypos, width, height, start, sweep);
        }
    }

////////////////////////////////////////////////////////////////////////////  
    public void RectOriginTransform(int xpos, int ypos, double rot, double xscale, double yscale, boolean fill) {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        if (fill) {
            g.fillRect(-10, -10, 20, 20);
        } else {
            g.drawRect(-10, -10, 20, 20);
        }
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void OvalOriginTransform(int xpos, int ypos, double rot, double xscale, double yscale, boolean fill) {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        if (fill) {
            g.fillOval(-10, -10, 20, 20);
        } else {
            g.drawOval(-10, -10, 20, 20);
        }
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void ArcOriginTransform(int start, int sweep, int xpos, int ypos, double rot, double xscale, double yscale, boolean fill) {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        if (fill) {
            g.fillArc(-10, -10, 20, 20, start, sweep);
        } else {
            g.drawArc(-10, -10, 20, 20, start, sweep);
        }
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void StringOriginTransform(String text, String font, int xpos, int ypos, double rot, double xscale, double yscale) {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        g.setFont(new Font(font, Font.PLAIN, 10));
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        g.drawString(text, -width / 2, height / 4);

        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    public void PolygonOrigin(int xvals[], int yvals[], boolean fill) {
        for (int i = 0; i < xvals.length; i++) {
            yvals[i] = -yvals[i];
        }
        if (fill) {
            g.fillPolygon(xvals, yvals, xvals.length);
        } else {
            g.drawPolygon(xvals, yvals, xvals.length);
        }
    }

////////////////////////////////////////////////////////////////
    // DRAW Ship ============================================================
//======= Change this code and place your own Ship here ======================
    public void DrawShip(int xpos, int ypos, double rot, double xscale, double yscale, Color color) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        //----------------------------------------------------\\
        Color newBlue = new Color(84, 114, 165);
        g.setColor(newBlue);
        RectOriginTransform(0, 10, 0, 4.5, 1, true);

        Color newRed = new Color(206, 63, 61);
        g.setColor(newRed);
        RectOriginTransform(-40, 30, 0, 0.5, 2, true);
        RectOriginTransform(40, 30, 0, 0.5, 2, true);
        RectOriginTransform(-70, 5, 0, 0.5, 1.5, true);
        RectOriginTransform(70, 5, 0, 0.5, 1.5, true);
        RectOriginTransform(-25, -30, 0, 1, 2, true);
        RectOriginTransform(25, -30, 0, 1, 2, true);

        g.setColor(color);
        int xvals[] = {0, -5, -5, -15, -15, -25, -25, -35, -35, -45, -45, -35, -35, -45, -45, -55, -55, -65, -65, -75, -75, -65, -65, -55, -55, -45, -45, -25, -25, -15, -15, -5, -5, 0, 0, 5, 5, 15, 15, 25, 25, 35, 35, 45, 45, 35, 35, 45, 45, 55, 55, 65, 65, 75, 75, 65, 65, 55, 55, 45, 45, 25, 25, 15, 15, 5, 5, 0};
        int yvals[] = {100, 100, 70, 70, 30, 30, 10, 10, 30, 30, 10, 10, 0, 0, -10, -10, -20, -20, 0, 0, -60, -60, -50, -50, -40, -40, -30, -30, -20, -20, -40, -40, -60, -60, 100, 100, 70, 70, 30, 30, 10, 10, 30, 30, 10, 10, 0, 0, -10, -10, -20, -20, 0, 0, -60, -60, -50, -50, -40, -40, -30, -30, -20, -20, -40, -40, -60, -60};
        PolygonOrigin(xvals, yvals, true);

        g.setColor(newRed);
        RectOriginTransform(-9, 0, 0, 0.5, 1, true);
        RectOriginTransform(9, 0, 0, 0.5, 1, true);
        RectOriginTransform(0, 10, 0, 0.6, 1, true);

        //-----------------------------------------------------\\
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    // DRAW LASER ========================================================    
    public void DrawLaser(int xpos, int ypos, double rot, double xscale, double yscale, Color color) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        //----------------------------------------------------\\

        g.setColor(color);
        RectOriginCentered(20, 0, 10, 60, true);
        RectOriginCentered(-20, 0, 10, 60, true);
        RectOriginCentered(20, 0, 5, 65, true);
        RectOriginCentered(-20, 0, 5, 65, true);

        //-----------------------------------------------------\\
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    //stars
    public void DrawStars(int xpos, int ypos, double rot, double xscale, double yscale, Color color) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        //----------------------------------------------------\\
        g.setColor(color);
        RectOriginCentered(0, 0, 5, 5, true);
        //-----------------------------------------------------\\
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    // DRAW INVADERS ==========================================================
    //scorpion
    public void DrawInvaders(int xpos, int ypos, double rot, double xscale, double yscale, Color color) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        //----------------------------------------------------\\
        Color teal = new Color(3, 254, 223);
        g.setColor(teal);
        int xvals[] = {0, -25, -25, -35, -35, -45, -45, -55, -55, -45, -45, -35, -35, 0, 0, 35, 35, 45, 45, 55, 55, 45, 45, 35, 35, 25, 25, 0};
        int yvals[] = {0, 0, 40, 40, 30, 30, 40, 40, 20, 20, 0, 0, -10, -10, -10, -10, 0, 0, 20, 20, 40, 40, 30, 30, 40, 40, 0, 0};
        PolygonOrigin(xvals, yvals, true);

        Color yellow = new Color(254, 184, 0);
        g.setColor(yellow);
        int xvalsbody[] = {-5, -5, -15, -15, -5, -5, -25, -25, -35, -35, -45, -45, -35, -35, -25, -25, -5, -5, 5, 5, 15, 15, 5, 5};
        int yvalsbody[] = {30, 20, 20, -30, -30, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -60, -60, -50, -50, -30, -30, 20, 20, 30};
        PolygonOrigin(xvalsbody, yvalsbody, true);

        Color newnewred = new Color(254, 0, 0);
        g.setColor(newnewred);
        RectOriginTransform(-10, 40, 0, 0.5, 1, true);
        RectOriginTransform(10, 40, 0, 0.5, 1, true);

        //-----------------------------------------------------\\
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    //boss
    public void DrawInvadersBoss(int xpos, int ypos, double rot, double xscale, double yscale, Color color) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        //----------------------------------------------------\\
        Color teal = new Color(0, 150, 136);
        g.setColor(teal);
        int xvalsBody[] = {0, -5, -5, -15, -15, -45, -45, -35, -35, -25, -25, -35, -35, -45, -45, -75, -75, -85, -85, -75, -75, -85, -85, -75, -75, -55, -55, -45, -45, -35, -35, 0, 35, 35, 45, 45, 55, 55, 75, 75, 85, 85, 75, 75, 85, 85, 75, 75, 45, 45, 35, 35, 25, 25, 35, 35, 45, 45, 15, 15, 5, 5, 0};
        int yvalsBody[] = {40, 40, 70, 70, 50, 50, 40, 40, 30, 30, 20, 20, 10, 10, 0, 0, -10, -10, -20, -20, -40, -40, -100, -100, -110, -110, -100, -100, -50, -50, -40, -40, -40, -50, -50, -100, -100, -110, -110, -100, -100, -40, -40, -20, -20, -10, -10, 0, 0, 10, 10, 20, 20, 30, 30, 40, 40, 50, 50, 70, 70, 40, 40};
        PolygonOrigin(xvalsBody, yvalsBody, true);

        Color orange = new Color(244, 67, 54);
        g.setColor(orange);
        RectOriginTransform(-10, -50, 0, 0.5, 1, true);
        RectOriginTransform(10, -50, 0, 0.5, 1, true);
        RectOriginTransform(-15, 35, 0, 1, 1.5, true);
        RectOriginTransform(15, 35, 0, 1, 1.5, true);

        int xvals[] = {-55, -65, -65, -75, -75, -55, -55, -65, -65, -55};
        int yvals[] = {-40, -40, -60, -60, -90, -90, -70, -70, -60, -60};
        PolygonOrigin(xvals, yvals, true);

        int xvals2[] = {55, 65, 65, 75, 75, 55, 55, 65, 65, 55};
        int yvals2[] = {-40, -40, -60, -60, -90, -90, -70, -70, -60, -60};
        PolygonOrigin(xvals2, yvals2, true);

        Color yellow = new Color(255, 235, 59);
        g.setColor(yellow);

        int xvals3[] = {0, -5, -5, -35, -35, 35, 35, 5, 5};
        int yvals3[] = {0, 0, 10, 10, -40, -40, 10, 10, 0};
        PolygonOrigin(xvals3, yvals3, true);

        //-----------------------------------------------------\\
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

    //Shards
    public void DrawShard(int xpos, int ypos, double rot, double xscale, double yscale, Color color) {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod, yposMod);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);
        //----------------------------------------------------\\
        g.setColor(color);

        int xvalsBody[] = {0, (int) (Math.random() * ((20 - 1)) + 1), (int) (Math.random() * ((20 - 1)) + 1)};
        int yvalsBody[] = {0, (int) (Math.random() * ((20 - 1)) + 1), (int) (Math.random() * ((20 - 1)) + 1)};
        PolygonOrigin(xvalsBody, yvalsBody, true);

        //-----------------------------------------------------\\
        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xposMod, -yposMod);
    }

////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////

    public void reset() {
        // RESET VALUES HERE ================================================

        //border
        red2 = 255;
        green2 = 0;
        blue2 = 0;

        //laser wall
        laserWall = false;

        for (int i = 0; i < numStars; i++) {
            xStars[i] = (int) (Math.random() * ((getWidth2() - 15) - 15) + 15);
            yStars[i] = (int) (Math.random() * ((getHeight() - 120) - 10) + 10);
        }

        //command
        GameOver = false;
        lives = 3;
        timer = 0;
        wait = 1;
        flashing = false;
        movey = 1;
        movex = 1;
        score = 0;
        bonus = 500;
        lifeup = false;
        fade = 255;
        powerup = false;
        level = 1;

        // SHIP
        SeeShip = 1;
        normalShip = 1;
        xCannon = getWidth2() / 2;
        yCannon = 0;
        redship = 255;
        greenship = 255;
        blueship = 255;

        //MINI SHIP
        xMini = xCannon + 100;
        yMini = yCannon - 100;

        //EXPLOSION
        repeat = true;
        boom = false;
        explosionfade = 255;
        //EXPLOSION
        for (int i = 0; i < numShards; i++) {
            xShards[i] = xboss;
            yShards[i] = yboss;
        }

        // LASER
        for (int i = 0; i < numLasers; i++) {
            SeeLaser[i] = false;
            xLaser[i] = xCannon;
            yLaser[i] = yCannon;
        }
        currentLaser = 0;

        redlaser = 255;
        greenlaser = 0;
        bluelaser = 0;

        // INVADERS
        xposInvader = 70;

        for (int i = 0; i < numInvaders; i++) {

            xInvader[i] = (int) (Math.random() * ((getWidth2() - 20) - 20) + 20);
            yInvader[i] = (int) ((Math.random() * (getHeight2() - (getHeight2() - 100))) + getHeight2() - 100);

            visibleInvader[i] = true;
        }

        for (int i = 0; i < numInvaders; i++) {
            scoreInvader[i] = (int) (Math.random() * ((6 - 1)) + 1);
        }

        deltaInvaders = -1;

        //BOSS
        xboss = getWidth2() / 2;
        yboss = getHeight2();
        bosshealth = 100;
        bossbattle = false;
        bossfightstart = 1000;
        bar = 8;

        //strings
        Float = 50;

        // DONE WITH RESET ===================================================
    }
/////////////////////////////////////////////////////////////////////////

    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();
        }
        // ANIMATE VALUES HERE ===============================================

        //stars
        for (int i = 0; i < numStars; i++) {
            if (yStars[i] < 5) {
                yStars[i] = getHeight2() - 5;
                xStars[i] = (int) (Math.random() * ((getWidth2() - 15) - 15) + 15);
            }
        }

        //command
        timer++;

        //MINI SHIP
        if (powerup) {
            if (yMini <= (yCannon - 10)) {
                yMini++;
            }
            xMini = xCannon + 100;
        } else if (!powerup && yMini > (yCannon - 100)) {
            yMini--;
        }

        // LASER
        for (int i = 0; i < numLasers; i++) {
//        if(laserWall==true){
//            SeeLaser[i]=true;
//            yLaser[i]+=movey*1;
//            xLaser[i]=i*10;
//            
//            if(yLaser[i]>=getHeight2()-5){
//            laserWall=false;
//            yLaser[i]=0;
//            SeeLaser[i] = false;
//            yLaser[i]=yCannon;
//            xLaser[i]=xCannon;
//            
//            }
//        }
//        
            if (powerup) {
                redlaser = (int) (Math.random() * (255 - 0) + 0);
                greenlaser = (int) (Math.random() * (255 - 0) + 0);
                bluelaser = (int) (Math.random() * (255 - 0) + 0);
            } else {
                redlaser = 255;
                greenlaser = 0;
                bluelaser = 0;
            }

            if (SeeLaser[i]) {
                if (powerup) {
                    yLaser[i] += movey * 15;
                }
                yLaser[i] += movey * 10;
            }
            if (yLaser[i] >= getHeight2()) {
                SeeLaser[i] = false;
                yLaser[i] = yCannon;
                xLaser[i] = xCannon;
            }
        }
        

        //invaders move
        if (!GameOver) {
            for (int i = 0; i < numInvaders; i++) {
                if (yInvader[i] < (getHeight2() / 2) + 25) {
                    yInvader[i] -= (movey * 8) + level;
                } else {
                    yInvader[i] -= (movey * 4) + level;
                }
            }
        }

        //reuse
        if (!GameOver) {
            for (int i = 0; i < numInvaders; i++) {
                if (yInvader[i] < 0) {
                    xInvader[i] = (int) (Math.random() * ((getWidth2() - 20) - 20) + 20);
                    yInvader[i] = (int) ((Math.random() * (getHeight2() - (getHeight2() - 100))) + getHeight2() - 100);
                    visibleInvader[i] = true;
                    score -= scoreInvader[i] * 2;
                }
            }
        }

        //bounce
        for (int x = 0; x < numInvaders; x++) {
            if (xInvader[x] > getWidth2() - 10 || xInvader[x] < 10) {
                deltaInvaders *= movex * -1;
            }
            xInvader[x] += movex * deltaInvaders;
        }

        //hit
        if (timer % wait == 0) {
            wait = 1;
            normalShip = 1;
            flashing = false;
            for (int i = 0; i < numInvaders; i++) {
                if (visibleInvader[i] && xInvader[i] > xCannon - 60 && xInvader[i] < xCannon + 60 && yInvader[i] < yCannon + 75 && flashing == false) {
                    lives--;
                    powerup = false;
                    redship = 255;
                    greenship = 255;
                    blueship = 255;
                    red2 = (int) (Math.random() * (255 - 0) + 0);
                    green2 = (int) (Math.random() * (255 - 0) + 0);
                    blue2 = (int) (Math.random() * (255 - 0) + 0);
                    if (lives > 0) {
                        normalShip = 0;
                        flashing = true;
                    }
                    wait = timer + 50;
                }
            }
        }

        // INVADERS and COLLISIONS
        for (int i = 0; i < numInvaders; i++) {
            for (int j = 0; j < numLasers; j++) {
                if ((visibleInvader[i]
                        && xLaser[j] > xInvader[i] - 40
                        && xLaser[j] < xInvader[i] + 40
                        && yLaser[j] > yInvader[i] - 20
                        && yLaser[j] < yInvader[i] + 20
                        && SeeLaser[j])
                        || (xInvader[i] > xCannon - 60
                        && xInvader[i] < xCannon + 60
                        && yInvader[i] < yCannon + 74
                        && !flashing)) {

                    xInvader[i] = (int) (Math.random() * ((getWidth2() - 20) - 20) + 20);
                    yInvader[i] = (int) ((Math.random() * (getHeight2() - (getHeight2() - 100))) + getHeight2() - 100);
                    scoreInvader[i] = (int) (Math.random() * ((6 - 1)) + 1);
                    SeeLaser[j] = false;
                    score += scoreInvader[i];
                    scoreup = true;
                }
            }
        }

        //BOSS FIGHT
        for (int i = 0; i < numLasers; i++) {
            if (bossbattle
                    && xLaser[i] > xboss - 80
                    && xLaser[i] < xboss + 80
                    && yLaser[i] > yboss - 40
                    && yLaser[i] < yboss + 40
                    && SeeLaser[i]) {
                SeeLaser[i] = false;
                bosshealth--;
                bar -= 0.08;

            }
        }

        if (score >= bossfightstart) {
            bossbattle = true;
        }

        if (bossbattle) {
            if (bosshealth > 0 && yboss > 0 && !GameOver) {
                yboss--;
            } else if (bosshealth <= 0) {
                boom = true;
                lives++;
                score += 250;
                bossbattle = false;
                powerup = true;
                level += 2;
                bossfightstart += bossfightstart;
                bosshealth = 100;
                bar = 8;
            } else if (yboss <= 0
                    || (xboss > xCannon - 100
                    && xboss < xCannon + 100
                    && xboss < yCannon + 114
                    && !flashing)) {
                GameOver = true;
            }
        }

        //power up ship color change
        if (powerup) {
            redship = (int) (Math.random() * (255 - 1) + 1);
            greenship = (int) (Math.random() * (255 - 1) + 1);
            blueship = (int) (Math.random() * (255 - 1) + 1);
        }

        //EXPLOSION
        if (repeat) {
            for (int i = 0; i < numShards; i++) {
                xShards[i] = xboss;
                yShards[i] = yboss;
            }
        }

        if (boom) {
            if (explosionfade > 0) {
                repeat = false;
                for (int i = 0; i < numShards; i += 2) {
                    xShards[i] += 10 * Math.cos((int) (Math.random() * ((90 - 1)) + 1));
                    yShards[i] += 10 * Math.sin((int) (Math.random() * ((90 - 1)) + 1));
                }
                for (int i = 1; i < numShards; i += 2) {
                    xShards[i] -= 10 * Math.cos((int) (Math.random() * ((90 - 1)) + 1));
                    yShards[i] -= 10 * Math.sin((int) (Math.random() * ((90 - 1)) + 1));
                }
                explosionfade -= 5;
            } else {
                repeat = true;
                explosionfade = 255;
                boom = false;
                yboss = getHeight2();
            }
        }

        //GAME OVER
        if (lives <= 0 || score < 0) {
            GameOver = true;
            movey = 0;
            movex = 0;
        }

        //get more lives ---- DO NOT CHANGE ------
        if (score >= bonus && score <= bonus + 4 && score != 0) {
            bonus += bonus;
            lives++;
            lifeup = true;
        }

        if (Float > 100) {
            lifeup = false;
            fade = 255;
            Float = 50;
        }

        if (lifeup) {
            if (fade > 0) {
                fade *= 0.9;
                Float += 2;
            }
        }

        //----------- DO NOT CHANGE -------------
        //stars
        for (int i = 0; i < numStars; i++) {
            yStars[i] -= 1 + level;
        }

        //invader score values
        // DONE WITH ANIMATION ==============================================
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////

    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////

    public int getX(int x) {
        return (x + XBORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }

    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
}
