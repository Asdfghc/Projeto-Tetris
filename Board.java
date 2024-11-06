import java.awt.Color;
import java.util.HashSet;


public class Board{
    protected static final int BOARD_HEIGHT = 20;
    protected static final int BOARD_WIDTH = 10;
    protected static final Color EMPTY_COLOR = Color.BLACK;
    
    private final Square[][] board;
    private Piece currentPiece;
    private Piece nextPiece;
    private int[] currentPieceCoords;
    private int currentPieceRotation;
    private boolean gameOver = false;

    private int TotalLinesCleared = 0;
    private int TotalLevel = 0;
    private int TotalScore = 0;

    public Board() {
        this.board = new Square[BOARD_WIDTH][BOARD_HEIGHT];
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                this.board[i][j] = new Square();
            }
        }
        UI.forceUnpause();
        UI.stopMusic();
        UI.playMusic("src\\TetrisMusic.wav");
        nextPiece = new Piece(Piece.PieceType.values()[(int)(Math.random()*7)]);
    }

    public Square getSquare(int i, int j) {
        return this.board[i][j];
    }

    public Square[] occupiedSquares(int x, int y, int rotation) {
        Square[] squares = new Square[4];
        for (int i = 0; i < 4; i++) {
            int x1 = currentPiece.getShape(rotation)[i][0];
            int y1 = currentPiece.getShape(rotation)[i][1];
            if (x + x1 < 0 || x + x1 >= BOARD_WIDTH || y + y1 < 0 || y + y1 >= BOARD_HEIGHT) {
                return null;
            }
            squares[i] = this.board[x + x1][y + y1];
        }
        return squares;
    }

    public boolean checkCollision(int x, int y, int rotation) {
        if (occupiedSquares(x, y, rotation) == null) {
            return true;
        }
        for(Square s : occupiedSquares(x, y, rotation)) {
            if (s.isOccupied()) {
                return true;
            }
        }
        return false;
    }

    public boolean tryMove(int x, int y, int rotation) {
        if (!checkCollision(currentPieceCoords[0] + x, currentPieceCoords[1] + y, (currentPieceRotation + rotation) % 4)) {
            for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                s.setMainColor(EMPTY_COLOR);
                s.setHighlightColor(EMPTY_COLOR);
            }
            currentPieceCoords[0] += x;
            currentPieceCoords[1] += y;
            currentPieceRotation = (currentPieceRotation + rotation) % 4;
            for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                s.setMainColor(currentPiece.getMainColor());
                s.setHighlightColor(currentPiece.getHighlightColor());
                s.setVariant(currentPiece.isVariant());
            }
            return true;
        }
        return false;
    }

    public void newPiece() {
        if(gameOver == true){
            return;
        }
        currentPiece =  nextPiece;
        nextPiece = new Piece(Piece.PieceType.values()[(int)(Math.random()*7)]);
        currentPieceCoords = new int[]{BOARD_WIDTH/2-2, 0};
        currentPieceRotation = 0;

        if(checkCollision(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)){  // v se tem colição, se tiver, vai ser true e vai printar game over
            gameOver = true;
            System.out.println("Game Over");
            UI.forcePause();
            UI.stopMusic();
            UI.playSound("src\\GameOverSound.wav");

            return;
        }
        for (int i = 0; i < 4; i++) {
            int x = currentPiece.getShape(currentPieceRotation)[i][0];
            int y = currentPiece.getShape(currentPieceRotation)[i][1];
            this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setMainColor(currentPiece.getMainColor());
            this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setHighlightColor(currentPiece.getHighlightColor());
            this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setVariant(currentPiece.isVariant());
        }
    }
    
    public void clearLines() {
        UI.forcePause();
        HashSet<Integer> lines = new HashSet<>();
        int LinesCleared = 0;
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            boolean full = true;
            for (int i = 0; i < BOARD_WIDTH; i++) {
                if (!this.board[i][j].isOccupied()) {
                    full = false;
                    break;
                }
            }
            if (full) {
                lines.add(j);
                LinesCleared++;
                TotalLinesCleared++;
                if(TotalLinesCleared == 10)
                {
                    TotalLinesCleared = 0;
                    TotalLevel++;
                }
                TotalScore += 40*(TotalLevel + 1);
            }
        }
        if(LinesCleared == 4) {
            UI.playSound("src\\ClearLineSound.wav");
        }
        if(LinesCleared < 4 && LinesCleared >= 1 ) {
            UI.playSound("src\\ClearOneLineSound.wav");
        }
        if(LinesCleared == 0) {
            UI.playSound("src\\FallingPieceSound.wav");
        }
        if (!lines.isEmpty()) {
            
            for (int q=0; q < BOARD_WIDTH; q++) {
                for (int j : lines) {
                    int i = BOARD_WIDTH/2 + ( q % 2 == 0 ? q/2 : -(q/2+1)); //index lookup here
                    this.board[i][j].setOccupied(false);
                    this.board[i][j].setMainColor(EMPTY_COLOR);
                    this.board[i][j].setHighlightColor(EMPTY_COLOR);
                }
                try {
                    Thread.sleep(34);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (q % 2 == 0) UI.repaintUI();
            }
            for (int j : lines) {
                for (int k = j; k > 0; k--) {
                    for (int i = 0; i < BOARD_WIDTH; i++) {
                        this.board[i][k].setOccupied(this.board[i][k-1].isOccupied());
                        this.board[i][k].setMainColor(this.board[i][k-1].getMainColor());
                        this.board[i][k].setHighlightColor(this.board[i][k-1].getHighlightColor());
                        this.board[i][k].setVariant(this.board[i][k-1].isVariant());
                    }
                }
            }
        }
        UI.forceUnpause();
    }

    public void movePieceDownGravity() {
        if(!tryMove(0, 1, 0)) {
            for (int i = 0; i < 4; i++) {
                int x = currentPiece.getShape(currentPieceRotation)[i][0];
                int y = currentPiece.getShape(currentPieceRotation)[i][1];
                this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setOccupied(true);
            }
            clearLines();
            newPiece();
        }
    }

    public void movePieceDown() {
        if (!tryMove(0, 1, 0)) {
            for (int i = 0; i < 4; i++) {
                int x = currentPiece.getShape(currentPieceRotation)[i][0];
                int y = currentPiece.getShape(currentPieceRotation)[i][1];
                this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setOccupied(true);
            }

        }
    }

    public int getLines()
    {
        return this.TotalLinesCleared;
    }

    public int getScore()
    {
        return this.TotalScore;
    }

    public int getLevel()
    {
        return this.TotalLevel;
    }

    public Piece getNextPiece()
    {
        return this.nextPiece;
    }
}
