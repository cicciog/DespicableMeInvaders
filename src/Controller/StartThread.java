package Controller;

import GUI.PanelStart;
import java.awt.Rectangle;

/**
 *  Thread animazione pannello start
 * @author Cicciog
 */
public class StartThread implements Runnable {

    private int sleep = 500;
    private Thread thread;
    //Pannello a cui è legato il thread
    private PanelStart panelStart;
    //Rettangoli per il movimento
    private Rectangle title;
    private Rectangle play;
    private Rectangle exit;

    /**
     * Questa classe è utilizzata per creare il movimento nel pannello start del
     * videogioco
     *
     * @param pStart Pannello start collegato al thread
     * @param pTitle Titolo
     * @param pPlay Pulsante Play
     * @param pExit Pulsante Exit
     */
    public StartThread(PanelStart pStart, Rectangle pTitle, Rectangle pPlay, Rectangle pExit) {
        this.panelStart = pStart;
        this.title = pTitle;
        this.play = pPlay;
        this.exit = pExit;
    }

    /**
     * Overrides del metodo run del thread
     */
    public void run() {
        while (true) {
            try {
                panelStart.smallAnimationBtn(play);
                panelStart.smallAnimationBtn(exit);
                panelStart.smallAnimationTitle(title);
                panelStart.repaint();
                Thread.sleep(sleep);

                panelStart.bigAnimationBtn(play);
                panelStart.bigAnimationBtn(exit);
                panelStart.bigAnimationTitle(title);
                panelStart.repaint();
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
        
        if( thread == null){
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
