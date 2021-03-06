package Controller;

import GUI.PanelGame;
import java.awt.Rectangle;

/**
 *  Thread animazione Minion
 * @author Cicciog
 */
public class MinionThread implements Runnable {

    private int sleep = 10;
    private Thread thread;
    private PanelGame panelgame;
    private Rectangle rectangle;
    private int direction;

    /**
     * Questa classe è utilizzata per creare il movimento nel pannello game del
     * videogioco
     *
     * @param pg Pannello Game collegato al thread
     * @param r Rettangolo contenitore del Minion
     * @param dir Direzione del movimento del Minion
     */
    public MinionThread(PanelGame pg, Rectangle r, int dir) {
        this.panelgame = pg;
        this.rectangle = r;
        this.direction = dir;
    }

    /**
     * Overrides del metodo run del thread
     */
    public void run() {
        try {
            
            while (true) {
                panelgame.moveMinion(rectangle, direction);
                if (panelgame.score == 50) {
                    panelgame.mf.initPanel(panelgame.mf.win, true);
                    panelgame.mf.initPanel(panelgame.mf.game, false);

                }
                
                if (panelgame.minion[0].y >= panelgame.dimFrame.height - panelgame.minion[0].height / 3 * 2 || panelgame.minion[1].y >= panelgame.dimFrame.height - panelgame.minion[1].height / 3 * 2 || panelgame.minion[2].y >= panelgame.dimFrame.height - panelgame.minion[2].height / 3 * 2) {
                    panelgame.thread1.stop();
                    panelgame.thread2.stop();
                    panelgame.thread3.stop();
                    panelgame.mf.initPanel(panelgame.mf.gameover, true);
                    panelgame.mf.initPanel(panelgame.mf.game, false);
                }
                panelgame.repaint();
                Thread.sleep(sleep);
            }
        } catch (InterruptedException ex) {
           System.out.println(ex.getCause());
        }

    }

    /**
     * Avvia il thread
     */
    public void start() {
        thread = new Thread(this);        
        thread.start ();  
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
