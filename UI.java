
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class UI extends JPanel {
    private final int BOARD_HEIGHT = 20;
    //private final int BOARD_WIDTH = 10;
    private Board board;

    public UI() {
        board = new Board();
        board.newPiece();
    }

    public void restart() {
        board = new Board();
        board.newPiece();
    }

    public int getWindowHeight() {
        return (int) (getSize().getHeight());
    }

    public int getWindowWidth() {
        return (int) (getSize().getWidth());
    }

    public int getSquareSize() {
        return getWindowHeight()*9/10/BOARD_HEIGHT;
    }

    /*public static final int screenHeightPixels() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = screenSize.getHeight();
        return (int) height; //40 da barra do windows
    }

    public static final int screenWidthPixels() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        return (int) width;
    }*/

    public void rotatePieceRight() {                     //TODO: deixar consistente
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
        board.movePieceLeft();
        repaint();
    }

    public void movePieceRight() {
        board.movePieceRight();
        repaint();
    }

    @Override 
	public void paintComponent(Graphics g) {
        
        g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				g.setColor(this.board.getSquare(i, j).getColor());
				g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-5), getWindowHeight()/20 + getSquareSize()*j, getSquareSize()-1, getSquareSize()-1);
			}
		}
	}
}
