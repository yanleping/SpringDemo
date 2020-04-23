package main.java.springDemo.car;

/**
 * @ClassName Buick
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/23 上午10:33
 **/
public class Buick implements Car {

    @Override
    public void start() {
        System.out.println("buick start");
    }

    @Override
    public void left() {
        System.out.println("buick go left");
    }

    @Override
    public void right() {
        System.out.println("buick go right");
    }

    @Override
    public void stop() {
        System.out.println("buick stop");
    }
}
