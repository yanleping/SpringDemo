package main.java.springDemo.human;

import main.java.springDemo.car.Car;

/**
 * @ClassName HumanWithCar
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/23 上午10:35
 **/
public abstract class HumanWithCar implements Human {

    protected Car car;

    public HumanWithCar(Car car){
        this.car = car;
    }

    @Override
    public abstract void gohome();
}
