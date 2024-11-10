import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel{

    
    public static UI game = new UI();


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame("Tetris");
            f.setSize(800, 600);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            //f.setUndecorated(true);
            f.setVisible(true);
            f.add(game);
        });
    }
}