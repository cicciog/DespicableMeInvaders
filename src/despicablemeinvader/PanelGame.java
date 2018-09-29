package despicablemeinvader;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javafx.scene.layout.BackgroundImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *  Pannello di gioco
 * @author Francesco  Gualtieri 149820
 */
public class PanelGame extends JPanel {
//Dichiarzione delle variabili
    //Rendo il cursore invisibile

    private static Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

    //Creo variabili dimension utilizzando la logica del dividere lo schermo in 16:9
    public static Dimension dimFrame = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
    public static Dimension dimMinion = new Dimension(dimFrame.width / 16 * 2, dimFrame.height / 6 * 2);
    public static int marginLeftOrRight = dimFrame.width / 32;
    public static int minionPlace = dimFrame.width / 16 * 5;
    public static int minionYmin = 0 - dimMinion.height;
    public static int minionYmax = 0 - dimMinion.height - dimFrame.height;
    public static Dimension dimScope = new Dimension(dimFrame.height / 18, dimFrame.height / 18);

    //Immagini e rettangoli per contenerle
    private Image Background;
    private Image minionImage;
    private Image scope;

    public Sound push;

    private Rectangle BackgroundRect;
    Rectangle[] minion = new Rectangle[3];
    private Rectangle scopeRect;

    //Variabili utilizzate per il movimento
    private static final int DOWN = 1;

    private int[] yDirection = new int[3];
    //Imposto il punteggio iniziale a 0
    public int score = 0;

    //Velocità del minion
    private int speed;

    //Thread di animazione
    MinionThread thread1;
    MinionThread thread2;
    MinionThread thread3;
    //Utilizzo la classe MyFrame per gestire tutti i pannelli
    public MyFrame mf;

    public PanelGame(MyFrame aThis) {
        this.mf = aThis;

        //Nascondo il cursore
        this.setCursor(HIDDEN_CURSOR);
        //Imposto la dimensione dei rettangoli che contengono le immagini
        this.BackgroundRect = new Rectangle(dimFrame);
        this.minion[0] = new Rectangle(randomMinMax(marginLeftOrRight, marginLeftOrRight + minionPlace - dimMinion.width), randomMinMax(minionYmin, minionYmax), dimMinion.width, dimMinion.height);
        this.minion[1] = new Rectangle(randomMinMax(marginLeftOrRight + minionPlace, marginLeftOrRight + minionPlace * 2 - dimMinion.width), randomMinMax(minionYmin, minionYmax), dimMinion.width, dimMinion.height);
        this.minion[2] = new Rectangle(randomMinMax(marginLeftOrRight + minionPlace * 2, marginLeftOrRight + minionPlace * 3 - dimMinion.width), randomMinMax(minionYmin, minionYmax), dimMinion.width, dimMinion.height);
        this.scopeRect = new Rectangle();
        this.scopeRect.setSize(dimScope);
        this.scopeRect.setLocation(-100, -100);
        //Imposto la direzione iniziale
        this.yDirection[0] = DOWN;
        this.yDirection[1] = DOWN;
        this.yDirection[2] = DOWN;

        //Inizializzo i thread
        this.thread1 = new MinionThread(this, this.minion[0], this.yDirection[0]);
        this.thread2 = new MinionThread(this, this.minion[1], this.yDirection[1]);
        this.thread3 = new MinionThread(this, this.minion[2], this.yDirection[2]);

        //Carico le immagini del pannello
        Background = Resources.getImage("/Images/space.jpg");
        minionImage = Resources.getImage("/Images/minion2.gif");
        scope = Resources.getImage("/Images/scope.png");
        //Suono
        push = Resources.getSound("/Sounds/minion_push.wav");

        this.addMouseMotionListener(new MouseMotionAdapter() {

            /**
             * Questo metodo viene chiamato ogni qual volta viene spostato il
             * cursore all'interno del pannello
             */
            @Override
            public void mouseMoved(MouseEvent e) {

                //Imposto il mirino sul cursore e chiamo il metodo repaint()
                scopeRect.x = e.getPoint().x - scopeRect.width / 2;
                scopeRect.y = e.getPoint().y - scopeRect.height / 2;
                repaint();

            }
        });

        this.addMouseListener(new MouseAdapter() {

            /**
             * Questo metodo viene chiamato ogni qual volta viene effettuato un
             * click all'interno del pannello
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                //Ottengo la posizione del mouse
                Point[] hitPoint = new Point[3];
                hitPoint[0] = e.getPoint();
                hitPoint[1] = e.getPoint();
                hitPoint[2] = e.getPoint();

                //Ottengo le coordinate del click rispetto al minion
                hitPoint[0].x -= minion[0].x;
                hitPoint[0].y -= minion[0].y;
                hitPoint[1].x -= minion[1].x;
                hitPoint[1].y -= minion[1].y;
                hitPoint[2].x -= minion[2].x;
                hitPoint[2].y -= minion[2].y;

                //Se il click è interno al minion
                this.contains(minion[0], hitPoint[0].x, hitPoint[0].y, thread1, 0);
                this.contains(minion[1], hitPoint[1].x, hitPoint[1].y, thread2, 1);
                this.contains(minion[2], hitPoint[2].x, hitPoint[2].y, thread3, 2);

                repaint();

            }

            /**
             * Verifica che il punto di coordinate ("x", "y") sia interno alla
             * parte opaca del minion
             *
             * @param r Rettangolo del minion
             * @param x HitPoint
             * @param y HitPoint
             * @param th Thread che anima il minion
             * @param ID Id del Minion
             */
            public void contains(Rectangle r, int x, int y, MinionThread th, int ID) {
                th.stop();

                if (x < 0 || y < 0 || x >= r.width || y >= r.height) {
                    repaint();
                    th.start();
                } else {
                    //Suono
                    push.play();
                    //Imposto la posizione iniziale del minion colpito
                    r.y = randomMinMax(minionYmin, minionYmax);
                    r.x = randomMinMax(marginLeftOrRight + minionPlace * ID, marginLeftOrRight + minionPlace * (ID + 1) - dimMinion.width);
                    //incremento il punteggio
                    score++;
                    //imposto la velocità
                    speed = 1 + (int) score / 9;
                    repaint();
                    th.start();

                }

            }

        });

    }

