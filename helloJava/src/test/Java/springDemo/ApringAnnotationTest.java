package springDemo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springDemo.car.Audi;

/**
 * @ClassName ApringAnnotationTest
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/28 下午9:57
 **/
public class ApringAnnotationTest {

    @Test
    public void test(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfiguration.class);

        Audi audi = context.getBean("audi",Audi.class);
    }
}
