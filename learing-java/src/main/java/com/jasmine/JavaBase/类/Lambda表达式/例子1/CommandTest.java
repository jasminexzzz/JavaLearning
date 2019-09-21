package com.jasmine.JavaBase.类.Lambda表达式.例子1;

public class CommandTest {


    public static void main(String[] args) {
        ProcessArray pa = new ProcessArray();
        int[] target = {3,-4,6,4};
        /*pa.process(target, new Command() {
            @Override
            public void process(int[] target) {
                int sum = 0;
                for(int tmp : target){
                    sum += tmp;
                }
                System.out.println("数组元素的总和是:"+ sum);
            }
        });*/

        pa.process(target,(int [] array)->{
            int sum = 0;
            for (int tmp : array){
                sum += tmp;
            }
            System.out.println("数组元素的总和是:"+ sum);
        });
    }
}
