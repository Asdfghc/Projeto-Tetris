import java.awt.Color;

public class Board{
    protected static final int BOARD_HEIGHT = 20;
    protected static final int BOARD_WIDTH = 10;
    protected static final Color EMPTY_COLOR = Color.BLACK;
    
    private Square[][] board;
    private Piece currentPiece;
    private int[] currentPieceCoords;
    private int currentPieceRotation;
    private boolean gameOver = false;

    public Board() {
        this.board = new Square[BOARD_WIDTH][BOARD_HEIGHT];
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                this.board[i][j] = new Square();
            }
        }
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
                s.setColor(EMPTY_COLOR);
            }
            currentPieceCoords[0] += x;
            currentPieceCoords[1] += y;
            currentPieceRotation = (currentPieceRotation + rotation) % 4;
            for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                s.setColor(currentPiece.getColor());
            }
            return true;
        }
        return false;
    }

    private boolean collisionPosition() {   // vai verificar se tem colission na posição onde as novas peças são geradas
        return checkCollision(4, 0, 0);
    }
    
    public boolean eGameOver() {
        return gameOver;
    }

    public void newPiece() {
        if(gameOver == true){
            return;
        }
        currentPiece = new Piece(Piece.PieceType.values()[(int)(Math.random()*7)]);
        currentPieceCoords = new int[]{BOARD_WIDTH/2-2, 0};

        if(collisionPosition() == true){  // v se tem colição, se tiver, vai ser true e vai printar game over
            gameOver = true;
            System.out.println("Game Over");
            return;
        }
        for (int i = 0; i < 4; i++) {
            int x = currentPiece.getShape(currentPieceRotation)[i][0];
            int y = currentPiece.getShape(currentPieceRotation)[i][1];
            this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setColor(currentPiece.getColor());
        }
    }

    public void clearLine(int j) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            this.board[i][j].setOccupied(false);
            this.board[i][j].setColor(EMPTY_COLOR);
        }
        for (int k = j; k > 0; k--) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                this.board[i][k].setOccupied(this.board[i][k-1].isOccupied());
                this.board[i][k].setColor(this.board[i][k-1].getColor());
            }
        }
        
    }

    public void clearLines() {
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            boolean full = true;
            for (int i = 0; i < BOARD_WIDTH; i++) {
                if (!this.board[i][j].isOccupied()) {
                    full = false;
                    break;
                }
            }
            if (full) {
                clearLine(j);
            }
        }
    }

    public void movePieceDown() {
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
}
