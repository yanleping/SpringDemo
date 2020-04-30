package springDemo.car;

/**
 * @ClassName CarFactory
 * @Description
 * @Author ledu
 * @Date 2020/4/25 下午11:06
 **/
public class CarFactory {

    /**
     * @Description: 使用spring实例化bean的方法2：通过静态方法实例化bean
     * @Param:
     * @Return:
     * @Author: leping
     * @Date: 2020/4/25 下午11:20
     */
    public static Buick getBuick(){
        return new Buick();
    }

    /**
     * @Description: 使用spring实例化bean的方法2：通过实例化方法实例化bean
     * @Param:
     * @Return:
     * @Author: leping
     * @Date: 2020/4/25 下午11:21
     */
    public Audi getAudi(){
        return new Audi();
    }
}
