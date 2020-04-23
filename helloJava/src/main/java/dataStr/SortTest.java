package main.java.dataStr;

/**
 * @ClassName SortTest
 * @Description 排序算法的代码实现
 * @Author ledu
 * @Date 2020/4/19 下午10:09
 **/
public class SortTest {

    public static void main(String[] args){

        int[] a = {2,5,7,9,1,4,8,3};
//        int[] res = bubbleSort(a);
//        int[] res = selectSort(a);
        int[] res = insertSort(a);
        for(int i = 0; i<res.length;i++){
            System.out.print(res[i]+" ");
        }

    }

    //冒泡排序
    public static int[]  bubbleSort(int[] a){
        for (int i= a.length-1;i>0;i--){
            for (int j=0;j<i;j++){
                if (a[j]>a[j+1]){
                    int tmp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = tmp;
                }
            }
        }
        return a;
    }

    //选择排序
    public static int[] selectSort(int[] a){
        for(int i = 0;i<a.length;i++){
            int min = i;
            for(int j=i+1;j<a.length;j++){
                if(a[j]<a[min]){
                    min = j;
                }
            }
            if(min != i) {
                int tmp = a[min];
                a[min] = a[i];
                a[i] = tmp;
            }
        }
        return a;
    }

    //插入排序
    public static int[] insertSort(int[] a){
        if(a.length <= 1){
            return a;
        }
        for (int i=1;i < a.length;i++){
            for (int j = i;j>0;j--){
                if (a[j]<a[j-1]){
                    int tmp = a[j-1];
                    a[j-1] = a[j];
                    a[j] = tmp;
                }
            }
        }
        return a;
    }
}
