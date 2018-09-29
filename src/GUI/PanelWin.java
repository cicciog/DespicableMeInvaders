package GUI;

import GUI.MyFrame;
import despicablemeinvader.Resources;
import despicablemeinvader.Sound;
import despicablemeinvader.WinThread;
import static GUI.PanelGameOver.dimFrame;
import static GUI.PanelStart.dimFrame;
import static GUI.PanelStart.dimScope;
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
 *  Pannello vittoria
 * @author Francesco  Gualtieri 149820
 */
public class PanelWin extends JPanel {
//Dichiarzione delle variabili

    //Creo il cursore del mouse per questo pannello
    private static Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

    //Immagini e rettangoli per contenerle
    private Image Background;
    private Image Happy;
    private Image youWin;
    private Image Replay;
    private Image exit;
    private Image scope;

    private Rectangle HappyRect;
    private Rectangle BackgoundRect;
    private Rectangle youWinRect;
    private Rectangle replayRect;
    private Rectangle exitRect;
    private Rectangle scopeRect;

    //Suoni
    public Sound win;
    public Sound fart;

    //Thread utilizzato per l'animazione del pannello
    private WinThread thread;
    //Utilizzo la classe MyFrame per gestire tutti i pannelli
    public MyFrame mf;
    //Creo variabili dimension utilizzando la logica del dividere lo schermo in 16:9
    public static Dimension dimFrame = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
    public Dimension dimBtn = new Dimension(dimFrame.height / 6, dimFrame.height / 6);
    public Dimension dimYouWin = new Dimension(dimFrame.width / 16 * 5, dimFrame.height / 3);
    public static Dimension dimScope = new Dimension(dimFrame.height / 18, dimFrame.height / 18);

    /**
     * Questa classe è utilizzata per creare il pannello di gioco del videogioco
     * compresa dei minion e del punteggio. Passo al costruttore una variabile
     * MyFrame per richiamare il frame contenitore dei pannelli.
     *
     * @param aThis Frame contenitore del pannello.
     */
    public PanelWin(MyFrame aThis) {
        this.mf = aThis;

        //Nascondo il cursore
        this.setCursor(HIDDEN_CURSOR);
        //Imposto la dimensione dei rettangoli che contengono le immagini
        this.BackgoundRect = new Rectangle(dimFrame);
        this.HappyRect = new Rectangle(dimFrame);
        this.youWinRect = new Rectangle((dimFrame.width - dimYouWin.width) / 2, (dimFrame.height - dimYouWin.height) / 2, dimYouWin.width, dimYouWin.height);
        this.replayRect = new Rectangle(dimFrame.width - dimBtn.width - dimFrame.width / 16 * 2, dimFrame.height - dimBtn.height - dimFrame.height / 9, dimBtn.width, dimBtn.height);
        this.exitRect = new Rectangle(0 + dimFrame.width / 16 * 2, dimFrame.height - dimBtn.height - dimFrame.height / 9, dimBtn.width, dimBtn.height);
        this.scopeRect = new Rectangle();
        this.scopeRect.setSize(dimScope);
        this.scopeRect.setLocation(-100, -100);

        //carico le immagini del pannello
        Background = Resources.getImage("/Images/panelwin.jpg");
        youWin = Resources.getImage("/Images/you_win.png");
        Happy = Resources.getImage("/Images/fuochi.gif");
        Replay = Resources.getImage("/Images/replay.png");
        exit = Resources.getImage("/Images/exitbtn.png");
        scope = Resources.getImage("/Images/hand.png");

        //Suoni
        win = Resources.getSound("/Sounds/win.wav");
        fart = Resources.getSound("/Sounds/fart.wav");
        //Inizializzo il thread
        this.thread = new WinThread(this, this.youWinRect, replayRect, exitRect, HappyRect);
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
                hitPoint[0].x -= replayRect.x;
                hitPoint[0].y -= replayRect.y;
                hitPoint[1].x -= exitRect.x;
                hitPoint[1].y -= exitRect.y;

                //Se il click è interno ai pulsanti
                this.containsReplay(replayRect, hitPoint[0].x, hitPoint[0].y);
                this.containsExit(exitRect, hitPoint[1].x, hitPoint[1].y);

                repaint();

            }

