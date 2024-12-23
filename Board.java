import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class Board{
    private final int boardWidth;
    private final int boardHeight;
    
    private final Square[][] board;
    private Piece currentPiece;
    private Piece nextPiece;
    private int[] currentPieceCoords;
    private int currentPieceRotation;
    private boolean gameOver = false;

    private int TotalLinesCleared = 0;
    private int TotalLevel = 0;
    private int TotalScore = 0;

    private final Map<Piece.PieceType, Integer> stats = new HashMap<>();

    public Board(int level, int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.board = new Square[boardWidth][boardHeight];
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                this.board[i][j] = new Square();
            }
        }
        for (Piece.PieceType type : Piece.PieceType.values()) {
            stats.put(type, 0);
        }
        Game.forceUnpause();
        Game.stopMusic();
        Game.playMusic("src\\TetrisMusic.wav");
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
            if (x + x1 < 0 || x + x1 >= boardWidth || y + y1 < 0 || y + y1 >= boardHeight) {
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
                s.setColorType(0);
            }
            currentPieceCoords[0] += x;
            currentPieceCoords[1] += y;
            currentPieceRotation = (currentPieceRotation + rotation) % 4;
            for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                s.setColorType(currentPiece.getColorType());
            }
            return true;
        } else if (x == 0 && y == 0) {
            if (!checkCollision(currentPieceCoords[0] + 1, currentPieceCoords[1], (currentPieceRotation + rotation) % 4)) {
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(0);
                }
                currentPieceCoords[0] += 1;
                currentPieceRotation = (currentPieceRotation + rotation) % 4;
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(currentPiece.getColorType());
                }
                return true;
            } else if (!checkCollision(currentPieceCoords[0] - 1, currentPieceCoords[1], (currentPieceRotation + rotation) % 4)) {
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(0);
                }
                currentPieceCoords[0] -= 1;
                currentPieceRotation = (currentPieceRotation + rotation) % 4;
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(currentPiece.getColorType());
                }
                return true;
            } else if (!checkCollision(currentPieceCoords[0] + 2, currentPieceCoords[1], (currentPieceRotation + rotation) % 4)) {
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(0);
                }
                currentPieceCoords[0] += 2;
                currentPieceRotation = (currentPieceRotation + rotation) % 4;
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(currentPiece.getColorType());
                }
                return true;
            } else if (!checkCollision(currentPieceCoords[0], currentPieceCoords[1] + 1, (currentPieceRotation + rotation) % 4)) {
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(0);
                }
                currentPieceCoords[1] += 1;
                currentPieceRotation = (currentPieceRotation + rotation) % 4;
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(currentPiece.getColorType());
                }
                return true;
            } else if (!checkCollision(currentPieceCoords[0], currentPieceCoords[1] + 2, (currentPieceRotation + rotation) % 4)) {
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(0);
                }
                currentPieceCoords[1] += 2;
                currentPieceRotation = (currentPieceRotation + rotation) % 4;
                for (Square s : occupiedSquares(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)) {
                    s.setColorType(currentPiece.getColorType());
                }
                return true;
            }
        }
        return false;
    }

    public void newPiece() { //TODO: ARE
        if(gameOver == true){
            return;
        }
        currentPiece = nextPiece;
        do {
            nextPiece = new Piece(Piece.PieceType.values()[(int)(Math.random()*7)]);
        } while(nextPiece.getType() == currentPiece.getType());
        stats.put(currentPiece.getType(), stats.get(currentPiece.getType()) + 1);
        if (currentPiece.getType() == Piece.PieceType.I) currentPieceCoords = new int[]{boardWidth/2-2, -2};
        else currentPieceCoords = new int[]{boardWidth/2-2, -1};
        currentPieceRotation = 0;

        if(checkCollision(currentPieceCoords[0], currentPieceCoords[1], currentPieceRotation)){  // v se tem colição, se tiver, vai ser true e vai printar game over
            gameOver = true;
            System.out.println("Game Over");
            Game.forcePause();
            Game.stopMusic();
            Game.playSound("src\\GameOverSound.wav");

            return;
        }
        for (int i = 0; i < 4; i++) {
            int x = currentPiece.getShape(currentPieceRotation)[i][0];
            int y = currentPiece.getShape(currentPieceRotation)[i][1];
            this.board[currentPieceCoords[0] + x][currentPieceCoords[1] + y].setColorType(currentPiece.getColorType());
        }
    }
    
    public void clearLines() {
        LinkedHashSet<Integer> lines = new LinkedHashSet<>();
        int LinesCleared = 0;
        for (int j = 0; j < boardHeight; j++) {
            boolean full = true;
            for (int i = 0; i < boardWidth; i++) {
                if (!this.board[i][j].isOccupied()) {
                    full = false;
                    break;
                }
            }
            if (full) {
                lines.add(j);
                LinesCleared++;
                TotalLinesCleared++;
                if(TotalLinesCleared % 10 == 0)
                {
                    TotalLevel++;
                }
            }
        }

        switch(LinesCleared) {    //Pontuação do jogo,dependendo do nivel que
            case 1 -> TotalScore += 40*(TotalLevel + 1);
            case 2 -> TotalScore += 100*(TotalLevel + 1);
            case 3 -> TotalScore += 300*(TotalLevel + 1);
            case 4 -> TotalScore += 1200*(TotalLevel + 1);
        }
        //Pontuação do jogo,dependendo do nivel que

        
        if(LinesCleared == 4) {
            Game.playSound("src\\ClearLineSound.wav");
        }
        if(LinesCleared < 4 && LinesCleared >= 1 ) {
            Game.playSound("src\\ClearOneLineSound.wav");
        }
        if(LinesCleared == 0) {
            Game.playSound("src\\FallingPieceSound.wav");
        }
        if (!lines.isEmpty()) {
            for (int q = 0; q < boardWidth; q++) {
                for (int j : lines) {
                    int i = boardWidth/2 + ( q % 2 == 0 ? q/2 : -(q/2+1));
                    this.board[i][j].setOccupied(false);
                    this.board[i][j].setColorType(0);
                }
                try {
                    Thread.sleep(2*Game.FRAME_LENGTH);
                } catch (InterruptedException e) {
                    System.err.println("InterruptedException: " + e.getMessage());
                }
                if (q % 2 == 0) Game.repaintUI();
            }
            for (int j : lines) {
                for (int k = j; k > 0; k--) {
                    for (int i = 0; i < boardWidth; i++) {
                        this.board[i][k].setOccupied(this.board[i][k-1].isOccupied());
                        this.board[i][k].setColorType(this.board[i][k-1].getColorType());
                    }
                }
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

    public int getBoardHeight()
    {
        return this.boardHeight;
    }

    public int getBoardWidth()
    {
        return this.boardWidth;
    }

    public int getStats(Piece.PieceType type) {
        return stats.get(type);
    }
}
