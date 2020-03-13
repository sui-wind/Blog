package com.tlc.blog;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hello");
    }



    public static void main(String[] args) {

        /*ArrayList<String> list = new ArrayList<String>();
        for(int i =0;i<1000;i++){
            list.add("sh"+i);
        }

        for(int i= 0;list.iterator().hasNext();i++) {
            list.remove(i);
            System.out.println("秘密" + list.get(i));
        }*/

        /*List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        for (String item : list) {
            // 当为1时运行正常，改成2时异常ConcurrentModificationException
            if ("2".equals(item)) {
                list.remove(item);
            }
        }*/
    }
}
