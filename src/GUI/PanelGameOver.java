package GUI;

import GUI.MyFrame;
import Controller.GameOverThread;
import Controller.Resources;
import Controller.Sound;
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
 *  Pannello GameOver
 * @author Cicciog
 */
public class PanelGameOver extends JPanel {
//Dichiarzione delle variabili

    //Creo il cursore del mouse per questo pannello
    private static final Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

    //Immagini e rettangoli per contenerle
    private Image Background;
    private Image Replay;
    private Image GameOverTxt;
    private Image exit;
    private Image scope;

    private Rectangle BackgoundRect;
    private Rectangle GameOverTxtRect;
    private Rectangle replayRect;
    private Rectangle exitRect;
    private Rectangle scopeRect;

    //Suoni
    public Sound fail;
    public Sound fart;

    //Thread utilizzato per l'animazione del pannello
    private GameOverThread thread;
    
    //Utilizzo la classe MyFrame per gestire tutti i pannelli
    public MyFrame mf;
    
    //Creo variabili dimension utilizzando la logica del dividere lo schermo in 16:9
    public static final Dimension dimFrame = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
    public static final Dimension dimBtn = new Dimension(dimFrame.height / 6, dimFrame.height / 6);
    public static final Dimension dimGameOver = new Dimension(dimFrame.width / 16 * 5, dimFrame.height / 3);
    public static final Dimension dimScope = new Dimension(dimFrame.height / 18, dimFrame.height / 18);

    /**
     * Questa classe è utilizzata per creare il pannello gameover del videogioco
     * compresa dei rispettivi pulsanti. Passo al costruttore una variabile
     * MyFrame per richiamare il frame contenitore dei pannelli.
     *
     * @param aThis Frame contenitore del pannello.
     */
    public PanelGameOver(MyFrame aThis) {
        this.mf = aThis;

        //Nascondo il cursore
        this.setCursor(HIDDEN_CURSOR);
        
        //Imposto la dimensione dei rettangoli che contengono le immagini
        this.BackgoundRect = new Rectangle(dimFrame);
        this.GameOverTxtRect = new Rectangle((dimFrame.width - dimGameOver.width) / 2, (dimFrame.height - dimGameOver.height) / 2, dimGameOver.width, dimGameOver.height);
        this.replayRect = new Rectangle(dimFrame.width - dimBtn.width - dimFrame.width / 16 * 2, dimFrame.height - dimBtn.height - dimFrame.height / 9, dimBtn.width, dimBtn.height);
        this.exitRect = new Rectangle(0 + dimFrame.width / 16 * 2, dimFrame.height - dimBtn.height - dimFrame.height / 9, dimBtn.width, dimBtn.height);
        this.scopeRect = new Rectangle();
        this.scopeRect.setSize(dimScope);
        this.scopeRect.setLocation(-100, -100);

        //Carico le immagini del pannello
        Background = Resources.getImage("/Images/gameover_background.jpg");
        GameOverTxt = Resources.getImage("/Images/Game_Over.gif");
        Replay = Resources.getImage("/Images/replay.png");
        exit = Resources.getImage("/Images/exitbtn.png");
        scope = Resources.getImage("/Images/hand.png");
        
        //Suoni
        fail = Resources.getSound("/Sounds/fail.wav");
        fart = Resources.getSound("/Sounds/fart.wav");

        //Inizializzo il thread
        thread = new GameOverThread(this, this.GameOverTxtRect, this.replayRect, this.exitRect);
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
             * Verifica che il punto di coordinate ("x", "y") sia interno al
             * pulsante replay
             *
             * @param r Rettangolo del Pulsante Replay
             * @param x HitPoint x
             * @param y HitPoint y
             */
            public void containsReplay(Rectangle r, int x, int y) {

                if (x < 0 || y < 0 || x >= r.width || y >= r.height) {

                } else {
                    //Imposto i minion nella posizione iniziale
                    mf.game.minion[0].y = mf.game.randomMinMax(mf.game.minionYmin, mf.game.minionYmax);
                    mf.game.minion[1].y = mf.game.randomMinMax(mf.game.minionYmin, mf.game.minionYmax);
                    mf.game.minion[2].y = mf.game.randomMinMax(mf.game.minionYmin, mf.game.minionYmax);
                    mf.game.score = 0;

                    //Suono
                    fart.play();

                    //Rendo visibile il pannello start e invisibile il pannello gameover
                    mf.initPanel(mf.start, true);
                    mf.initPanel(mf.gameover, false);

                }
            }

            /**
             * Verifica che il punto di coordinate ("x", "y") sia interno al
             * pulsante exit
             *
             * @param r Rettangolo del Pulsante exit
             * @param x HitPoint x
             * @param y HitPoint y
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
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Background, this.BackgoundRect.x, this.BackgoundRect.y, this.BackgoundRect.width, this.BackgoundRect.height, null);
        g.drawImage(GameOverTxt, this.GameOverTxtRect.x, this.GameOverTxtRect.y, this.GameOverTxtRect.width, this.GameOverTxtRect.height, null);
        g.drawImage(Replay, this.replayRect.x, this.replayRect.y, this.replayRect.width, this.replayRect.height, null);
        g.drawImage(exit, this.exitRect.x, this.exitRect.y, this.exitRect.width, this.exitRect.height, null);
        g.drawImage(scope, this.scopeRect.x, this.scopeRect.y, this.scopeRect.width, this.scopeRect.height, null);
    }

    /**
     * Con questo metodo imposto a 0 la grandezza della scritta "gameover" per
     * l'animazione nel thread
     *
     * @param r Rettangolo scritta Gameover
     */
    public void notVisibleAnimationGameover(Rectangle r) {
        r.width = 0;
        r.height = 0;
    }

    /**
     * Con questo metodo imposto la grandezza standard della scritta "gameover"
     * per l'animazione nel thread
     *
     * @param r Rettangolo scritta Gameover
     */
    public void VisibleAnimationGameover(Rectangle r) {
        r.width = dimGameOver.width;
        r.height = dimGameOver.height;
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
     * faccio partire il suono fail.
     */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            //Suono
            fail.play();
        }
    }
}
