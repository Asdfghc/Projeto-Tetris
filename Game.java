import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener{

    public static final int FRAME_LENGTH = 17;
    private static final int[] gravityLevels = {48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1};
    private static int gravity = gravityLevels[0];
    private static int das = 0;
    private static int time = 0;
    private Board board;
    private static boolean paused = false;
    private static boolean forcePaused = false;
    private static final AudioPlayer musicPlayer = new AudioPlayer();
    private static final AudioPlayer soundPlayer = new AudioPlayer();
    private final Thread thread;

    public Game() {
        restart();
        
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        thread = new Thread() {
            @Override public void run() {
                while (true) {
                    if (!isPaused()) {
                        time ++;
                        if (time >= gravity) {
                            movePieceDown();
                            time = 0;
                        }

                        if (das == 16 || das == 0) {
                            if (pressedKeys.contains(KeyEvent.VK_A)) {
                                movePieceLeft();
                                if (das == 16) das = 10;
                            }
                            if (pressedKeys.contains(KeyEvent.VK_D)) {
                                movePieceRight();
                                if (das == 16) das = 10;
                            }
                        }
                        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_D)) das ++;
                    }
                    try {
                        Thread.sleep(FRAME_LENGTH);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        thread.start();  
    }

    HashSet<Integer> pressedKeys = new HashSet<>();
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode())) {
            pressedKeys.add(e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) das = 0;
            if (e.getKeyCode() == KeyEvent.VK_SPACE) gravity = 2;
            if (e.getKeyCode() == KeyEvent.VK_J) this.rotatePieceLeft();
            if (e.getKeyCode() == KeyEvent.VK_L) this.rotatePieceRight();
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SPACE) gravity = gravityLevels[board.getLevel()];
    }
    
    @Override
    public synchronized void keyTyped(KeyEvent e) {
        switch (Character.toLowerCase(e.getKeyChar())) {
            case 'q' -> this.endGame();
            case 'r' -> this.restart();
            case 'e' -> this.pause();
        }
    }
    

    @Override 
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        //Menu UI
            //Main Title
            g.setColor(Color.WHITE);
            g.setFont(new Font("Clarendo", Font.BOLD, 80));
            g.drawString("TETRIS", getWindowWidth()/2 , (int) ((Board.BOARD_HEIGHT - 6.6)*getSquareSize()));
        
        int boardOriginX = getWindowWidth()/2 - Board.BOARD_WIDTH/2*getSquareSize();
        int boardOriginY = getWindowHeight()/20;
        int boardWidth = getSquareSize()*Board.BOARD_WIDTH;
        int boardHeight = getSquareSize()*Board.BOARD_HEIGHT;
            
        // All Panel Color
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWindowWidth(), getWindowHeight());
        
        //Board UI
            int BoardBorderSize = (int) (0.25*getSquareSize());
            // Board Border Color
            g.setColor(Color.CYAN);
            g.fillRect(boardOriginX - BoardBorderSize, boardOriginY - BoardBorderSize, boardWidth + 2*BoardBorderSize, boardHeight + 2*BoardBorderSize);

            // Board color
            g.setColor(Color.BLACK);
            g.fillRect(boardOriginX, boardOriginY, boardWidth, boardHeight);

        // Score UI
            //Box UI //TODO: consertar
            g.setColor(Color.CYAN);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/17 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));

            g.setColor(Color.black);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/14 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));

            // Text UI
            g.setColor(Color.WHITE);
            g.setFont(new Font("Clarendo", Font.BOLD, 20));
            g.drawString("Score: " + board.getScore(), getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.10)*getSquareSize()));

            
        // Level UI
            //Box UI //TODO: consertar
            g.setColor(Color.CYAN);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/2 -  (int) (0.25*getSquareSize()) + 3* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));
            
            g.setColor(Color.black);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/2 - (int) (0.25*getSquareSize()) + 4* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));
            
        // PIECES

        int outline = 2;
        int highlightSize = 8;
        // Next Piece UI
            //Border UI
            int nextPieceBoxOriginX = getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize());
            int nextPieceBoxOriginY = getWindowHeight()/4 - (int) (0.25*getSquareSize());
            int nextPieceBoxWidth = (int) 6*getSquareSize();
            int nextPieceBoxHeight = (int) 4*getSquareSize();
            int nextPieceBoxBorderSize = (int) (0.25*getSquareSize());

            g.setColor(Color.CYAN);
            g.fillRect(nextPieceBoxOriginX - nextPieceBoxBorderSize, nextPieceBoxOriginY - nextPieceBoxBorderSize, nextPieceBoxWidth + 2*nextPieceBoxBorderSize, nextPieceBoxHeight + 2*nextPieceBoxBorderSize);

            g.setColor(Color.black);
            g.fillRect(nextPieceBoxOriginX, nextPieceBoxOriginY, nextPieceBoxWidth, nextPieceBoxHeight);

            // Piece UI //TODO: Consertar
            Square nextPieceSquare = new Square();
            nextPieceSquare.setColorType(board.getNextPiece().getColorType());
            for(int[] square : board.getNextPiece().getShape(0)){
                g.setColor(nextPieceSquare.getMainColor(board.getLevel()));
                g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()) + outline, (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()) + outline, getSquareSize() -2*outline, getSquareSize() -2*outline);
                g.setColor(nextPieceSquare.getHighlightColor(board.getLevel()));
                g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()) + outline, (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()) + outline, getSquareSize()/highlightSize, getSquareSize()/highlightSize);
                if (!nextPieceSquare.isVariant()) {
                    g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()) + getSquareSize()/highlightSize + outline, (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()) + getSquareSize()/highlightSize + outline, getSquareSize()/highlightSize * 2, getSquareSize()/highlightSize);
                    g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()) + getSquareSize()/highlightSize + outline, (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()) + getSquareSize()/highlightSize + outline, getSquareSize()/highlightSize, getSquareSize()/highlightSize * 2);
                } else {
                    g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()) + getSquareSize()/highlightSize + outline, (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()) + getSquareSize()/highlightSize + outline, getSquareSize()*2/3, getSquareSize()*2/3);
                }
            }

            // Text UI
            g.setColor(Color.WHITE);
            g.setFont(new Font("Clarendo", Font.BOLD, 20));
            g.drawString("Level: " + board.getLevel(), getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 6.6)*getSquareSize()));
        
        // Board UI
        for (int i = 0; i < Board.BOARD_WIDTH; i++) {
            for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
                g.setColor(board.getSquare(i, j).getMainColor(board.getLevel()));
                g.fillRect(boardOriginX + outline + i*getSquareSize(), boardOriginY + outline + getSquareSize()*j, getSquareSize() - 2*outline, getSquareSize() - 2*outline);
                g.setColor(board.getSquare(i, j).getHighlightColor(board.getLevel()));
                g.fillRect(boardOriginX + outline + i*getSquareSize(), boardOriginY + outline + getSquareSize()*j, getSquareSize()/highlightSize, getSquareSize()/highlightSize);
                if (!board.getSquare(i, j).isVariant()) {
                    g.fillRect(boardOriginX + outline + i*getSquareSize() + getSquareSize()/highlightSize, boardOriginY + outline + getSquareSize()*j + getSquareSize()/highlightSize, getSquareSize()/highlightSize * 2, getSquareSize()/highlightSize);
                    g.fillRect(boardOriginX + outline + i*getSquareSize() + getSquareSize()/highlightSize, boardOriginY + outline + getSquareSize()*j + getSquareSize()/highlightSize, getSquareSize()/highlightSize, getSquareSize()/highlightSize * 2);
                } else {
                    g.fillRect(boardOriginX + outline + i*getSquareSize() + getSquareSize()/highlightSize, boardOriginY + outline + getSquareSize()*j + getSquareSize()/highlightSize, getSquareSize()*2/3, getSquareSize()*2/3);
                }
            }
        }
    }

    
    public static void playMusic(String path) {
        musicPlayer.playMusic(path);
    }

    public static void stopMusic() {
        musicPlayer.stopMusic();
    }

    /*
    public static void pauseMusic() {
        musicPlayer.pause();
    }

    public static void resumeMusic() {
        musicPlayer.resume();
    }
    */

    public static void playSound(String path) {
        soundPlayer.playSound(path);
    }

    public static boolean getSoundIsRunning() {
        return soundPlayer.getSoundIsRunning();
    }

    public void restart() {
        board = new Board();
        forceUnpause();
        if(paused) pause();
        board.newPiece();
    }

    public void endGame() {
        stopMusic();
        thread.interrupt();
        Tetris.game.menuScreen();
    }

    public static boolean isPaused() {
        return paused || forcePaused;
    }

    public void pause() {
        paused = !paused;
        if (paused) {
            musicPlayer.pause();
        } else {
            musicPlayer.resume();
        }
    }

    public static void forcePause() {
        forcePaused = true;
    }

    public static void forceUnpause() {
        forcePaused = false;
    }

    public int getWindowHeight() {
        return (int) (getSize().getHeight());
    }

    public int getWindowWidth() {
        return (int) (getSize().getWidth());
    }

    public int getSquareSize() {
        return getWindowHeight()*9/10/Board.BOARD_HEIGHT;
    }

    public void rotatePieceRight() {
        if (!isPaused()) {
            board.tryMove(0, 0, 1);
            repaint();
            if(!getSoundIsRunning()) {
                playSound("src\\RotatePieceSound.wav");
            }
        }
    }

    public void rotatePieceLeft() {
        if (!isPaused()) {
            board.tryMove(0, 0, 3);
            repaint();
            if(!getSoundIsRunning()) {
                playSound("src\\RotatePieceSound.wav");
            }
        }
    }

    public void movePieceDown() {
        if (!isPaused()) {
            board.movePieceDown();
            repaint();
        }
    }

    public void movePieceLeft() {
        if (!isPaused()) {
            board.tryMove(-1, 0, 0);
            repaint();
            if(!getSoundIsRunning()) {
                playSound("src\\MovingPieceSound.wav");
            }
        }
    }

    public void movePieceRight() {
        if (!paused && !forcePaused) {
            board.tryMove(1, 0, 0);
            repaint();
            if(!getSoundIsRunning()) {
                playSound("src\\MovingPieceSound.wav");
            }
        }
    }

    public static void repaintUI() {
        Tetris.game.repaint();
    }
}
