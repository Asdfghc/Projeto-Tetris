import java.awt.Color;

public class Square {
    private static final Color EMPTY_COLOR = Color.BLACK;

    private int colorType;
    private boolean occupied;

    private static final Color[][] colors = {
        {EMPTY_COLOR, new Color(0, 255, 255), new Color(0, 0, 255), new Color(255, 165, 0)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)},
        {EMPTY_COLOR, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)}
    };

    public Square() {
        this.colorType = 0;
        this.occupied = false;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public int getColorType() {
        return this.colorType;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public Color getMainColor(int level) {
        return colors[level][this.colorType];
    }

    public Color getHighlightColor(int level) {
        if (this.colorType == 0) {
            return EMPTY_COLOR;
        }
        return new Color(255, 255, 255);
    }

    public boolean isVariant() {
        return this.colorType == 1;
    }
}
