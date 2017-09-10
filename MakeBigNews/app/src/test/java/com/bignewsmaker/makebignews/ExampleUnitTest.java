package com.bignewsmaker.makebignews;

import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.extra_class.ValueComparator;

import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void Logic_isCorrect() throws Exception{

//        String a[] = new String[]{"d","c","b","a"};
        String a ="dcba";

        ConstData.getInstance().setLike1("a");
        ConstData.getInstance().setLike("a");

        ConstData.getInstance().setLike1("b");
        ConstData.getInstance().setLike("b");
        ConstData.getInstance().setLike1("b");
        ConstData.getInstance().setLike("b");

        ConstData.getInstance().setLike1("c");
        ConstData.getInstance().setLike("c");
        ConstData.getInstance().setLike1("c");
        ConstData.getInstance().setLike("c");
        ConstData.getInstance().setLike1("c");
        ConstData.getInstance().setLike("c");

        ConstData.getInstance().setLike1("d");
        ConstData.getInstance().setLike("d");
        ConstData.getInstance().setLike1("d");
        ConstData.getInstance().setLike("d");
        ConstData.getInstance().setLike1("d");
        ConstData.getInstance().setLike("d");
        ConstData.getInstance().setLike1("d");
        ConstData.getInstance().setLike("d");

        String b="";
        int i = 0;
        for (String e:ConstData.getInstance().getLike().keySet()){
            i++;
            b+= e;
        }


        assertEquals(a.equals(b),true);

    }
}