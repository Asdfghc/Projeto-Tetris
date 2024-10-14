
import java.awt.Color;

public class Square {
    private Color color;
    private boolean occupied;

    public Square() {
        this.color = Color.GRAY;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
