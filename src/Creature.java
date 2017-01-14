import java.awt.*;
import java.util.MissingFormatArgumentException;

/**
 * Created by alimousa on 8/10/16.
 */
public class Creature {
    private World world;

    public int x;
    public int y;
    public int z;

    private char glyph;

    private Color color;

    private int maxHp;
    private int hp;

    private int attackValue;
    private int defenseValue;

    private Inventory inventory;

    private int visionRadius;

    private CreatureAi ai;

    private String name;


    public Creature(World world, char glyph, Color color, int maxHp, int attackValue, int defenseValue, String name) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.visionRadius = 9;
        this.name = name;
        this.inventory = new Inventory(20);
    }

    public void moveBy(int mx, int my, int mz){
        Tile tile = world.tile(x+mx, y+my, z+mz);

        if (mx==0 && my==0 && mz==0)
            return;

        if (mz == -1){
            if (tile == Tile.STAIRS_DOWN) {
                notify("walk up the stairs to level %d", z+mz+1);
            } else {
                notify("try to go up but are stopped by the cave ceiling");
                return;
            }
        } else if (mz == 1){
            if (tile == Tile.STAIRS_UP) {
                notify("walk down the stairs to level %d", z+mz+1);
            } else {
                notify("try to go down but are stopped by the cave floor");
                return;
            }
        }

        Creature other = world.creature(x+mx, y+my, z+mz);

        if (other == null)
            ai.onEnter(x+mx, y+my, z+mz, tile);
        else
            attack(other);
    }

    public void pickup(){
        Item item = world.item(x, y, z);

        if(inventory.isFull() || item == null)
            doAction("grab at the ground");
        else{
            doAction("pickup a %s", item.name());
            world.remove(x, y, z);
            inventory.add(item);
        }
    }

    public void drop(Item item){
        if(world.addAtEmptySpace(item, x, y, z)) {
            doAction("drop a " + item.name());
            inventory.remove(item);
        }
        else
            notify("There's nowhere to drop the %s", item.name());
    }

    public Creature creature(int wx, int wy, int wz){
        return world.creature(wx, wy, wz);
    }

    public void doAction(String message, Object ... params){
        int r = 9;
        
        for(int ox = -r; ox < r+1; ox++)
            for (int oy = 0; oy < r+1; oy++) {
                if(ox*ox + oy*oy > r*r)
                    continue;

                Creature other = world.creature(x+ox, y+oy, z);

                if(other == null)
                    continue;

                if(other == this)
                    other.notify("You " + message + ".", params);
                else if (other.canSee(x, y, z)){
                    try {
                        other.notify(String.format("The %s %s.", name, makeSecondPerson(message), params));
                    }catch (MissingFormatArgumentException m){
                        //
                    }
                }

            }
    }

    private String makeSecondPerson(String text){
        String[] words = text.split(" ");

        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for(String word: words){
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public void update() {
        ai.onUpdate();
    }

    public void attack(Creature other) {
        int amount = Math.max(0, this.getAttackValue() - other.getDefenseValue());

        amount = (int) (Math.random() * amount) + 1;

        other.modifyHp(-amount);

        notify("You attack the %s for %d damage.", other.name, amount);
        other.notify("The %s attacks you for %d damage.", name, amount);
    }

    public void setCreatureAi(CreatureAi ai) {
        this.ai = ai;
    }

    public void modifyHp(int amount) {
        hp += amount;

        if (hp < 1) {
            doAction("die");
            world.remove(this);
        }
    }

    public void dig(int wx, int wy, int wz) {
        world.dig(wx, wy, wz);
        doAction("dig");
    }

    public Color color() {
        return color;
    }

    public char glyph() {
        return glyph;
    }

    public boolean canEnter(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz).isGround() && world.creature(x, y, z) == null;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public int getVisionRadius(){
        return visionRadius;
    }

    public boolean canSee(int wx, int wy, int wz){
        return ai.canSee(wx, wy, wz);
    }

    public Tile tile(int wx, int wy, int wz){
        return world.tile(wx, wy, wz);
    }

    public Inventory inventory(){
        return inventory;
    }
}
