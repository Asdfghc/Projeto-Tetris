import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import javax.imageio.ImageIO;
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
    private int highScore;

    public Game(int highScore) {
        this.highScore = highScore;
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
        
        int windowHeight = (int) (getSize().getHeight());
        int windowWidth = (int) (getSize().getWidth());
        int squareSize = windowHeight*3/(4*Board.BOARD_HEIGHT);
        int imageHeight = Math.floorDiv((squareSize)*4*Board.BOARD_HEIGHT, 3) + squareSize;
        int imageWidth = imageHeight*8/7;
        int imageOriginX = windowWidth/2 - imageWidth/2;
        //int imageOriginY = windowHeight/2 - imageHeight/2;
        try {
            BufferedImage background = ImageIO.read(new File("src\\TetrisBackground.png"));
            Image scaledBackground = background.getScaledInstance(imageWidth, imageHeight, BufferedImage.SCALE_DEFAULT);
            g.drawImage(scaledBackground, imageOriginX, 0, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int boardOriginX = (int) (imageOriginX + imageWidth*0.372f);
        int boardOriginY = (int) (imageHeight*0.175f);
        g.setColor(Color.WHITE);
        g.drawRect(boardOriginX, boardOriginY, 1, 1);
        //int boardWidth = squareSize*Board.BOARD_WIDTH;
        //int boardHeight = squareSize*Board.BOARD_HEIGHT;
            
        // All Panel Color
        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, windowWidth, windowHeight);
        
        //Board UI
        //int boardBorderSize = (int) (0.25*squareSize);
        // Board Border Color
        //g.setColor(Color.CYAN);
        //g.fillRect(boardOriginX - boardBorderSize, boardOriginY - boardBorderSize, boardWidth + 2*boardBorderSize, boardHeight + 2*boardBorderSize);
        
        // Board color
        //g.setColor(Color.BLACK);
        //g.fillRect(boardOriginX, boardOriginY, boardWidth, boardHeight);
        
    // Lines Box UI
        int linesTextOriginX = boardOriginX + Board.BOARD_WIDTH*squareSize/2 + (int) (2.1*squareSize);
        int linesTextOriginY = boardOriginY - (int) (2*squareSize);

        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*2f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(new DecimalFormat("000").format(board.getLines()), linesTextOriginX, linesTextOriginY);

    // Score Box UI //TODO: Melhorar tamanhos e posições
        //int scoreBoxBorderSize = (int) (0.15*squareSize);
        //int scoreBoxOriginX = boardOriginX + Board.BOARD_WIDTH*squareSize + 2*squareSize;
        //int scoreBoxOriginY = windowHeight/17 - (int) (0.25*squareSize);
        //int scoreBoxWidth = (int) 6*squareSize;
        //int scoreBoxHeight = (int) 3*squareSize;

        //g.setColor(Color.CYAN);
        //g.fillRect(scoreBoxOriginX - scoreBoxBorderSize, scoreBoxOriginY - scoreBoxBorderSize, scoreBoxWidth + 2*boardBorderSize, scoreBoxHeight + 2*boardBorderSize);
        
        //g.setColor(Color.black);
        //g.fillRect(scoreBoxOriginX, scoreBoxOriginY, scoreBoxWidth, scoreBoxHeight);

        // Text
        int highScoreTextOriginX = boardOriginX + Board.BOARD_WIDTH*squareSize + (int) (1.9*squareSize);
        int highScoreTextOriginY = boardOriginY + (int) (0.1*squareSize);

        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*2f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        if (board.getScore() > highScore) {
            highScore = board.getScore();
        }
        g.drawString(new DecimalFormat("000000").format(highScore), highScoreTextOriginX, highScoreTextOriginY);


        int scoreTextOriginX = boardOriginX + Board.BOARD_WIDTH*squareSize + (int) (1.9*squareSize);
        int scoreTextOriginY = boardOriginY + (int) (3.1*squareSize);

        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*2f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(new DecimalFormat("000000").format(board.getScore()), scoreTextOriginX, scoreTextOriginY);
        
    // Next Piece UI
    int outline = 2;
    int highlightSize = 8;
        //Border UI
        //int nextPieceBoxBorderSize = (int) (0.25*squareSize);
        //int nextPieceBoxWidth = (int) 4*squareSize;
        //int nextPieceBoxHeight = (int) 6*squareSize;
        //int nextPieceOriginX = boardOriginX + Board.BOARD_WIDTH*squareSize + 2*squareSize;
        //int nextPieceOriginY = windowHeight/2 - nextPieceBoxHeight/2 - nextPieceBoxBorderSize;

        //g.setColor(Color.CYAN);
        //g.fillRect(nextPieceOriginX - nextPieceBoxBorderSize, nextPieceOriginY - nextPieceBoxBorderSize, nextPieceBoxWidth + 2*nextPieceBoxBorderSize, nextPieceBoxHeight + 2*nextPieceBoxBorderSize);
        
        //g.setColor(Color.black);
        //g.fillRect(nextPieceOriginX, nextPieceOriginY, nextPieceBoxWidth, nextPieceBoxHeight);
            
    // Level UI
        //Level Box UI
        //int levelBoxBorderSize = (int) (0.15*squareSize);
        //int levelBoxOriginX = boardOriginX - levelBoxBorderSize + Board.BOARD_WIDTH*squareSize + 2*squareSize;
        //int levelBoxOriginY = nextPieceOriginY + nextPieceBoxHeight + 2*squareSize;
        //int levelBoxWidth = (int) 6*squareSize;
        //int levelBoxHeight = (int) 4*squareSize;

        //g.setColor(Color.CYAN);
        //g.fillRect(levelBoxOriginX - levelBoxBorderSize, levelBoxOriginY - levelBoxBorderSize, levelBoxWidth + 2*levelBoxBorderSize, levelBoxHeight + 2*levelBoxBorderSize);
        
        //g.setColor(Color.black);
        //g.fillRect(levelBoxOriginX, levelBoxOriginY, levelBoxWidth, levelBoxHeight);
        
        // Text
        
        int levelBoxTextOriginX = boardOriginX + Board.BOARD_WIDTH*squareSize + (int) (1.9*squareSize);
        int levelBoxTextOriginY = boardOriginY + Board.BOARD_HEIGHT*squareSize - (int) (5.1*squareSize);
        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont((float) squareSize*2);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(new DecimalFormat("00").format(board.getLevel()), levelBoxTextOriginX + 2*squareSize, levelBoxTextOriginY + squareSize);
            
            
        // Piece UI
        Piece nextPiece = board.getNextPiece();
        Square nextPieceSquare = new Square();
        nextPieceSquare.setColorType(nextPiece.getColorType());
        int nextPieceCenterx = boardOriginX + Board.BOARD_WIDTH*squareSize + (int) (2.9*squareSize);
        int nextPieceOriginX = nextPieceCenterx;
        switch (nextPiece.getType()) {
            case I -> nextPieceOriginX -= squareSize;
            case O -> nextPieceOriginX -= squareSize;
            case T -> nextPieceOriginX -= squareSize*1/2;
            case J -> nextPieceOriginX -= squareSize*1/2;
            case L -> nextPieceOriginX -= squareSize*1/2;
            case S -> nextPieceOriginX -= squareSize*1/2;
            case Z -> nextPieceOriginX -= squareSize*1/2;
        }
        int nextPieceOriginY = boardOriginY + Board.BOARD_HEIGHT*squareSize - (int) (12*squareSize);
        for(int[] square : board.getNextPiece().getShape(0)){
            g.setColor(nextPieceSquare.getMainColor(board.getLevel()));
            g.fillRect(nextPieceOriginX + (square[0] * squareSize) + outline, nextPieceOriginY + (square[1] * squareSize) + outline, squareSize -2*outline, squareSize -2*outline);
            g.setColor(nextPieceSquare.getHighlightColor(board.getLevel()));
            g.fillRect(nextPieceOriginX + (square[0] * squareSize) + outline,nextPieceOriginY + (square[1] * squareSize) + outline, squareSize/highlightSize, squareSize/highlightSize);
            if (!nextPieceSquare.isVariant()) {
                g.fillRect(nextPieceOriginX + (square[0] * squareSize) + squareSize/highlightSize + outline, nextPieceOriginY + (square[1] * squareSize) + squareSize/highlightSize + outline, squareSize/highlightSize * 2, squareSize/highlightSize);
                g.fillRect(nextPieceOriginX + (square[0] * squareSize) + squareSize/highlightSize + outline, nextPieceOriginY + (square[1] * squareSize) + squareSize/highlightSize + outline, squareSize/highlightSize, squareSize/highlightSize * 2);
            } else {
                g.fillRect(nextPieceOriginX + (square[0] * squareSize) + squareSize/highlightSize + outline, nextPieceOriginY + (square[1] * squareSize) + squareSize/highlightSize + outline, squareSize*2/3, squareSize*2/3);
            }
        }
        
        // Board UI
        for (int i = 0; i < Board.BOARD_WIDTH; i++) {
            for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
                g.setColor(board.getSquare(i, j).getMainColor(board.getLevel()));
                g.fillRect(boardOriginX + outline + i*squareSize, boardOriginY + outline + squareSize*j, squareSize - 2*outline, squareSize - 2*outline);
                g.setColor(board.getSquare(i, j).getHighlightColor(board.getLevel()));
                g.fillRect(boardOriginX + outline + i*squareSize, boardOriginY + outline + squareSize*j, squareSize/highlightSize, squareSize/highlightSize);
                if (!board.getSquare(i, j).isVariant()) {
                    g.fillRect(boardOriginX + outline + i*squareSize + squareSize/highlightSize, boardOriginY + outline + squareSize*j + squareSize/highlightSize, squareSize/highlightSize * 2, squareSize/highlightSize);
                    g.fillRect(boardOriginX + outline + i*squareSize + squareSize/highlightSize, boardOriginY + outline + squareSize*j + squareSize/highlightSize, squareSize/highlightSize, squareSize/highlightSize * 2);
                } else {
                    g.fillRect(boardOriginX + outline + i*squareSize + squareSize/highlightSize, boardOriginY + outline + squareSize*j + squareSize/highlightSize, squareSize*2/3, squareSize*2/3);
                }
            }
        }
    }

    public int getHighScore() {
        return highScore;
    }
    
    public static void playMusic(String path) { //TODO: Musica so no board
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
