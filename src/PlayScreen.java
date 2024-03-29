import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alimousa on 8/9/16.
 */
public class PlayScreen implements Screen {
    private World world;
    private Screen subscreen;
    private Creature player;
    private FieldOfView fov;
    private int screenWidth;
    private int screenHeight;
    private final int DEPTH = 5;

    private List<String> messages;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        messages = new ArrayList<String>();
        createWorld();
        fov = new FieldOfView(world);
        subscreen = null;

        StuffFactory stuffFactory = new StuffFactory(world, fov);
        createCreatures(stuffFactory);
        createItems(stuffFactory);
    }

    private void createCreatures(StuffFactory stuffFactory){
        player = stuffFactory.newPlayer(messages);
        for (int j = 0; j < DEPTH; j++) {
            for (int i = 0; i < 8; i++) {
                stuffFactory.newFungus(j);
            }
        }

        for (int j = 0; j < DEPTH; j++) {
            for (int i = 0; i < 20; i++) {
                stuffFactory.newBat(j);
            }
        }


    }

    private void createItems(StuffFactory factory) {
        for (int z = 0; z < world.depth(); z++){
            for (int i = 0; i < world.width() * world.height() / 20; i++){
                factory.newRock(z);
            }
        }
        factory.newVictoryItem(DEPTH - 1);
    }

    private void createWorld() {
        world = new WorldBuilder(90, 31, DEPTH).makeCaves().build();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages){
        int top = screenHeight - messages.size();
        for(int i = 0; i < messages.size(); i++){
            terminal.writeCenter(messages.get(i), top + i);
        }
        messages.clear();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        fov.update(player.x, player.y, player.z, player.getVisionRadius());

        for (int x = 0; x < screenWidth; x++){
            for (int y = 0; y < screenHeight; y++){
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy, player.z))
                    terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
                else
                    terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
            }
        }
        for(Creature c: world.getCreatures()){
            if((c.x >= left && c.x < left + screenWidth) && (c.y >= top && c.y < top + screenHeight) &&
                    c.z == player.z && player.canSee(c.x, c.y, c.z))
                terminal.write(c.glyph(), c.x - left, c.y - top, c.color());
        }
    }

    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();

        displayTiles(terminal, left, top);
        displayMessages(terminal, messages);
        terminal.write(player.glyph(), player.x - left, player.y - top, player.color());

        String stats = String.format(" %3d/%3d hp", player.getHp(), player.getMaxHp());
        terminal.write(stats, 1, 23);

        String depth = String.format(" Depth: %d", player.z+1);
        terminal.write(depth, 9, 23);

        if(subscreen != null)
            subscreen.displayOutput(terminal);
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight));
    }

    public Screen respondToUserInput(KeyEvent key) {
        if (subscreen != null) {
            subscreen = subscreen.respondToUserInput(key);
        } else {
            switch (key.getKeyCode()){
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_H: player.moveBy(-1, 0, 0); break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_L: player.moveBy( 1, 0, 0); break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_K: player.moveBy( 0,-1, 0); break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_J: player.moveBy( 0, 1, 0); break;
                case KeyEvent.VK_Y: player.moveBy(-1,-1, 0); break;
                case KeyEvent.VK_U: player.moveBy( 1,-1, 0); break;
                case KeyEvent.VK_B: player.moveBy(-1, 1, 0); break;
                case KeyEvent.VK_N: player.moveBy( 1, 1, 0); break;
                case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
            }

            switch (key.getKeyChar()){
                case 'g':
                case ',': player.pickup(); break;
                case 'z':
                    if (userIsTryingToExit())
                        return userExits();
                    else
                        player.moveBy( 0, 0, -1);
                    break;
                case 'q': player.moveBy( 0, 0, 1); break;
            }
        }

        if (subscreen == null)
            world.update();

        if (player.getHp() < 1)
            return new LoseScreen();

        return this;
    }

    private boolean userIsTryingToExit(){
        return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
    }

    private Screen userExits(){
        for(Item item : player.inventory().getItems()){
            if (item != null && item.glyph() == (char) 234)
                return new WinScreen();
        }
        return this;
    }
}
