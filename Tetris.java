import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel{
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame("Tetris");
            f.setSize(800, 600);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            //f.setUndecorated(true);
            f.setVisible(true);
            
            var game = new UI();
            f.add(game);

            f.addKeyListener(new KeyListener() {
                HashSet<Integer> pressedKeys = new HashSet<>();
                
                @Override
                public synchronized void keyPressed(KeyEvent e) {
                    pressedKeys.add(e.getKeyCode());
                    if (!pressedKeys.isEmpty()) {
                        for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                            switch (it.next()) {
                                case KeyEvent.VK_UP -> game.rotatePieceRight();
                                case KeyEvent.VK_DOWN -> game.rotatePieceLeft();
                                case KeyEvent.VK_SPACE -> game.movePieceDown();
                                case KeyEvent.VK_LEFT -> game.movePieceLeft();
                                case KeyEvent.VK_RIGHT -> game.movePieceRight();
                            }
                        }
                    }
                }
                
                @Override
                public synchronized void keyReleased(KeyEvent e) {
                    pressedKeys.remove(e.getKeyCode());
                }
                
                @Override
                public synchronized void keyTyped(KeyEvent e) {
                    switch (Character.toLowerCase(e.getKeyChar())) {
                        case 'q' -> System.exit(0);
                        case 'r' -> game.restart();
                        case 'e' -> game.pause();
                    }
                }
            });


            new Thread() {
                @Override public void run() {
                    while (game.isRunning()) {
                        try {
                            Thread.sleep(500);
                            game.movePieceDown();
                        } catch (InterruptedException e) {}
                    }
                }
            }.start();   
        });
    }
}