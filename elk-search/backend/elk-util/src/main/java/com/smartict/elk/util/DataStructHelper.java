/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by burak.sahin 03.04.2018
 */
public class DataStructHelper {

    public static <T> List<T> union(List<T> list1, List<T> list2, List<T> list3) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);
        set.addAll(list3);
        return new ArrayList<T>(set);
    }

    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static <T> List<T> unionAll(List<List<T>> lists) {
        Set<T> set = new HashSet<T>();
        for (List<T> list : lists) {
            set.addAll(list);
        }
        return new ArrayList<T>(set);
    }
}
