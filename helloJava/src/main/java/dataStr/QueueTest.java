package main.java.dataStr;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueTest {

    public static void main(String[] args){

        //声明一个队列,长度为3
        Queue<String> msg = new ArrayBlockingQueue<String>(3);

        msg.add("dataStucture");//add 是集合 Collection 的方法

        msg.offer("dataStucture");//队列新增数据的方法

        msg.element(); //查询队首的第一个数据，不删除

        msg.poll();//查询队首的第一个数据，并删除

        msg.size();//集合 Collection 的方法：查询队列所有数据的总数
    }
}
