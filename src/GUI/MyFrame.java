package GUI;

import despicablemeinvader.Resources;
import despicablemeinvader.Sound;
import java.awt.Container;
import java.awt.Toolkit;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *  Frame contenitore di pannelli
 * @author Cicciog
 */
public class MyFrame extends JFrame {
    

    
    public PanelStart start = new PanelStart(this);
    public PanelGame game = new PanelGame(this);
    public PanelWin win = new PanelWin(this);
    public PanelGameOver gameover = new PanelGameOver(this);
    public Container c;
    
    public Sound soundtrack;
   
    public MyFrame() {
        
        super("DespicableMeInvader");
        
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        this.setVisible(true);
        this.soundtrack = Resources.getSound("/Sounds/music.wav");
        c = getContentPane();
        this.soundtrack.play(100);
        this.initPanel(this.start, true);
       
    
        this.initPanel(this.game, false);
        this.initPanel(this.win, false);
        this.initPanel(this.gameover, false);
        
       
    
    }
   
    public void initPanel(JPanel panel, boolean b){
        panel.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setVisible(b);
        this.add(panel);
    }
    
    
}
