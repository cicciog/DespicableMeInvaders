package despicablemeinvader;

import java.awt.Rectangle;

/**
 *  Thread animazione pannello Gameover
 * @author Francesco  Gualtieri 149820
 */
public class GameOverThread implements Runnable {

    private int sleep = 5;
    private Thread thread;
    //Pannello a cui è legato il thread
    private PanelGameOver panelgameover;
    //Rettangoli per il movimento
    private Rectangle rectangle;
    private Rectangle replay;
    private Rectangle exit;

    /**
     * Questa classe è utilizzata per creare il movimento nel pannello gameover
     * del videogioco
     *
     * @param pgo Pannello Gameover collegato al thread
     * @param r Scritta gameover
     * @param pReplay Pulsante Replay
     * @param pExit Pulsante Exit
     */
    public GameOverThread(PanelGameOver pgo, Rectangle r, Rectangle pReplay, Rectangle pExit) {
        this.panelgameover = pgo;
        this.rectangle = r;
        this.replay = pReplay;
        this.exit = pExit;

    }

    /**
     * Overrides del metodo run del thread
     */
    public void run() {

        while (true) {

            try {
                
                panelgameover.notVisibleAnimationGameover(rectangle);
                panelgameover.smallAnimationBtn(replay);
                panelgameover.smallAnimationBtn(exit);
                panelgameover.repaint();
                Thread.sleep(500);
                panelgameover.VisibleAnimationGameover(rectangle);
                panelgameover.bigAnimationBtn(replay);
                panelgameover.bigAnimationBtn(exit);
                panelgameover.repaint();
                Thread.sleep(500);
                
            } catch (InterruptedException ex) {
                System.out.println(ex.getCause());
            }

        }
    }

    /**
     * Avvia il thread
     */
    public void start() {
        
         if (thread == null) {
            thread  = new Thread (this);
            thread.start ();
         }
    }

    /**
     * Ferma il thread
     */
    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

}