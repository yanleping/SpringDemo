package springDemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springDemo.car.Audi;

/**
 * @ClassName MyConfiguration 注解类
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/28 下午9:54
 **/
@Configuration
@ComponentScan(value = "springDemo.car")
public class MyConfiguration {

//    @Bean
//    public Audi audi(){
//        return new Audi();
//    }
}
