package main.java.springDemo.car;

/**
 * @ClassName Audi
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/23 上午10:26
 **/
public class Audi implements Car{

    public void start(){
        System.out.println("audi start");
    }

    @Override
    public void left() {
        System.out.println("audi go left");
    }

    @Override
    public void right() {
        System.out.println("audi go right");
    }

    @Override
    public void stop() {
        System.out.println("audi stop");
    }
}
