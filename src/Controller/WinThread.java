package Controller;

import GUI.PanelWin;
import java.awt.Rectangle;


/**
 *  Thread animazione pannello vittoria
 * @author Cicciog
 */
public class WinThread implements Runnable {

    private int sleep = 500;
    private Thread thread;
    private PanelWin panelwin;
    private Rectangle rectangle;
    private Rectangle replay;
    private Rectangle exit;
    private Rectangle pyro;

    /**
     * Questa classe è utilizzata per creare il movimento nel pannello win
     * del videogioco
     *
     * @param pw Pannello Win collegato al thread
     * @param r Scritta You Win
     * @param pReplay Pulsante Replay
     * @param pExit Pulsante Exit
     * @param pPyro Fuochi d'artificio
     */
    public WinThread(PanelWin pw, Rectangle r, Rectangle pReplay, Rectangle pExit, Rectangle pPyro) {
        this.panelwin = pw;
        this.rectangle = r;
        this.replay = pReplay;
        this.exit = pExit;
        this.pyro = pPyro;

    }

    /**
     * Overrides del metodo run del thread
     */
    public void run() {

        while (true) {
            try {
                panelwin.notVisibleAnimation(rectangle);
                panelwin.smallAnimationBtn(replay);
                panelwin.smallAnimationBtn(exit);
                panelwin.smallVisibleAnimationPyro(pyro);
                panelwin.repaint();
          
                Thread.sleep(sleep);
            
                panelwin.VisibleAnimation(rectangle);
                panelwin.bigAnimationBtn(replay);
                panelwin.bigAnimationBtn(exit);
                panelwin.bigVisibleAnimationPyro(pyro);
                panelwin.repaint();
           
                Thread.sleep(sleep);
            
                panelwin.smallVisibleAnimationPyro(pyro);
                panelwin.repaint();
            
                Thread.sleep(sleep);
            
                panelwin.bigVisibleAnimationPyro(pyro);
                panelwin.repaint();
            
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                System.out.println(ex.getCause());
            }
        }

    }

    /**
     * Avvia il thread
     */
    public void start() {

        if(thread == null){
            thread = new Thread(this);
            thread.start();
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
