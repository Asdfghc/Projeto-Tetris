import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
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

            HashSet<Integer> pressedKeys = new HashSet<>();
            f.addKeyListener(new KeyListener() {
                
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
                    while (true) {
                        if (!UI.isPaused()) {
                            try {
                                game.movePieceDownGravity();
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();   
        });
    }
}