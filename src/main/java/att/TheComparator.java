package att;

import java.util.Comparator;

public class TheComparator implements Comparator<Object[]> {
    public int compare(Object[] o1, Object[] o2) {
        return (int) ((Long) o2[1] - (Long) o1[1]);
    }
}