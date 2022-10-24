package com.xzzz.B2_algorithm.递归;

/**
 * 将链表 1 > 2 > 3 > 4
 * 转换为 4 > 3 > 2 > 1
 */
public class 反转单链表 {

    static class Node{
        int date;
        Node next;

        public Node(int date,Node next){
            this.date = date;
            this.next = next;
        }
    }

    static Node reverseList(Node head){
        if(head == null || head.next == null){
            return head;
        }

        Node newList = reverseList(head.next);
        System.out.println(head.date);
        System.out.println(head.next.date);
        Node t1 = head.next;
        t1.next = head;
        head.next = null;
        return newList;
    }

    public static void main(String[] args) {
        Node node4 = new Node(4,null);
        Node node3 = new Node(3,node4);
        Node node2 = new Node(2,node3);
        Node nodehead = new Node(1,node2);
        Node newNode = reverseList(nodehead);
    }
}
