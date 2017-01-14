/**
 * Created by alimousa on 8/10/16.
 */
public class CreatureAi {
    protected Creature creature;

    public CreatureAi(Creature creature){
        this.creature = creature;
        this.creature.setCreatureAi(this);
    }

    public void onEnter(int x, int y, int z, Tile tile){
        if(tile.isGround()){
            creature.x = x;
            creature.y = y;
            creature.z = z;
        }
        else
            creature.doAction("bump into a wall");
    }

    public void onUpdate(){
    }

    public void onNotify(String message){
    }

    public void wander(){
        int mx = (int)(Math.random() * 3) - 1;
        int my = (int)(Math.random() * 3) - 1;

        Creature other = creature.creature(my, my, creature.z);

        if(other != null && other.glyph() == creature.glyph() && creature.tile(mx, my, creature.z).isGround())
            return;
        else
            creature.moveBy(mx, my, 0);
    }

    public boolean canSee(int wx, int wy, int wz) {
        if (creature.z != wz)
            return false;

        if ((creature.x-wx)*(creature.x-wx) + (creature.y-wy)*(creature.y-wy) > creature.getVisionRadius()*creature.getVisionRadius())
            return false;

        for (Point p : new Line(creature.x, creature.y, wx, wy).getPoints()){
            if (creature.tile(p.x, p.y, wz).isGround() || p.x == wx && p.y == wy)
                continue;

            return false;
        }

        return true;
    }
}
