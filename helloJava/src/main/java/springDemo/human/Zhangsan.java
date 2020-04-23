package main.java.springDemo.human;

import main.java.springDemo.car.Audi;
import main.java.springDemo.car.Car;

/**
 * @ClassName Zhangsan
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/23 上午10:37
 **/
public class Zhangsan extends HumanWithCar {

//    Car car = new Audi();

    public Zhangsan(Car car){
        super(car);
    }

    @Override
    public void gohome() {
        car.start();
        car.left();
        car.right();
        car.stop();
    }
}