    /**
     * Metodo utilizzato per gestire il movimento
     *
     * @param r rettangolo del Minion
     * @param direction Direzione del movimento
     */
    void moveMinion(Rectangle r, int direction) {

        //Sposto il minion di uno step
        r.y += direction * this.speed;

        //Gestisco le collisioni con la parete sinistra e quella destra
        if (r.y <= 0) {
            direction = DOWN;
        }
    }

    /**
     * Overrides del metodo setVisible. Quando si rende visibile il pannello
     * faccio partire i thread, al contrario quando invisibile fermo i thread
     */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            this.speed = 1;
            this.thread1.start();
            this.thread2.start();
            this.thread3.start();

        } else {
            this.thread1.stop();
            this.thread2.stop();
            this.thread3.stop();

        }

    }

    /**
     * Faccio l'overrides del metodo paintComponent per visualizzare a video
     * tutte le immagini
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Background, this.BackgroundRect.x, this.BackgroundRect.y, this.BackgroundRect.width, this.BackgroundRect.height, null);
        g.drawImage(minionImage, this.minion[0].x, this.minion[0].y, this.minion[0].width, this.minion[0].height, null);
        g.drawImage(minionImage, this.minion[1].x, this.minion[1].y, this.minion[1].width, this.minion[1].height, null);
        g.drawImage(minionImage, this.minion[2].x, this.minion[2].y, this.minion[2].width, this.minion[2].height, null);
        g.drawImage(scope, this.scopeRect.x, this.scopeRect.y, this.scopeRect.width, this.scopeRect.height, null);

        String str = "score ";
        str = str + score;
        g.setColor(Color.white);
        //Scelgo il font
        g.setFont(new Font("Trebuchet MS", Font.ITALIC, 40));
        //Scrivo il punteggio a video
        g.drawString(str, dimFrame.width / 16, dimFrame.height - dimFrame.height / 9);
    }

    /**
     * Genera un intero compreso tra min e max
     *
     * @param min Grandezza minima
     * @param max Grandezza massima
     * @return
     */
    int randomMinMax(int min, int max) {
        return ((int) (min + Math.random() * (max - min)));
    }

}
