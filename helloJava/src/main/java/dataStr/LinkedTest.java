package main.java.dataStr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedTest {

    public static void main(String[] args){

        //数组实现
        List<String> array = new ArrayList<String>();

        //链表实现
        List<String> linded = new LinkedList<String>();

        //新增数据:比较数组实现和链表实现的性能差别，linkedList 新增数据的性能比 ArrayList 好
        Long startTime = System.nanoTime();
        for(int i = 0; i<100000;i++){
            array.add("i");//6171429
        }
        Long endTime = System.nanoTime();
        System.out.println("array:" + (endTime - startTime));

        Long startTime2 = System.nanoTime();
        for(int i = 0; i<100000;i++){
            linded.add("i");//5218831
        }
        Long endTime2 = System.nanoTime();
        System.out.println("Linked:" + (endTime2 - startTime2));

    }
}
