import java.awt.*;

/**
 * Created by alimousa on 8/16/16.
 */
public class Item {

    private char glyph;
    private Color color;

    private String name;

    public Item(char glyph, Color color, String name){
        this.name = name;
        this.glyph = glyph;
        this.color = color;
    }

    public String name(){
        return name;
    }

    public Color color(){
        return color;
    }

    public char glyph(){
        return glyph;
    }
}
