import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel{

    private static final int FRAME_LENGTH = 17;
    public static UI game = new UI();
    private static int das = 0;
    private static int time = 0;
    private static int gravity = 160;

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
                    if (!pressedKeys.contains(e.getKeyCode())) {
                        pressedKeys.add(e.getKeyCode());
                        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) das = 0;
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) gravity /= 2;
                    }
                }

                @Override
                public synchronized void keyReleased(KeyEvent e) {
                    pressedKeys.remove(e.getKeyCode());
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) gravity *= 2;
                }
                
                @Override
                public synchronized void keyTyped(KeyEvent e) {
                    switch (Character.toLowerCase(e.getKeyChar())) {
                        case 'q' -> System.exit(0);
                        case 'r' -> game.restart();
                        case 'e' -> game.pause();
                        case 'j' -> game.rotatePieceLeft();
                        case 'l' -> game.rotatePieceRight();
                    }
                }
            });
            
            new Thread() {
                @Override public void run() {
                    while (true) {
                        if (das == 16 || das == 0) {
                            if (pressedKeys.contains(KeyEvent.VK_A)) {
                                game.movePieceLeft();
                                if (das == 16) das = 10;
                            }
                            if (pressedKeys.contains(KeyEvent.VK_D)) {
                                game.movePieceRight();
                                if (das == 16) das = 10;
                            }
                        }
                        if (!pressedKeys.isEmpty()) {
                            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                                switch (it.next()) {
                                    case KeyEvent.VK_A -> das++;
                                    case KeyEvent.VK_D -> das++;
                                }
                            }
                        }
                        try {
                            Thread.sleep(FRAME_LENGTH);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //System.out.println(das);
                    }
                }
            }.start();

            new Thread() {
                @Override public void run() {
                    while (true) {
                        if (!UI.isPaused()) {
                            time += FRAME_LENGTH;
                            if (time >= gravity) {
                                game.movePieceDownGravity();
                                time = 0;
                            }
                        }
                        try {
                            Thread.sleep(FRAME_LENGTH);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();   
        });
    }
}