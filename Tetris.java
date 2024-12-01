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
                f.setSize(1720, 1000);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
                f.add(game);
                frame = f;
        });
    }
    
    public static void fullScreen() {
        if (fullScreen) {
            frame.dispose();
            frame.setLocation(80,40);
            frame.setSize(1720, 1000);
            frame.setUndecorated(false);
        } else {
            frame.dispose();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            frame.setUndecorated(true);
        }
        frame.setVisible(true);
        fullScreen = !fullScreen;
    }
}