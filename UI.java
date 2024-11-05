
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class UI extends JPanel {
    private static boolean paused = false;
    private static boolean forcePaused = false;
    private Board board;
    private static AudioPlayer musicPlayer = new AudioPlayer();
    private static AudioPlayer soundPlayer = new AudioPlayer();

    public UI() {
        board = new Board();
        board.newPiece();
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

    public void restart() {
        board = new Board();
        board.newPiece();
    }

    public static boolean isPaused() {
        return !paused && !forcePaused;
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
        if (isPaused()) {
            board.tryMove(0, 0, 1);
            repaint();
        }
    }

    public void rotatePieceLeft() {
        if (isPaused()) {
            board.tryMove(0, 0, 3);
            repaint();
        }
    }

    public void movePieceDown() {
        if (isPaused()) {
            board.movePieceDown();
            repaint();
        }
    }

    public void movePieceLeft() {
        if (isPaused()) {
            board.tryMove(-1, 0, 0);
            repaint();
        }
    }

    public void movePieceRight() {
        if (!paused && !forcePaused) {
            board.tryMove(1, 0, 0);
            repaint();
        }
    }

    public static void repaintUI() {
        Tetris.game.repaint();
    }
    
    @Override 
	public void paintComponent(Graphics g) {

        super.paintComponent(g);

        //Board UI
            // all Panel Color
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

            // Board Box Color
            g.setColor(Color.CYAN);
            g.fillRect(getWindowWidth()/2 - (int) ((Board.BOARD_WIDTH/2+0.25)*getSquareSize()), getWindowHeight()/20 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH+0.5)*getSquareSize()), (int) ((Board.BOARD_HEIGHT+0.5)*getSquareSize()));

            //inside Board color
            g.setColor(Color.BLACK);
            g.fillRect(getWindowWidth()/2 - Board.BOARD_WIDTH/2*getSquareSize(), getWindowHeight()/20 - 1, getSquareSize()*Board.BOARD_WIDTH, getSquareSize()* Board.BOARD_HEIGHT);

        // Score UI
            //Box UI
            g.setColor(Color.CYAN);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/17 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));

            g.setColor(Color.black);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/14 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));

            // Text UI
            g.setColor(Color.WHITE);
            g.setFont(new Font("Clarendo", Font.BOLD, 20));
            g.drawString("Score: " + board.getScore(), getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.10)*getSquareSize()));

        // Next Pice UI
            //Box UI
            g.setColor(Color.CYAN);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/4 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  4)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 14.75)*getSquareSize()));

            g.setColor(Color.black);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  4.5)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 15.25)*getSquareSize()));

            // Piece UI
            g.setColor(board.getNextPiece().getColor());
            for(int[] square : board.getNextPiece().getShape(0)){
                g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()), (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()), getSquareSize() -1, getSquareSize() -1);
            }
            
        // Level UI
            //Box UI
            g.setColor(Color.CYAN);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/2 -  (int) (0.25*getSquareSize()) + 3* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));
            
            g.setColor(Color.black);
            g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/2 - (int) (0.25*getSquareSize()) + 4* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));

            // Text UI
            g.setColor(Color.WHITE);
            g.setFont(new Font("Clarendo", Font.BOLD, 20));
            g.drawString("Level: " + board.getLevel(), getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 6.6)*getSquareSize()));
        
        //Lines UI maybe
            //Box UI
            //TODO: fazer as caixas
            //g.setColor(Color.CYAN);
            //g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/2 -  (int) (0.25*getSquareSize()) + 3* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));
            
            //g.setColor(Color.black);
            //g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/2 - (int) (0.25*getSquareSize()) + 4* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));


            //Text UI
            //g.setColor(Color.WHITE);
            //g.setFont(new Font("Clarendo", Font.BOLD, 20));
            //g.drawString("Lines: " + board.getLines(), (int) ((Board.BOARD_WIDTH -  4)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.10)*getSquareSize()));
            
		for (int i = 0; i < Board.BOARD_WIDTH; i++) {
			for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
				g.setColor(this.board.getSquare(i, j).getColor());
				g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2), getWindowHeight()/20 + getSquareSize()*j, getSquareSize()-1, getSquareSize()-1);
			}
		}
	}
}
