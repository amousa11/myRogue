import asciiPanel.AsciiPanel;
import sun.misc.ASCIICaseInsensitiveComparator;

import java.awt.*;

/**
 * Created by alimousa on 8/9/16.
 */
public enum Tile {
    STAIRS_DOWN('Q', AsciiPanel.white),
    STAIRS_UP('Z', AsciiPanel.white),
    FLOOR((char) 250, AsciiPanel.yellow),
    WALL((char) 177, AsciiPanel.yellow),
    BOUNDS('x', AsciiPanel.brightBlack),
    UNKNOWN(' ', AsciiPanel.white);

    private char glyph;
    private Color color;

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }

    public char glyph() {
        return glyph;
    }

    public Color color() {
        return color;
    }

    public boolean isDiggable(){
        return this == WALL;
    }

    public boolean isGround(){
        return this != WALL && this != BOUNDS;
    }

}
