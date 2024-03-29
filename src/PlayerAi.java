import java.util.ArrayList;
import java.util.List;

/**
 * Created by alimousa on 8/10/16.
 */
public class PlayerAi extends CreatureAi {
    private List<String> messages;
    private FieldOfView fov;

    public PlayerAi(Creature creature, List<String> messages, FieldOfView fov){
        super(creature);
        this.messages = messages;
        this.fov = fov;
    }

    @Override
    public void onEnter(int x, int y, int z, Tile tile) {
        if(tile.isGround()){
            creature.x = x;
            creature.y = y;
            creature.z = z;
        }
        else
            if(tile.isDiggable()){
            creature.dig(x, y, z);
            }
    }

    public void onNotify(String message){
        messages.add(message);
    }

    public boolean canSee(int wx, int wy, int wz) {
        return fov.isVisible(wx, wy, wz);
    }
}
