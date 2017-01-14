import asciiPanel.AsciiPanel;

import java.util.List;

/**
 * Created by alimousa on 8/10/16.
 */
public class StuffFactory {
    private World world;
    private FieldOfView fov;

    public StuffFactory(World world, FieldOfView fov) {
        this.world = world;
        this.fov = fov;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 100, 20, 5, "You");
        world.addAtEmptyLocation(player, player.z);
        new PlayerAi(player, messages,fov);
        return player;
    }

    public Creature newFungus(int z) {
        Creature fungus = new Creature(world, 'f', AsciiPanel.green, 10, 0, 0, "Fungus");
        world.addAtEmptyLocation(fungus, z);
        new FungusAi(fungus, this);
        return fungus;
    }

    public Creature newBat(int z){
        Creature bat = new Creature(world, 'b', AsciiPanel.yellow, 15, 5, 0, "Bat");
        world.addAtEmptyLocation(bat, z);
        new BatAi(bat);
        return bat;
    }

    public Item newRock(int depth){
        Item rock = new Item(',', AsciiPanel.yellow, "rock");
        world.addAtEmptyLocation(rock, depth);
        return rock;
    }

    public Item newVictoryItem(int depth){
        Item item = new Item((char) 234, AsciiPanel.brightWhite, "Scroll of CPF");
        world.addAtEmptyLocation(item, depth);
        return item;
    }


}