            /**
             * Verifica che il punto di coordinate ("x", "y") sia interno alla
             * parte opaca dell'immagine replay
             *
             * @param r Rettangolo Bottone Replay
             * @param x HitPoint x
             * @param y HitPoint y
             */
            public void containsReplay(Rectangle r, int x, int y) {

                if (x < 0 || y < 0 || x >= r.width || y >= r.height) {

                } else {
                    mf.game.minion[0].y = mf.game.randomMinMax(mf.game.minionYmin, mf.game.minionYmax);
                    mf.game.minion[1].y = mf.game.randomMinMax(mf.game.minionYmin, mf.game.minionYmax);
                    mf.game.minion[2].y = mf.game.randomMinMax(mf.game.minionYmin, mf.game.minionYmax);
                    mf.game.score = 0;
                    //Suono
                    fart.play();
                    mf.initPanel(mf.start, true);
                    mf.initPanel(mf.win, false);

                }
            }

            /**
             * Verifica che il punto di coordinate ("x", "y") sia interno alla
             * parte opaca dell'immagine exit
             *
             * @param r Rettangolo Bottone Exit
             * @param x HitPoint x
             * @param y Hitpoint y
             */
            public void containsExit(Rectangle r, int x, int y) {

                if (x < 0 || y < 0 || x >= r.width || y >= r.height) {

                } else {
                    //chiudo il gioco
                    System.exit(0);
                }
            }
        });

    }

    /**
     * Faccio l'overrides del metodo paintComponent per visualizzare a video
     * tutte le immagini
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Background, this.BackgoundRect.x, this.BackgoundRect.y, this.BackgoundRect.width, this.BackgoundRect.height, null);
        g.drawImage(Happy, this.HappyRect.x, this.HappyRect.y, this.HappyRect.width, this.HappyRect.height, null);
        g.drawImage(youWin, this.youWinRect.x, this.youWinRect.y, this.youWinRect.width, this.youWinRect.height, null);
        g.drawImage(Replay, this.replayRect.x, this.replayRect.y, this.replayRect.width, this.replayRect.height, null);
        g.drawImage(exit, this.exitRect.x, this.exitRect.y, this.exitRect.width, this.exitRect.height, null);
        g.drawImage(scope, this.scopeRect.x, this.scopeRect.y, this.scopeRect.width, this.scopeRect.height, null);
    }

    /**
     * Con questo metodo imposto a 0 la grandezza della scritta "you win" per
     * l'animazione nel thread
     *
     * @param r Rettangolo YouWin
     */
    public void notVisibleAnimation(Rectangle r) {
        r.width = 0;
        r.height = 0;
    }

    /**
     * Con questo metodo imposto la grandezza standard della scritta "you win"
     * per l'animazione nel thread
     *
     * @param r Rettangolo YouWin
     */
    public void VisibleAnimation(Rectangle r) {
        r.width = dimYouWin.width;
        r.height = dimYouWin.height;
    }

    /**
     * Con questo metodo imposto la grandezza minima dei fuochi d'artificio per
     * l'animazione nel thread
     *
     * @param r Rettangolo Fuochi D'Artificio
     */
    public void smallVisibleAnimationPyro(Rectangle r) {
        r.width = dimFrame.width - dimFrame.width / 16 * 2;
        r.height = dimFrame.height - dimFrame.height / 9 * 2;
    }

    /**
     * Con questo metodo imposto la grandezza massima dei fuochi d'artificio per
     * l'animazione nel thread
     *
     * @param r Rettangolo Fuochi D'Artificio
     */
    public void bigVisibleAnimationPyro(Rectangle r) {
        r.width = dimFrame.width;
        r.height = dimFrame.height;
    }

    /**
     * Con questo metodo imposto la grandezza minima del bottone per
     * l'animazione nel thread
     *
     * @param r Rettangolo Bottone
     */
    public void smallAnimationBtn(Rectangle r) {
        r.width = dimBtn.width;
        r.height = dimBtn.height;
    }

    /**
     * Con questo metodo imposto la grandezza massima del bottone per
     * l'animazione nel thread
     *
     * @param r Rettangolo Bottone
     */
    public void bigAnimationBtn(Rectangle r) {
        r.width = dimBtn.width + dimFrame.height / 64;
        r.height = dimBtn.height + dimFrame.height / 36;
    }

    /**
     * Overrides del metodo setVisible. Quando si rende visibile il pannello
     * faccio partire il suono win.
     */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            //Suono
            win.play();
        }
    }

}
