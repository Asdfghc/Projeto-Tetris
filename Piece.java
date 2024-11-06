import java.awt.Color;

public class Piece {
    public enum PieceType {
        I, J, L, O, S, T, Z
    }
    private PieceType type;
    private int[][][] shape; //[rotation][squares][x, y]
    private Color mainColor;
    private Color highlightColor;
    private boolean variant;

    public Piece(PieceType type) {
        this.type = type;
        this.shape = new int[4][4][2];
        switch (this.type) {
            case I -> {
                this.mainColor = Color.CYAN;
                this.highlightColor = Color.WHITE;
                this.variant = false;
                this.shape[0] = new int[][]{{0, 2}, {1, 2}, {2, 2}, {3, 2}};
                this.shape[1] = new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}};
                this.shape[2] = new int[][]{{0, 2}, {1, 2}, {2, 2}, {3, 2}};
                this.shape[3] = new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}};
            }
            case J -> {
                this.mainColor = Color.BLUE;
                this.highlightColor = Color.WHITE;
                this.variant = false;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {2, 2}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 2}};
                this.shape[2] = new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 1}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 0}};
            }
            case L -> {
                this.mainColor = Color.ORANGE;
                this.highlightColor = Color.WHITE;
                this.variant = false;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {0, 2}};
                this.shape[1] = new int[][]{{0, 0}, {1, 0}, {1, 1}, {1, 2}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {2, 0}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 2}};
            }
            case O -> {
                this.mainColor = Color.YELLOW;
                this.highlightColor = Color.WHITE;
                this.variant = true;
                this.shape[0] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                this.shape[1] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                this.shape[2] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                this.shape[3] = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
            }
            case S -> {
                this.mainColor = Color.GREEN;
                this.highlightColor = Color.WHITE;
                this.variant = false;
                this.shape[0] = new int[][]{{0, 2}, {1, 2}, {1, 1}, {2, 1}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {2, 1}, {2, 2}};
                this.shape[2] = new int[][]{{0, 2}, {1, 2}, {1, 1}, {2, 1}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {2, 1}, {2, 2}};
            }
            case Z -> {
                this.mainColor = Color.RED;
                this.highlightColor = Color.WHITE;
                this.variant = false;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {1, 2}, {2, 2}};
                this.shape[1] = new int[][]{{2, 0}, {2, 1}, {1, 1}, {1, 2}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {1, 2}, {2, 2}};
                this.shape[3] = new int[][]{{2, 0}, {2, 1}, {1, 1}, {1, 2}};
            }
            case T -> {
                this.mainColor = Color.MAGENTA;
                this.highlightColor = Color.WHITE;
                this.variant = true;
                this.shape[0] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 2}};
                this.shape[1] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 1}};
                this.shape[2] = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 0}};
                this.shape[3] = new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 1}};
            }
        }
    }

    public int[][] getShape(int rotation) {
        return this.shape[rotation];
    }

    public Color getMainColor() {
        return this.mainColor;
    }

    public Color getHighlightColor() {
        return this.highlightColor;
    }

    public boolean isVariant() {
        return this.variant;
    }
}
