package despicablemeinvader;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *  Pannello start
 * @author Cicciog
 */
public class PanelStart extends JPanel {
   //Dichiarzione delle variabili

    //Creo il cursore del mouse per questo pannello
    private static Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

    //Immagini e rettangoli per contenerle
    private Image Background;
    private Image play;
    private Image exit;
    private Image scope;
    private Image title;

    private Rectangle BackgoundRect;
    private Rectangle playRect;
    private Rectangle exitRect;
    private Rectangle scopeRect;
    private Rectangle titleRect;

    //Suono
    public Sound playSound;

    //Creo variabili dimension utilizzando la logica del dividere lo schermo in 16:9
    public static Dimension dimFrame = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
    public Dimension dimBtn = new Dimension(dimFrame.height / 6, dimFrame.height / 6);
    public static Dimension dimScope = new Dimension(dimFrame.height / 18, dimFrame.height / 18);
    public static Dimension dimTitle = new Dimension(dimFrame.width / 32 * 9, dimFrame.height / 3);

    //Thread utilizzato per l'animazione del pannello
    private StartThread thread;
    //Utilizzo la classe MyFrame per gestire tutti i pannelli 
    private MyFrame mf;

    /**
     * Questa classe è utilizzata per creare il pannello start del videogioco
     * compresa dei rispettivi pulsanti. Passo al costruttore una variabile
     * MyFrame per richiamare il frame contenitore dei pannelli.
     *
     * @param aThis Frame contenitore del pannello.
     */
    public PanelStart(MyFrame aThis) {
        this.mf = aThis;

        //Nascondo il cursore
        this.setCursor(HIDDEN_CURSOR);

        //Imposto la dimensione dei rettangoli che contengono le immagini
        this.BackgoundRect = new Rectangle(dimFrame.width, dimFrame.height);
        this.playRect = new Rectangle(dimBtn);
        this.exitRect = new Rectangle(dimBtn);
        this.scopeRect = new Rectangle(-100, -100, dimScope.width, dimScope.height);
        this.titleRect = new Rectangle(dimFrame.width - dimTitle.width - dimFrame.width / 32, dimFrame.height / 18, dimTitle.width, dimTitle.height);

        //Carico le immagini del pannello utilizzando la classe Resources
        Background = Resources.getImage("/Images/start_background.jpg");
        play = Resources.getImage("/Images/play.gif");
        exit = Resources.getImage("/Images/exitbtn.png");
        scope = Resources.getImage("/Images/hand.png");
        title = Resources.getImage("/Images/despicableme.gif");

        //Suono
        playSound = Resources.getSound("/Sounds/hahaha.wav");

        //Imposto la posizione dei pulsanti play e exit
        this.playRect.setLocation((this.dimFrame.width - this.playRect.width - this.dimFrame.width / 16 * 2), (this.dimFrame.height - this.playRect.height - this.dimFrame.height / 9));
        this.exitRect.setLocation((0 + this.dimFrame.width / 16 * 2), (this.dimFrame.height - this.dimFrame.height / 9 - this.dimBtn.height));

        //Inizializzo il thread
        thread = new StartThread(this, this.titleRect, this.playRect, this.exitRect);
        thread.start();

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
                Point[] hitPoint = new Point[2];
                hitPoint[0] = e.getPoint();
                hitPoint[1] = e.getPoint();

                //Ottengo le coordinate del click rispetto al minion
                hitPoint[0].x -= playRect.x;
                hitPoint[0].y -= playRect.y;
                hitPoint[1].x -= exitRect.x;
                hitPoint[1].y -= exitRect.y;

                //Se il click è interno ai pulsanti
                this.containsPlay(playRect, hitPoint[0].x, hitPoint[0].y);
                this.containsExit(exitRect, hitPoint[1].x, hitPoint[1].y);

                repaint();

            }

            /**
             * Verifica che il punto di coordinate ("x", "y") sia interno alla
             * parte opaca dell'immagine del pulsante play
             *
             * @param r Rettangolo Bottone Play
             * @param x HitPoint x
             * @param y HitPoint y
             */
            public void containsPlay(Rectangle r, int x, int y) {

                if (x < 0 || y < 0 || x >= r.width || y >= r.height) {

                } else {
                    //Suono
                    playSound.play();
                    //rendo invisibile il pannello start e attivo quello game
                    mf.initPanel(mf.game, true);
                    mf.initPanel(mf.start, false);
                }
                repaint();
            }

            /**
             * Verifica che il punto di coordinate ("x", "y") sia interno alla
             * parte opaca dell'immagine del pulsante exit
             *
             * @param r Rettangolo Bottone Exit
             * @param x HitPoint x
             * @param y HitPoint y
             */
            public void containsExit(Rectangle r, int x, int y) {

                if (x < 0 || y < 0 || x >= r.width || y >= r.height) {

                } else {
                    //chiudo il gioco
                    System.exit(0);
                }
                repaint();
            }
        });

    }

    /**
     * Faccio l'overrides del metodo paintComponent per visualizzare a video
     * tutte le immagini
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Background, this.BackgoundRect.x, this.BackgoundRect.y, dimFrame.width, dimFrame.height, null);
        g.drawImage(play, (this.dimFrame.width - this.playRect.width - this.dimFrame.width / 16 * 2), (this.dimFrame.height - this.playRect.height - this.dimFrame.height / 9), playRect.width, playRect.height, getParent());
        g.drawImage(exit, (0 + this.dimFrame.width / 16 * 2), (this.dimFrame.height - this.dimFrame.height / 9 - this.dimBtn.height), this.exitRect.width, this.exitRect.height, null);
        g.drawImage(title, this.titleRect.x, this.titleRect.y, this.titleRect.width, this.titleRect.height, null);
        g.drawImage(scope, this.scopeRect.x, this.scopeRect.y, this.scopeRect.width, this.scopeRect.height, null);
    }

    /**
     * Con questo metodo imposto la grandezza minima del bottone per
     * l'animazione nel thread
     *
     * @param r Rettangolo bottone
     */
    void smallAnimationBtn(Rectangle r) {
        r.width = dimBtn.width;
        r.height = dimBtn.height;
    }

    /**
     * Con questo metodo imposto la grandezza massima del bottone per
     * l'animazione nel thread
     *
     * @param r Rettangolo bottone
     */
    void bigAnimationBtn(Rectangle r) {
        r.width = dimBtn.width + dimFrame.height / 64;
        r.height = dimBtn.height + dimFrame.height / 36;
    }

    /**
     * Con questo metodo imposto la grandezza minima del titolo per l'animazione
     * nel thread
     *
     * @param r Rettangolo Titolo
     */
    void smallAnimationTitle(Rectangle r) {
        r.width = dimTitle.width - dimFrame.height / 64;
        r.height = dimTitle.height - dimFrame.height / 36;
    }

    /**
     * Con questo metodo imposto la grandezza massima del titolo per
     * l'animazione nel thread
     *
     * @param r Rettangolo Titolo
     */
    void bigAnimationTitle(Rectangle r) {
        r.width = dimTitle.width;
        r.height = dimTitle.height;
    }
}
