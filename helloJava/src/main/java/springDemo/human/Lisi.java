package main.java.springDemo.human;

import main.java.springDemo.car.Buick;
import main.java.springDemo.car.Car;

/**
 * @ClassName Lisi
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/23 上午10:39
 **/
public class Lisi extends HumanWithCar{

    public Lisi(Car car){
        super(car);
    }

//    Car car = new Buick();

    @Override
    public void gohome() {
        car.start();
        car.stop();
    }
}
