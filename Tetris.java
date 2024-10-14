
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.HashSet;
import java.util.Iterator;

public class Tetris extends JPanel{

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            //Board board = new Board();
            JFrame f = new JFrame("Tetris");
            f.setSize(800, 600);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            //f.setUndecorated(true);
            f.setVisible(true);
            
            var game = new UI();
            //game.start();
            f.add(game);


            f.addKeyListener(new KeyListener() {
                HashSet<Integer> pressedKeys = new HashSet<>();
                
                @Override
                public synchronized void keyPressed(KeyEvent e) {
                    pressedKeys.add(e.getKeyCode());
                    if (!pressedKeys.isEmpty()) {
                        for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                            switch (it.next()) {
                                case KeyEvent.VK_UP:
                                    game.rotatePieceRight();
                                    break;
                                case KeyEvent.VK_DOWN:
                                    game.rotatePieceLeft();
                                    break;
                                case KeyEvent.VK_SPACE:
                                    game.movePieceDown();
                                    break;
                                case KeyEvent.VK_LEFT:
                                    game.movePieceLeft();
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    game.movePieceRight();
                                    break;
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
                    switch (e.getKeyChar()) {
                        case 'q':
                            System.exit(0);
                            break;
                        case 'r':
                            game.restart();
                            break;
                    }
                }
            });

            new Thread() {
                @Override public void run() {
                    while (true) {
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