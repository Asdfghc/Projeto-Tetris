import java.awt.Color;

public class Piece {
    public enum PieceType {
        I, J, L, O, S, T, Z
    }
    private PieceType type;
    private int[][][] shape; //[rotation][row][column][x, y]
    private Color color;

    public Piece(PieceType type) {
        this.type = type;
        this.shape = new int[4][4][2];
        switch (this.type) {
            case I:
                this.color = Color.CYAN;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {3, 1}};
                this.shape[1] = new int[][]{{3, 0}, {3, 1}, {3, 2}, {3, 3}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {3, 1}};
                this.shape[3] = new int[][]{{3, 0}, {3, 1}, {3, 2}, {3, 3}};
                break;
            case J:
                this.color = Color.BLUE;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {2, 0}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 2}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {0, 2}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 0}};
                break;
            case L:
                this.color = Color.ORANGE;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {2, 2}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 2}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {0, 0}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 0}};
                break;
            case O:
                this.color = Color.YELLOW;
                this.shape[0] = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
                this.shape[1] = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
                this.shape[2] = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
                this.shape[3] = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
                break;
            case S:
                this.color = Color.GREEN;
                this.shape[0] = new int[][]{{1, 0}, {2, 0}, {0, 1}, {1, 1}};
                this.shape[1] = new int[][]{{0, 0}, {0, 1}, {1, 1}, {1, 2}};
                this.shape[2] = new int[][]{{1, 0}, {2, 0}, {0, 1}, {1, 1}};
                this.shape[3] = new int[][]{{0, 0}, {0, 1}, {1, 1}, {1, 2}};
                break;
            case T:
                this.color = Color.MAGENTA;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 0}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 1}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 2}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 1}};
                break;
            case Z:
                this.color = Color.RED;
                this.shape[0] = new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}};
                this.shape[1] = new int[][]{{1, 0}, {0, 1}, {1, 1}, {0, 2}};
                this.shape[2] = new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}};
                this.shape[3] = new int[][]{{1, 0}, {0, 1}, {1, 1}, {0, 2}};
                break;
        }
    }

    public int[][] getShape(int rotation) {
        return this.shape[rotation];
    }

    public Color getColor() {
        return this.color;
    }

}