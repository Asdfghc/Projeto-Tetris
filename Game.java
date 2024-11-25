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
    private int boardWidth = 10;
    private int boardHeight = 20;
    private int startLevel = 0;
    private static boolean paused = false;
    private static boolean forcePaused = false;
    private static final AudioPlayer musicPlayer = new AudioPlayer();
    private static final AudioPlayer soundPlayer = new AudioPlayer();
    private final Thread thread;
    private int highScore;

    public Game(int highScore, int startLevel, int boardWidth, int boardHeight) {
        this.highScore = highScore;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.startLevel = startLevel;
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
                            if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) {
                                movePieceLeft();
                                if (das == 16) das = 10;
                            }
                            if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                                movePieceRight();
                                if (das == 16) das = 10;
                            }
                        }
                        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_D) || 
                            pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_RIGHT)) das ++;
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
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D || 
                e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) das = 0;
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) gravity = 2;
            if (e.getKeyCode() == KeyEvent.VK_J || e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_Z) this.rotatePieceLeft();
            if (e.getKeyCode() == KeyEvent.VK_L || e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_X || 
                e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) this.rotatePieceRight();
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) gravity = gravityLevels[board.getLevel()];
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
        int squareSize = windowHeight*3/(4*board.getBoardHeight());
        int imageHeight = Math.floorDiv((squareSize)*4*board.getBoardHeight(), 3) + squareSize;
        int imageWidth = imageHeight*8/7;
        int imageOriginX = windowWidth/2 - imageWidth/2;
        //int imageOriginY = windowHeight/2 - imageHeight/2;

        if (boardWidth != 10 || boardHeight != 20) {
            //TODO: Fundo
        } else {
            try {
                BufferedImage background = ImageIO.read(new File("src\\TetrisBackground.png"));
                Image scaledBackground = background.getScaledInstance(imageWidth, imageHeight, BufferedImage.SCALE_DEFAULT);
                g.drawImage(scaledBackground, imageOriginX, 0, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int boardOriginX = (int) (imageOriginX + imageWidth/2 - board.getBoardWidth()/2*squareSize + squareSize*0.95f);
        int boardOriginY = (int) (imageHeight*0.175f);
        g.setColor(Color.WHITE);
        g.drawRect(boardOriginX, boardOriginY, 1, 1);
        
        if (boardWidth != 10 || boardHeight != 20) {
            
            int boardWidthPixels = squareSize*board.getBoardWidth();
            int boardHeightPixels = squareSize*board.getBoardHeight();
                
            // All Panel Color
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, windowWidth, windowHeight);
            
            //Board UI
            int boardBorderSize = (int) (0.25*squareSize);
            //Board Border Color
            g.setColor(Color.CYAN);
            g.fillRect(boardOriginX - boardBorderSize, boardOriginY - boardBorderSize, boardWidthPixels + 2*boardBorderSize, boardHeightPixels + 2*boardBorderSize);
            
            // Board color
            g.setColor(Color.BLACK);
            g.fillRect(boardOriginX, boardOriginY, boardWidthPixels, boardHeightPixels);
        }

    // Lines Box UI
        if (boardWidth != 10 || boardHeight != 20) {
            int linesBoxBorderSize = (int) (0.15*squareSize);
            int linesBoxWidth = (int) 11*squareSize;
            int linesBoxHeight = (int) 2*squareSize;
            int linesBoxOriginX = boardOriginX + Math.min(board.getBoardWidth()*squareSize/2 - linesBoxWidth/2, board.getBoardWidth()*squareSize - (int) (10*squareSize));
            int linesBoxOriginY = boardOriginY - (int) (3.4*squareSize);

            g.setColor(Color.CYAN);
            g.fillRect(linesBoxOriginX - linesBoxBorderSize, linesBoxOriginY - linesBoxBorderSize, linesBoxWidth + 2*linesBoxBorderSize, linesBoxHeight + 2*linesBoxBorderSize);
            
            g.setColor(Color.black);
            g.fillRect(linesBoxOriginX, linesBoxOriginY, linesBoxWidth, linesBoxHeight);

            int linesLabelOriginX = boardOriginX + (int) (0.3*squareSize) + Math.min(board.getBoardWidth()*squareSize/2 - linesBoxWidth/2, board.getBoardWidth()*squareSize - (int) (10*squareSize));
            int linesLabelOriginY = boardOriginY - (int) (2*squareSize);

            g.setColor(Color.WHITE);
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
                g.setFont(customFont);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            g.drawString("LINES -", linesLabelOriginX, linesLabelOriginY);
            
        }
        
        int linesTextOriginX = boardOriginX + board.getBoardWidth()*squareSize/2 + Math.min((int) (2.1*squareSize), board.getBoardWidth()*squareSize/2 - (int) (2.1*squareSize));
        int linesTextOriginY = boardOriginY - (int) (2*squareSize);

        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(new DecimalFormat("000").format(board.getLines()), linesTextOriginX, linesTextOriginY);

    // Score Box UI
        if (boardWidth != 10 || boardHeight != 20) {
            
            int scoreBoxBorderSize = (int) (0.15*squareSize);
            int scoreBoxOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.6*squareSize);
            int scoreBoxOriginY = boardOriginY - (int) (3*squareSize);
            int scoreBoxWidth = (int) 7*squareSize;
            int scoreBoxHeight = (int) 7*squareSize;

            g.setColor(Color.CYAN);
            g.fillRect(scoreBoxOriginX - scoreBoxBorderSize, scoreBoxOriginY - scoreBoxBorderSize, scoreBoxWidth + 2*scoreBoxBorderSize, scoreBoxHeight + 2*scoreBoxBorderSize);
            
            g.setColor(Color.black);
            g.fillRect(scoreBoxOriginX, scoreBoxOriginY, scoreBoxWidth, scoreBoxHeight);

            int highScoreLabelOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
            int highScoreLabelOriginY = boardOriginY - (int) (1*squareSize);

            g.setColor(Color.WHITE);
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
                g.setFont(customFont);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            g.drawString("TOP", highScoreLabelOriginX, highScoreLabelOriginY);

            int scoreLabelOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
            int scoreLabelOriginY = boardOriginY + (int) (2*squareSize);

            g.setColor(Color.WHITE);
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
                g.setFont(customFont);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            g.drawString("SCORE", scoreLabelOriginX, scoreLabelOriginY);
        }

        // Text
        int highScoreTextOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
        int highScoreTextOriginY = boardOriginY + (int) (0.1*squareSize);

        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        if (board.getScore() > highScore) {
            highScore = board.getScore();
        }
        g.drawString(new DecimalFormat("000000").format(highScore), highScoreTextOriginX, highScoreTextOriginY);


        int scoreTextOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
        int scoreTextOriginY = boardOriginY + (int) (3.1*squareSize);

        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(new DecimalFormat("000000").format(board.getScore()), scoreTextOriginX, scoreTextOriginY);
        
    // Next Piece UI
    int outline = 2;
    int highlightSize = 8;
        //Border UI
        if (boardWidth != 10 || boardHeight != 20) {
            int nextPieceBoxBorderSize = (int) (0.25*squareSize);
            int nextPieceBoxWidth = (int) (4.2*squareSize);
            int nextPieceBoxHeight = (int) (6*squareSize);
            int nextPieceOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
            int nextPieceOriginY = windowHeight/2 - nextPieceBoxHeight/2 - nextPieceBoxBorderSize;

            g.setColor(Color.CYAN);
            g.fillRect(nextPieceOriginX - nextPieceBoxBorderSize, nextPieceOriginY - nextPieceBoxBorderSize, nextPieceBoxWidth + 2*nextPieceBoxBorderSize, nextPieceBoxHeight + 2*nextPieceBoxBorderSize);
            
            g.setColor(Color.black);
            g.fillRect(nextPieceOriginX, nextPieceOriginY, nextPieceBoxWidth, nextPieceBoxHeight);

            int nextPieceLabelOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (2.1*squareSize);
            int nextPieceLabelOriginY = boardOriginY + (int) (7*squareSize);

            g.setColor(Color.WHITE);
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
                g.setFont(customFont);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            g.drawString("NEXT", nextPieceLabelOriginX, nextPieceLabelOriginY);
            
    // Level UI
        //Level Box UI
            int levelBoxBorderSize = (int) (0.15*squareSize);
            int levelBoxOriginX = boardOriginX - levelBoxBorderSize + board.getBoardWidth()*squareSize + (int) (1.8*squareSize);
            int levelBoxOriginY = nextPieceOriginY + nextPieceBoxHeight + 2*squareSize;
            int levelBoxWidth = (int) 6*squareSize;
            int levelBoxHeight = (int) 3*squareSize;

            g.setColor(Color.CYAN);
            g.fillRect(levelBoxOriginX - levelBoxBorderSize, levelBoxOriginY - levelBoxBorderSize, levelBoxWidth + 2*levelBoxBorderSize, levelBoxHeight + 2*levelBoxBorderSize);
            
            g.setColor(Color.black);
            g.fillRect(levelBoxOriginX, levelBoxOriginY, levelBoxWidth, levelBoxHeight);

            int levelLabelOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
            int levelLabelOriginY = boardOriginY + board.getBoardHeight()*squareSize - (int) (5.1*squareSize);

            g.setColor(Color.WHITE);
            try {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
                g.setFont(customFont);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            g.drawString("LEVEL", levelLabelOriginX, levelLabelOriginY);
        }
        
        // Text
        
        int levelBoxTextOriginX = boardOriginX + board.getBoardWidth()*squareSize + (int) (1.9*squareSize);
        int levelBoxTextOriginY = boardOriginY + board.getBoardHeight()*squareSize - (int) (5.1*squareSize);
        g.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(new DecimalFormat("00").format(board.getLevel()), levelBoxTextOriginX + 2*squareSize, levelBoxTextOriginY + squareSize);
            
            
        // Piece UI
        Piece nextPiece = board.getNextPiece();
        Square nextPieceSquare = new Square();
        nextPieceSquare.setColorType(nextPiece.getColorType());
        int nextPieceCenterX = boardOriginX + board.getBoardWidth()*squareSize + (int) (2.9*squareSize);
        int nextPieceOriginX = nextPieceCenterX;
        switch (nextPiece.getType()) {
            case I -> nextPieceOriginX -= squareSize;
            case O -> nextPieceOriginX -= squareSize;
            case T -> nextPieceOriginX -= squareSize*1/2;
            case J -> nextPieceOriginX -= squareSize*1/2;
            case L -> nextPieceOriginX -= squareSize*1/2;
            case S -> nextPieceOriginX -= squareSize*1/2;
            case Z -> nextPieceOriginX -= squareSize*1/2;
        }
        int nextPieceOriginY = boardOriginY + board.getBoardHeight()*squareSize - (int) (12*squareSize);
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
        for (int i = 0; i < board.getBoardWidth(); i++) {
            for (int j = 0; j < board.getBoardHeight(); j++) {
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

        // Stats UI
        if (boardWidth == 10 && boardHeight == 20) {
            int smallSquareSize = squareSize*4/5;

            // Piece UI
            for (int i = 0; i < 7; i++) {
                Piece piece = new Piece(Piece.PieceType.values()[i]);
                Square square = new Square();
                square.setColorType(piece.getColorType());
                int statsOriginX = boardOriginX - (int) (8.3*squareSize);
                switch (piece.getType()) {
                    case I -> statsOriginX -= squareSize*1/2;
                    case O -> statsOriginX -= squareSize*1/2;
                    case T -> {}
                    case J -> {}
                    case L -> {}
                    case S -> {}
                    case Z -> {}
                }
                int statsOriginY = boardOriginY + (int) (5*squareSize) + (int) (i*2.4*smallSquareSize);
                for(int[] squareCoords : piece.getShape(0)){
                    g.setColor(square.getMainColor(board.getLevel()));
                    g.fillRect(statsOriginX + outline + squareCoords[0]*smallSquareSize, statsOriginY + outline + squareCoords[1]*smallSquareSize, smallSquareSize - 2*outline, smallSquareSize - 2*outline);
                    g.setColor(square.getHighlightColor(board.getLevel()));
                    g.fillRect(statsOriginX + outline + squareCoords[0]*smallSquareSize, statsOriginY + outline + squareCoords[1]*smallSquareSize, smallSquareSize/highlightSize, smallSquareSize/highlightSize);
                    if (!square.isVariant()) {
                        g.fillRect(statsOriginX + outline + squareCoords[0]*smallSquareSize + smallSquareSize/highlightSize, statsOriginY + outline + squareCoords[1]*smallSquareSize + smallSquareSize/highlightSize, smallSquareSize/highlightSize * 2, smallSquareSize/highlightSize);
                        g.fillRect(statsOriginX + outline + squareCoords[0]*smallSquareSize + smallSquareSize/highlightSize, statsOriginY + outline + squareCoords[1]*smallSquareSize + smallSquareSize/highlightSize, smallSquareSize/highlightSize, smallSquareSize/highlightSize * 2);
                    } else {
                        g.fillRect(statsOriginX + outline + squareCoords[0]*smallSquareSize + smallSquareSize/highlightSize, statsOriginY + outline + squareCoords[1]*smallSquareSize + smallSquareSize/highlightSize, smallSquareSize*2/3, smallSquareSize*2/3);
                    }
                }
                // Stats Text
                int statsLabelOriginX = boardOriginX - (int) (5.3*squareSize);
                int statsLabelOriginY = boardOriginY + (int) (7*squareSize);
                g.setColor(Color.RED);
                try {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(squareSize*1f);
                    g.setFont(customFont);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }
                g.drawString(new DecimalFormat("000").format(board.getStats(piece.getType())), statsLabelOriginX, statsLabelOriginY + (int) (i*2.5*smallSquareSize));
            }

        }
    }

    public int getHighScore() {
        return highScore;
    }
    
    public static void playMusic(String path) {
        musicPlayer.playMusic(path);
    }

    public static void stopMusic() {
        musicPlayer.stopMusic();
    }

    public static void playSound(String path) {
        soundPlayer.playSound(path);
    }

    public static boolean getSoundIsRunning() {
        return soundPlayer.getSoundIsRunning();
    }

    public void restart() {
        board = new Board(startLevel, boardWidth, boardHeight);
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
            if(board.tryMove(0, 0, 1)) {
                repaint();
                if (!getSoundIsRunning()) playSound("src\\RotatePieceSound.wav");
            }
        }
    }

    public void rotatePieceLeft() {
        if (!isPaused()) {
            if(board.tryMove(0, 0, 3)) {
                repaint();
                if (!getSoundIsRunning()) playSound("src\\RotatePieceSound.wav");
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
            if (board.tryMove(-1, 0, 0)) {
                repaint();
                if (!getSoundIsRunning()) playSound("src\\MovingPieceSound.wav");
            }
        }
    }

    public void movePieceRight() {
        if (!isPaused()) {
            if (board.tryMove(1, 0, 0)) {
                repaint();
                if (!getSoundIsRunning()) playSound("src\\MovingPieceSound.wav");
            }
        }
    }

    public static void repaintUI() {
        Tetris.game.repaint();
    }
}
