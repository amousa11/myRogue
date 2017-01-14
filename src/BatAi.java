/**
 * Created by alimousa on 8/15/16.
 */
public class BatAi extends CreatureAi {

    public BatAi(Creature creature){
        super(creature);
    }

    public void onUpdate(){
        wander();
        wander();
    }
}
