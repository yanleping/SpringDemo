package test.Java.springDemo;

import main.java.springDemo.IocContainer;
import main.java.springDemo.car.Audi;
import main.java.springDemo.car.Buick;
import main.java.springDemo.human.Human;
import main.java.springDemo.human.Lisi;
import main.java.springDemo.human.Zhangsan;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName IocContainerTest
 * @Description TODO
 * @Author ledu
 * @Date 2020/4/23 下午8:01
 **/
public class IocContainerTest {

    private IocContainer iocContainer = new IocContainer();

    //所有依赖关系集中起来管理，如zhangsan和audi、lisi和buick的依赖关系
    @Before
    public void before(){
        iocContainer.setBeans(Audi.class,"audi");
        iocContainer.setBeans(Buick.class,"buick");
        iocContainer.setBeans(Zhangsan.class,"zhangsan","audi");
        iocContainer.setBeans(Lisi.class,"lisi","buick");
    }

    @Test
    public void test(){
        Human zhangsan = (Human)iocContainer.getBean("zhangsan");
        Human lisi = (Human) iocContainer.getBean("lisi");
        zhangsan.gohome();
        lisi.gohome();
    }

}
