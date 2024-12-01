import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel{

    public static UI game = new UI();
    private static JFrame frame;
    private static boolean fullScreen = false;
        public static void main(String[] args) {
            JFrame f = new JFrame("Tetris");
            EventQueue.invokeLater(() -> {
                f.setLocation(80,40);
                f.setSize(1300, 800);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
                f.add(game);
                frame = f;
        });
    }
    
    public static void fullScreen() {
        frame.dispose();
        if (fullScreen) {
            frame.setSize(1300, 800);
            frame.setLocation(80,40);
            frame.setUndecorated(false);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            frame.setUndecorated(true);
        }
        frame.setVisible(true);
        fullScreen = !fullScreen;
    }
}