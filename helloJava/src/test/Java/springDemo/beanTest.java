package springDemo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springDemo.car.Audi;
import springDemo.car.Buick;

/**
 * @ClassName beanTest
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/25 下午2:41
 **/
public class beanTest {

    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Audi audi = context.getBean("audi", Audi.class);
        System.out.println("audi="+audi);

        Buick buick = context.getBean("buick",Buick.class);
        System.out.println("buick="+buick);
    }
}
