import asciiPanel.AsciiPanel;

import javax.print.DocFlavor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by alimousa on 8/16/16.
 */
public abstract class InventoryBasedScreen implements Screen {
    protected Creature player;
    private String letters;

    protected abstract String getVerb();
    protected abstract boolean isAcceptable(Item item);
    protected abstract Screen use(Item item);

    public InventoryBasedScreen(Creature player){
        this.player = player;
        this.letters = "abcdefghijklmnopqrstuvwxyz";
    }

    public void displayOutput(AsciiPanel terminal){
        ArrayList<String> lines = getList();

        int y = 23 - lines.size();
        int x = 4;

        if(lines.size() > 0)
            terminal.clear(' ', x, y , 20, lines.size());

        for(String line : lines)
            terminal.write(line, x, y++);

        terminal.clear(' ', 0, 23, 80, 1);
        terminal.write("What would you like to " + getVerb() + "? (Backspace to quit dropping)", 2 ,23);

        terminal.repaint();
    }

    public ArrayList<String> getList(){
        ArrayList<String> lines = new ArrayList<>();
        Item[] inv = player.inventory().getItems();

        for (int i = 0; i < inv.length; i++) {
            Item item = inv[i];

            if(item == null || !isAcceptable(item))
                continue;

            String line = letters.charAt(i) + "-" + item.glyph() + " " + item.name();

            lines.add(line);
        }
        return lines;
    }

    public Screen respondToUserInput(KeyEvent key){
        char c = key.getKeyChar();

        Item[] items = player.inventory().getItems();

        if(letters.indexOf(c) > -1
                && items.length > letters.indexOf(c)
                && items[letters.indexOf(c)] != null
                && isAcceptable(items[letters.indexOf(c)]))
            return use(items[letters.indexOf(c)]);
        else if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            return null;
        else
            return this;
    }
}
