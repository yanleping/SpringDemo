package main.java.springDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName IocContainer
 * @Description 实现一个简单的Spring IOC容器
 *              1、实例化bean
 *              2、保存bean
 *              3、提供bean
 *              4、每一个bean要产生一个唯一的ID与之对应
 * @Author ledu
 * @Date 2020/4/23 上午10:48
 **/
public class IocContainer {

//    使用ConcurrentHashMap 实例化，是因为线程安全
    private Map<String,Object> beans = new ConcurrentHashMap<String, Object>();
    
    /** 
     * @Description:  获取一个bean
     * @Param: [beanId] 
     * @Return: java.lang.Object 返回bean
     * @Author: leping
     * @Date: 2020/4/23 上午11:40
     */ 
    public Object getBean(String beanId){
        return beans.get(beanId);
    }

    /** 
     * @Description: 委托IOC容创建一个bean
     * @Param: calzz:要创建bean的class;
     *         paramBeanIds:要创建的bean的class的构造方法所需参数需要的beanId们，也就是要创建的对象所依赖的对象
     * @Return: void 
     * @Author: leping
     * @Date: 2020/4/23 下午12:02
     */ 
    public void setBeans(Class<?> calzz, String beanId,String... paramBeanIds){

        //1、组装构造方法所需要的bean
        Object[] paramValues = new Object[paramBeanIds.length];
        for (int i=0;i<paramBeanIds.length;i++){
            paramValues[i] = beans.get(paramBeanIds[i]);

        }
        //2、调用构造方法，实例化bean
        Object bean = null;
        for (Constructor<?> constructor : calzz.getConstructors()){
            try {
                bean = constructor.newInstance(paramValues);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (bean == null){
                throw new RuntimeException("找不到合适的方法实例化bean");
            }
        }
        //3、将实例化的bean放入beans
        beans.put(beanId,bean);
    }
}
