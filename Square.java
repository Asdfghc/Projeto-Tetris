import java.awt.Color;

public class Square {
    private static final Color EMPTY_COLOR = Color.BLACK;

    private int colorType;
    private boolean occupied;

    private static final Color[][] colors = {
        {EMPTY_COLOR, new Color(14, 85, 222), new Color(61, 188, 252), new Color(14, 85, 222)},
        {EMPTY_COLOR, new Color(0, 171, 0), new Color(187, 249, 25), new Color(0, 171, 0)},
        {EMPTY_COLOR, new Color(217, 0, 203), new Color(250, 121, 253), new Color(217, 0, 203)},
        {EMPTY_COLOR, new Color(2, 87, 250), new Color(89, 217, 88), new Color(2, 87, 250)},
        {EMPTY_COLOR, new Color(225, 1, 88), new Color(84, 253, 151), new Color(225, 1, 88)},
        {EMPTY_COLOR, new Color(89, 247, 158), new Color(105, 134, 252), new Color(89, 247, 158)},
        {EMPTY_COLOR, new Color(243, 59, 0), new Color(125, 125, 125), new Color(243, 59, 0)},
        {EMPTY_COLOR, new Color(103, 69, 251), new Color(166, 2, 33), new Color(103, 69, 251)},
        {EMPTY_COLOR, new Color(2, 88, 246), new Color(243, 58, 4), new Color(2, 88, 246)},
        {EMPTY_COLOR, new Color(243, 59, 0), new Color(249, 162, 66), new Color(243, 59, 0)}
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
        return colors[level%10][this.colorType];
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
