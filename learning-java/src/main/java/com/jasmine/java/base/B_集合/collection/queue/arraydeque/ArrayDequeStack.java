package com.jasmine.java.base.B_集合.collection.queue.arraydeque;


import java.util.ArrayDeque;

/**
 * 做为栈来使用
 * LIFO（后进先出）
 */
public class ArrayDequeStack
{
    public static void main(String[] args)
    {
        ArrayDeque<String> stack = new ArrayDeque<>();
        // 依次将三个元素push入"栈"
        stack.push("疯狂Java讲义");
        stack.push("轻量级Java EE企业应用实战");
        stack.push("疯狂Android讲义");
        // 输出：[疯狂Android讲义, 轻量级Java EE企业应用实战, 疯狂Java讲义]
        System.out.println(stack);
        // 访问第一个元素，但并不将其pop出"栈"，输出：疯狂Android讲义
        //peek：获取队列头部的元素,不删除
        System.out.println(stack.peek());
        // 依然输出：[疯狂Android讲义, 疯狂Java讲义, 轻量级Java EE企业应用实战]
        System.out.println(stack);
        // pop(栈方法),pop出双该双端队列所表示的栈的栈顶元素，相当于removeFirst()，删除
        System.out.println(stack.pop());
        // 输出：[轻量级Java EE企业应用实战, 疯狂Java讲义]
        System.out.println(stack);
    }
}

