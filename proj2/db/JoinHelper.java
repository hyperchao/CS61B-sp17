package db;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class JoinHelper {
    public final List<Integer> selfList, otherList;
    public final Set<Integer> selfSet, otherSet;
    public boolean mutual;

    public JoinHelper() {
        selfList = new ArrayList<>();
        otherList = new ArrayList<>();
        selfSet = new HashSet<>();
        otherSet = new HashSet<>();
    }
}
