import com.sun.java.accessibility.util.java.awt.TextComponentTranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by alimousa on 8/12/16.
 */
public class Point {
    public int x;
    public int y;
    public int z;

    public Point(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int hashCode(){
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + x;
        result = PRIME * result + y;
        result = PRIME * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Point))
            return false;
        Point other = (Point) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    public List<Point> neighbors8(){
        List<Point> points = new ArrayList<Point>();

        for (int ox = -1; ox < 2; ox++){
            for (int oy = -1; oy < 2; oy++){
                if (ox == 0 && oy == 0)
                    continue;

                int nx = x+ox;
                int ny = y+oy;
                if(nx >= 0 && ny >= 0 && ny <= 30 && nx <= 89)
                    points.add(new Point(nx, ny, z));
            }
        }

        Collections.shuffle(points);
        return points;
    }
}
