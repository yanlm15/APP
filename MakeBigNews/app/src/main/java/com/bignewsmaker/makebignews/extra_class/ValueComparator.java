package com.bignewsmaker.makebignews.extra_class;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liminyan on 11/09/2017.
 */
public class ValueComparator implements Comparator<String> {

    Map<String, Long> base;
    //这里需要将要比较的map集合传进来
    public ValueComparator(Map<String, Long> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    //比较的时候，传入的两个参数应该是map的两个key，根据上面传入的要比较的集合base，可以获取到key对应的value，然后按照value进行比较
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}