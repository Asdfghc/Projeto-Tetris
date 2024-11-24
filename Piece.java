public class Piece {
    public enum PieceType {
        T, J, Z, O, S, L, I
    }
    private PieceType type;
    private int[][][] shape; //[rotation][squares][x, y]
    private int colorType;

    public Piece(PieceType type) {
        this.type = type;
        this.shape = new int[4][4][2];
        switch (this.type) {
            case I -> {
                this.colorType = 1;
                this.shape[0] = new int[][]{{0, 2}, {1, 2}, {2, 2}, {3, 2}};
                this.shape[1] = new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}};
                this.shape[2] = new int[][]{{0, 2}, {1, 2}, {2, 2}, {3, 2}};
                this.shape[3] = new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}};
            }
            case J -> {
                this.colorType = 2;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {2, 2}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 2}};
                this.shape[2] = new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 1}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 0}};
            }
            case L -> {
                this.colorType = 3;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {0, 2}};
                this.shape[1] = new int[][]{{0, 0}, {1, 0}, {1, 1}, {1, 2}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {2, 0}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 2}};
            }
            case O -> {
                this.colorType = 1;
                this.shape[0] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                this.shape[1] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                this.shape[2] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                this.shape[3] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
            }
            case S -> {
                this.colorType = 2;
                this.shape[0] = new int[][]{{0, 2}, {1, 2}, {1, 1}, {2, 1}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {2, 1}, {2, 2}};
                this.shape[2] = new int[][]{{0, 2}, {1, 2}, {1, 1}, {2, 1}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {2, 1}, {2, 2}};
            }
            case Z -> {
                this.colorType = 3;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {1, 2}, {2, 2}};
                this.shape[1] = new int[][]{{2, 0}, {2, 1}, {1, 1}, {1, 2}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {1, 2}, {2, 2}};
                this.shape[3] = new int[][]{{2, 0}, {2, 1}, {1, 1}, {1, 2}};
            }
            case T -> {
                this.colorType = 1;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 2}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 1}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 0}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 1}};
            }
        }
    }

    public PieceType getType() {
        return this.type;
    }

    public int[][] getShape(int rotation) {
        return this.shape[rotation];
    }

    public int getColorType() {
        return this.colorType;
    }
}
