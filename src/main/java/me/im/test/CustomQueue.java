package me.im.test;

public class CustomQueue<T> {
    private T[] list;
    /**
     * 大小
     */
    private int limit;
    /**
     * 下标，始终指向当前队列的头元素的位置
     */
    private int front = 0;
    /**
     * 下标，始终指向当前队列的尾元素的下个位置
     */
    private int rear = 0;

    public CustomQueue() {
        this(10);
    }

    public CustomQueue(int size) {
        limit = size;
        list = (T[]) new Object[size];
    }


    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return front == rear;
    }


    /**
     * 入
     *
     * @param str
     */
    public synchronized void push(T str) throws RuntimeException {
        if (rear < limit) {
            list[rear] = str;
            rear++;
        } else {
            throw new RuntimeException("queue is full");
        }
    }

    /**
     * 出
     *
     * @return
     */
    public synchronized T pop() throws RuntimeException {
        if (front < rear) {
            front++;
            return list[front - 1];
        } else {
            throw new RuntimeException("no element in queue");
        }
    }

    public static void main(String[] args) {
        CustomQueue<String> l = new CustomQueue<String>();
        l.push("fgasdasfas");
        l.push("fsadfasd");
        l.push("zxcvbnm,,");
        System.out.println(l.pop());
        System.out.println(l.pop());
    }
}
