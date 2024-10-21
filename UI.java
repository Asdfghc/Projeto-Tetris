
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class UI extends JPanel {
    private Board board;
    private boolean paused = false;

    public UI() {
        board = new Board();
        board.newPiece();
    }

    public void restart() {
        board = new Board();
        board.newPiece();
    }

    public void pause() {
        paused = !paused;
    }

    public boolean isRunning() {
        return !paused;
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
        board.tryMove(0, 0, 1);
        repaint();
    }

    public void rotatePieceLeft() {
        board.tryMove(0, 0, 3);
        repaint();
    }

    public void movePieceDown() {
        board.movePieceDown();
        repaint();
    }

    public void movePieceLeft() {
        board.tryMove(-1, 0, 0);
        repaint();
    }

    public void movePieceRight() {
        board.tryMove(1, 0, 0);
        repaint();
    }

    @Override 
	public void paintComponent(Graphics g) {
        
        g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

        g.setColor(Color.CYAN);
        g.fillRect(getWindowWidth()/2 - (int) ((Board.BOARD_WIDTH/2+0.25)*getSquareSize()), getWindowHeight()/20 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH+0.5)*getSquareSize()), (int) ((Board.BOARD_HEIGHT+0.5)*getSquareSize()));

        g.setColor(Color.BLACK);
        g.fillRect(getWindowWidth()/2 - Board.BOARD_WIDTH/2*getSquareSize(), getWindowHeight()/20 - 1, getSquareSize()*Board.BOARD_WIDTH, getSquareSize()* Board.BOARD_HEIGHT);

		for (int i = 0; i < Board.BOARD_WIDTH; i++) {
			for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
				g.setColor(this.board.getSquare(i, j).getColor());
				g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2), getWindowHeight()/20 + getSquareSize()*j, getSquareSize()-1, getSquareSize()-1);
			}
		}
	}
}
