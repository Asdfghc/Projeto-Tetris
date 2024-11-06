
import java.awt.Color;

public class Square {
    private Color mainColor;
    private Color highlightColor;
    private boolean variant;
    private boolean occupied;

    public Square() {
        this.mainColor = Board.EMPTY_COLOR;
        this.highlightColor = Board.EMPTY_COLOR;
        this.variant = false;
        this.occupied = false;
    }

    public Color getMainColor() {
        return this.mainColor;
    }

    public void setMainColor(Color color) {
        this.mainColor = color;
    }

    public Color getHighlightColor() {
        return this.highlightColor;
    }

    public void setHighlightColor(Color color) {
        this.highlightColor = color;
    }

    public boolean isVariant() {
        return this.variant;
    }

    public void setVariant(boolean variant) {
        this.variant = variant;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
