package com.jasmine.JavaBase.B_集合;

@SuppressWarnings("all")
public class 概念 {

    /**


    ┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |         ──────																															 |																															 |
    |       /        \                                                                                                                           |
    |       |                 ──────        |        |           ──────          ──────           |        0        ──────       |──\      |     |
    |       |               /        \      |        |         /        \      /        \     ────┼────    |      /        \     |   \     |     |
    |       |              |          |     |        |        |          |    |                   |        |     |          |    |    \    |     |
    |       |              |          |     |        |        |──────────┘    |                   |        |     |          |    |     \   |     |
    |       |              |          |     |        |        |               |                   |        |     |          |    |      \  |     |
    |       \________/      \________/      \__/     \__/      \________/      \________/         \__/     |      \________/     |       \_|     |
    └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

    Iterator (interface) : 迭代器
    |
    |  除Map类的所有集合的顶层接口
    |  包含:
    |      hashNext()            :判断集合中元素是否遍历完毕，如果没有，就返回true。
    |      next()                :返回迭代器刚越过的元素的引用，返回值是 Object，需要强制转换成自己需要的类型.
    |      remove()              :删除迭代器刚越过的元素
    |      forEachRemaining()    :
    |
    └── Iterable (interface) : 可迭代的
        |
        |  Iterable中创建了一个Iterator 所有实现此接口的类均可使用Iterator迭代器
        |      forEach()     : 循环输出Iterable
        |      spliterator() :
        |
        └── Collection extends Iterable (interface) : 集合容器
            |
            |  所有集合实现类的接口
            |  包含:
            |      boolean add(Object o)             : 添加对象到集合.
            |      boolean remove(Object o)          : 删除指定的对象.
            |          int size()                    : 返回当前集合中元素的数量.
            |      boolean contains(Object o)        : 查找集合中是否有指定的对象.
            |      boolean isEmpty()                 : 判断集合是否为空.
            |     Iterator iterator()                : 返回一个迭代器.
            |      boolean containsAll(Collection c) : 查找集合中是否有集合c中的元素.
            |      boolean addAll(Collection c)      : 将集合c中所有的元素添加给该集合.
            |         void clear()                   : 删除集合中所有元素.
            |         void removeAll(Collection c)   : 从集合中删除c集合中也有的元素.
            |         void retainAll(Collection c)   : 从集合中删除集合c中不包含的元素.
            |
            |
            ├── abstract class AbstractCollection<E> implements Collection<E> : 只有iterator()方法和size()没有实现,其他都已实现
            |   |
            |   |
            |   └── abstract class AbstractList<E> extends AbstractCollection<E> implements list<E>
            |
            |           1. 重写了euqals()和hashCode()
            |
            |               public boolean equals(Object o) {
            |                   if (o == this)
            |                       return true;
            |                   //o是否是this的实例
            |                   if (!(o instanceof list))
            |                       return false;
            |
            |                   // 创建迭代器
            |                   ListIterator<E> e1 = listIterator();
            |                   ListIterator<?> e2 = ((list<?>) o).listIterator();
            |
            |                   // 在两者都有元素的情况下，比较两者元素是否相等
            |                   while (e1.hasNext() && e2.hasNext()) {
            |                       E o1 = e1.next();
            |                       Object o2 = e2.next();
            |
            |                       // 这里的比较规则如下：
            |                       // 1. 两者元素其中一个为 null, 但另一个不为 null, 则返回 false
            |                       // 2. 其中一个元素不为 null, 但两者使用 equals() 方法比较为 false, 则返回 false
            |                       if (!(o1==null ? o2==null : o1.equals(o2)))
            |                           return false;
            |                   }
            |                   // 集合元素变量完之后，且循环体没有返回 flase, 则 如果任意一个集合还有元素则返回 false
            |                   return !(e1.hasNext() || e2.hasNext());
            |               }
            |
            |
            |               public int hashCode() {
            |                   int hashCode = 1;
            |                   for (E e : this)
            |                       hashCode = 31 * hashCode + (e==null ? 0 : e.hashCode());
            |                   return hashCode;
            |               }
            |
            |           2. 继承该抽象类的类
            |           @see java.util.ArrayList
            |           @see java.util.Vector
            |           @see java.util.AbstractSequentialList
            |                @see java.util.linkedlist
            |
            |
            |
            |
            |
            ├── list extends Collection (interface) : List集合的接口
            |   |
            |   |  List里存放的对象是有序的，同时也是可以重复的.
            |   |  包含:
            |   |         void add(int index,Object element)  : 在指定位置上添加一个对象.
            |   |      boolean addAll(int index,Collection c) : 将集合c的元素添加到指定的位置.
            |   |       Object get(int index)                 : 返回List中指定位置的元素.
            |   |          int indexOf(Object o)              : 返回第一个出现元素o的位置.
            |   |       Object remove(int index)              : 删除指定位置的元素.
            |   |       Object set(int index,Object element)  : 用元素element取代位置index上的元素,返回被取代的元素.
            |   |         void sort()                         ：使用提供的 Comparator 对此列表进行排序以比较元素.
            |   |      boolean contains(Object o)             : 集合中是否包含该元素,返回true或者false.
            |   |      boolean isEmpty()                      : 集合是否为空的,没有包含任何元素,而不是引用等于null.
            |   |     Object[] toArray()                      : 将集合转换为数组,无法强转,如 String[] = (String) list.toArray();因为Java强制类型转换是针对单个对象的.
            |   |      <T> T[] toArray(T[] a)                 : 将集合转换为指定类型的数组,如 String[] = (String) list.toArray(new String[list.seize()]).
            |   |
            |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
            |   |                                                             ArrayList                                                              |
            |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
            |   |  ArrayList<E> extends AbstractList<E> implements list<E>
            |   |                                                  RandomAccess : 只要List集合实现这个接口，就能支持快速随机访问.
            |   |                                                  Cloneable : 用于指明被创建的一个允许对对象进行位复制（也就是对象副本）的类.
            |   |                                                  Java.io.Serializable : 允许进行实例化.
            |   |
            |   |  底层数据结构是数组，查询快，增删慢;线程不安全，效率高
            |   |  实现线程安全的方法 : list list = Collections.synchronizedList(new ArrayList());
            |   |
            |   |  1.构造实现方法
            |   |    1). new ArrayList() 无参构造方法
            |   |        Object[] = {};
            |   |    2). new ArrayList(int initialCapacity) 构造具有指定初始容量的空列表。
            |   |        if(initialCapacity > 0)
            |   |           Object[] = new Object[initialCapacity]
            |   |        if(initialCapacity = 0)
            |   |           等同于ArrayList()
            |   |        else 抛出参数异常错误 : throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
            |   |    3). new ArrayList(Collection<? extends E> c)  构造一个包含指定集合的元素的列表，按照它们由集合的迭代器返回的顺序。
            |   |
            |   |
            |   |
            |   |  2.添加方法
            |   |    1). add(E e) : 方法实现,由于需要copyOf数组,所以增删很慢,但其实linkedlist在中间插入数据也很慢只在头尾插入数据较快
            |   |
            |   |        public boolean add(E e) {
            |   |            ensureCapacityInternal(size + 1);  // Increments modCount!! 将当前数组内元素个数 + 1
            |   |            elementData[size++] = e;//此处的elementData已经是重新分配了长度的数组,然后将添加的元素插入到size++的位置
            |   |            return true;
            |   |        }
            |   |
            |   |        @minCapacity 数组长度 + 1
            |   |        private void ensureCapacityInternal(int minCapacity) {
            |   |            ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
            |   |        }
            |   |
            |   |        @elementData 当前数组
            |   |        @minCapacity 当前数组内元素个数 + 1
            |   |        //用于返回数组的最短长度,小于默认范围默认,大于默认返回自身
            |   |        private static int calculateCapacity(Object[] elementData, int minCapacity) {
            |   |            //如果是默认判断当前数组是否为默认数组数组,则判断当前长度是否大于ArrayList默认长度10,将较大的数设为数组的最小容量
            |   |            if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            |   |                return Math.max(DEFAULT_CAPACITY, minCapacity);
            |   |            }
            |   |            return minCapacity;
            |   |        }
            |   |
            |   |        @minCapacity 当前数组内元素个数 + 1
            |   |        private void ensureExplicitCapacity(int minCapacity) {
            |   |            modCount++;
            |   |            //如果数组内元素的个数大于数组当前的长度,也就是说数组已经满了,就重新给数组赋值
            |   |            if (minCapacity - elementData.length > 0)
            |   |                grow(minCapacity);
            |   |        }
            |   |
            |   |        //该方法其实是变更容量的方法
            |   |        //变更数组的长度 = 原来的长度 + 原来长度的一半
            |   |        //按照新的长度copy一套并且赋给自己
            |   |        private void grow(int minCapacity) {
            |   |            // overflow-conscious code
            |   |            int oldCapacity = elementData.length;               //旧容量  =  数组长度
            |   |            int newCapacity = oldCapacity + (oldCapacity >> 1); //新的容量 = 旧容量 + 旧容量的一半(oldCapacity >> 1 取oldCapacity的1/2)
            |   |            if (newCapacity - minCapacity < 0)                  //如果新容量 < 当前数组的最小容量 则新容量 = 当前数组的最小容量
            |   |                newCapacity = minCapacity;
            |   |            if (newCapacity - MAX_ARRAY_SIZE > 0)               //如果新容量 > Integer.MAX_VALUE - 8 (数组作为一个对象，需要一定的内存存储对象头信息，数组的对象头信息相较于其他Object，多了一个表示数组长度的信息,这个信息的长度为8byte)
            |   |                newCapacity = hugeCapacity(minCapacity);        //
            |   |            // minCapacity is usually close to size, so this is a win:
            |   |            elementData = Arrays.copyOf(elementData, newCapacity);//将自己复制一套并变更容量然后赋值给自己,确保对象不会改变,此动作是数组插入数据慢的原因
            |   |        }
            |   |
            |   |    2).add(int index, E element) : 在指定位置添加
            |   |
            |   |       @index 插入位置
            |   |       public void add(int index, E element) {
            |   |           rangeCheckForAdd(index);//校验插入位置,需要小于数组长度且大于0
            |   |           ensureCapacityInternal(size + 1);  // Increments modCount!!
            |   |           //将elementData从Index位置复制到elementData的index + 1位置,复制的长度为 size - index
            |   |           System.arraycopy(elementData, index, elementData, index + 1,size - index);
            |   |           //在index位置插入元素element
            |   |           elementData[index] = element;
            |   |           size++;
            |   |       }
            |   |
            |   |  3. get
            |   |       return (E) elementData[index];
            |   |       直接从数组中返回
            |   |
            |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
            |   |                                                             linkedlist                                                             |
            |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
            |   |  public class linkedlist<E> extends AbstractSequentialList<E> implements list<E>
            |   |                                                                          Deque<E>
            |   |                                                                          Cloneable
            |   |                                                                          java.io.Serializable
            |   |  底层数据结构是双向链表，查询慢，增删快;线程不安全，效率高
            |   |  备注:增删快只是针对插入头尾的效率高,插入中间的效率其实和查询的效率差不多,都会根据size >> 1来遍历半个链表获取指定位置的节点
            |   |  实现线程安全的方法 : list list = Collections.synchronizedList(new linkedlist());
            |   |
            |   |  1. 构造方法实现
            |   |     LinkedList类包含成员变量:
            |   |            transient int size = 0;  //链表节点个数.
            |   |            transient Node<E> first; //指向链表头部.
            |   |            transient Node<E> last;  //指向链表尾部.
            |   |
            |   |     1. 一个无参的构造方法
            |   |         public linkedlist() {
            |   |         }
            |   |
            |   |     2. 构造一个包含指定集合的元素的列表，按照它们由集合的迭代器返回的顺序。
            |   |         public linkedlist(Collection<? extends E> c) {
            |   |             this();
            |   |             addAll(c);//向集合中新增节点
            |   |         }
            |   |
            |   |  2. Node结点:Node类LinkedList的静态内部类。
            |   |        private static class Node<E> {
            |   |           E item;        //自己.
            |   |           Node<E> next;  //下一个节点.
            |   |           Node<E> prev;  //上一个节点.
            |   |
            |   |           Node(Node<E> prev, E element, Node<E> next) {
            |   |               this.item = element;
            |   |               this.next = next;
            |   |               this.prev = prev;
            |   |           }
            |   |       }
            |   |
            |   |  3. 添加方法
            |   |     1). add(E e) : 将指定的节点追加到此列表的末尾.
            |   |
            |   |           public boolean add(E e) {
            |   |               linkLast(e);
            |   |               return true;
            |   |           }
            |   |
            |   |           void linkLast(E e) {
            |   |               final Node<E> l = last;//获取最后一个节点
            |   |               final Node<E> newNode = new Node<>(l, e, null);//新建节点,l:最后一个; e:本节点; null:下个节点为空因为是添加到末尾
            |   |               last = newNode;//本节点添加到链表末尾后,本节点成为最后一个节点
            |   |               //如果没有最后节点,说明链表为空,也就是说添加进的节点为第一个节点,设为first
            |   |               if (l == null)
            |   |                   first = newNode;
            |   |               //否则原来的最后节点的下一个节点为当前添加的节点
            |   |               else
            |   |                   l.next = newNode;
            |   |               size++;
            |   |               modCount++;
            |   |           }
            |   |
            |   |     2). add(int index,E e) : 用于在指定位置添加元素,元素从0开始.
            |   |
            |   |           public void add(int index, E element) {
            |   |               checkPositionIndex(index);
            |   |               //如果添加节点的位置等于链表的个数,则添加到最后
            |   |               if (index == size)
            |   |                   linkLast(element);
            |   |               //否则添加到中间
            |   |               else
            |   |                   //添加前先调用node方法构造一个node类
            |   |                   linkBefore(element, node(index));
            |   |           }
            |   |
            |   |
                            //获取指定位置的节点
            |   |           Node<E> node(int index) {
            |   |               //判断要插入的位置是在链表的前半部还是后半部,然后循环寻找位置,这个就很耗时,感觉比arraylist插入还要慢
            |   |               //在前半部,从头开始遍历
            |   |               if (index < (size >> 1)) {
            |   |                   //节点为第一个
            |   |                   Node<E> x = first;
            |   |                   for (int i = 0; i < index; i++)
            |   |                       x = x.next;
            |   |                   return x;
            |   |               //在后半部,从尾开始遍历
            |   |               } else {
            |   |                   //节点为最后一个
            |   |                   Node<E> x = last;
            |   |                   //从最后一个-1开始遍历,因为如果index = size,则调用linkLast
            |   |                   for (int i = size - 1; i > index; i--)
            |   |                       //直到遍历到x为原来index位置所在的节点
            |   |                       x = x.prev;
            |   |                   return x;
            |   |               }
            |   |           }
            |   |
            |   |           @e    插入的元素
            |   |           @succ 当前节点
            |   |           void linkBefore(E e, Node<E> succ) {
            |   |               //原来节点的上一个节点
            |   |               final Node<E> pred = succ.prev;
            |   |               //创建插入的节点
            |   |               final Node<E> newNode = new Node<>(pred, e, succ);
            |   |               //当前节点的上一个节点为新插入的节点
            |   |               succ.prev = newNode;
            |   |               //如果上一个节点为空,则插入节点为头节点
            |   |               if (pred == null)
            |   |                   first = newNode;
            |   |               //否则头节点的下一个节点为自己
            |   |               else
            |   |                   pred.next = newNode;
            |   |               size++;
            |   |               modCount++;
            |   |           }
            |   |
            |   |
            |   |  4. 删除方法
            |   |     1). remove(Object o) : 从列表中删除指定元素的第一个出现（如果存在）。
            |   |           public boolean remove(Object o) {
            |   |               if (o == null) {
                                    //查找第一个个item为空的元素,从头开始查找
            |   |                   for (Node<E> x = first; x != null; x = x.next) {
            |   |                       if (x.item == null) {
            |   |                           unlink(x);
            |   |                           return true;
            |   |                       }
            |   |                   }
            |   |               } else {
                                    //查找第一个个item为o的元素,从头开始查找
            |   |                   for (Node<E> x = first; x != null; x = x.next) {o
            |   |                       if (o.equals(x.item)) {
            |   |                           unlink(x);
            |   |                           return true;
            |   |                       }
            |   |                   }
            |   |               }
            |   |               return false;
            |   |           }
            |   |
            |   |
            |   |           E unlink(Node<E> x) {
            |   |               // assert x != null;
            |   |               final E element = x.item;
            |   |               final Node<E> next = x.next;
            |   |               final Node<E> prev = x.prev;
            |   |
            |   |               //如果本节点没有上节点,则下节点变为头节点
            |   |               if (prev == null) {
            |   |                   first = next;
            |   |               } else {
            |   |               //否则上节点的下节点变为本节点的下节点,本节点的上节点为空
            |   |               //也就是说把自己剔除了
            |   |                   prev.next = next;
            |   |                   x.prev = null;
            |   |               }
            |   |
            |   |               //如果下节点为空,则上节点为尾节点
            |   |               if (next == null) {
            |   |                   last = prev;
            |   |               } else {
            |   |               //否则下节点的上节点为本节点的上节点,本节点的下节点为空
            |   |                   next.prev = prev;
            |   |                   x.next = null;
            |   |               }
            |   |               //本节点内容设置为空,链表长度-1
            |   |               x.item = null;
            |   |               size--;
            |   |               modCount++;
            |   |               return element;
            |   |           }
            |   |
            |   |  5. get(int index)
            |   |       public E get(int index) {
            |   |           checkElementIndex(index);
            |   |           return node(index).item;
            |   |       }
            |   |
            |   |       //检查元素,必须大于0,小于当前元素数量
            |   |       private void checkElementIndex(int index) {
            |   |           if (!isElementIndex(index))
            |   |               throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
            |   |       }
            |   |
            |   |       //先判断index在链表的前半部还是后半部
            |   |       //从前或从后开始遍历,直到遍历到index位置,然后返回,效率慢啊这个遍历
            |   |       Node<E> node(int index) {
            |   |           if (index < (size >> 1)) {
            |   |               Node<E> x = first;
            |   |               for (int i = 0; i < index; i++)
            |   |                   x = x.next;
            |   |               return x;
            |   |           } else {
            |   |               Node<E> x = last;
            |   |               for (int i = size - 1; i > index; i--)
            |   |                   x = x.prev;
            |   |               return x;
            |   |           }
            |   |       }
            |   |
            |   |
            |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
            |   |                                                                Vector                                                              |
            |   └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
            |       虽然是线程安全的,但java不建议使用,因为Vector与ArrayList基本一致
            |       不同点:
            |           1.Vector是线程安全的
            |           2.ArrayList增长量默认是0.5倍；而Vector的增长量capacityIncrement是可以配置的，如果未指定（<=0），则大小增长一倍.
            |
            |
            ├── Set<E> extends Collection<E>    (interface) : Set集合的接口
            |   |
            |   |
            |   └── AbstractSet<E> extends AbstractCollection<E> implements Set<E> (抽象类) :
            |       |
            |       |   需要注意:AbstractSet重写了Object的equels()和hashCode()两个方法
            |       |   Object:
            |       |           public boolean equals(Object obj) {
            |       |               return (this == obj);
            |       |           }
            |       |
            |       |           //使用native关键字说明这个方法是原生函数，也就是这个方法是用C/C++语言实现的，并且被编译成了DLL，由java去调用。
            |       |           public native int hashCode();
            |       |
            |       |   重写后:
            |       |           public boolean equals(Object o) {
            |       |
            |       |               //如果自己和自己比较
            |       |               if (o == this)
            |       |                   return true;
            |       |
            |       |               //如果不是Set的实现类则
            |       |               if (!(o instanceof Set))
            |       |                   return false;
            |       |
            |       |               //如果长度size不同
            |       |               Collection<?> c = (Collection<?>) o;
            |       |               if (c.size() != size())
            |       |                   return false;
            |       |               try {
            |       |                   //AbstractCollection.containsAll();
            |       |                   //如果此集合包含指定集合中的所有元素，则返回true。
            |       |                   return containsAll(c);
            |       |               } catch (ClassCastException unused)   {
            |       |                   return false;
            |       |               } catch (NullPointerException unused) {
            |       |                   return false;
            |       |               }
            |       |           }
            |       |
            |       |           //Set的hashCode值是由集合中每一个元素的hashCode值相加
            |       |           public int hashCode() {
            |       |               int h = 0;
            |       |               Iterator<E> i = iterator();
            |       |               while (i.hasNext()) {
            |       |                   E obj = i.next();
            |       |                   if (obj != null)
            |       |                       h += obj.hashCode();
            |       |               }
            |       |               return h;
            |       |           }
            |       |
            |       |
            |       ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
            |       |                                                            hashSet                                                        |
            |       ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
            |       |   HashSet
            |       |   不能保证元素的顺序,不可重复,线程不安全,集合元素可以为null
            |       |
            |       |   HashSet实际就是一个HashMap,只不过其中的key-value的value是一个静态属性PRESENT.
            |       |   由于hashmap的key不能重复,即可实现hashset中元素不可重复的功能.
            |       |   既然要保证元素不可重复,那么就要保证每一个放入hashset表中的对象都要提供hashcode和equals方法的实现.如果两个对象equals相同,hashcode也
            |       |   应该相同.
            |       |
            |       |   1. 构造方法
            |       |       //实际就是一个hashMap,所有的增删改查和hashset一样
            |       |       public HashSet() {
            |       |           map = new HashMap<>();
            |       |       }
            |       |
            |       |   2. 插入
            |       |       调用的就是hashmao的put方法
            |       ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
            |       |                                                          linkedhashset                                                    |
            |       ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
            |       ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
            |       |                                                             treeset                                                       |
            |       ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
            └── Queue<E> extends Collection<E>  (interface) : Queue队列的接口






    ┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                            ┌─             ─┐                                                                               |
    |                                            |  \          / |                                                                               |
    |                                            |   \        /  |         ──────            ┌────────\                                          |
    |                                            |    \      /   |        /        \         |        |                                          |
    |                                            |     \    /    |       |          |        |________/                                          |
    |                                            |      \  /     |       |          |        |                                                   |
    |                                            |       \/      |       |          |        |                                                   |
    |                                            |               |        \________/\_       |                                                   |
    └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

    map<K,V> (interface) : 键值对
    | 包含:
    |       boolean isEmpty()                   : 如果map中没有key-value映射返回true
    |       boolean containsKey(Object key)     : 如果map不含key映射，返回false，当key的类型不符合，抛出ClassCastException，当key是null且该map不支持key的值是null时，抛出NullPointerException
    |       boolean containsValue(Object value) : 如果map含有一个以上的key映射的参数value，返回true，异常抛出的情况和containKey一样
    |       V get(Object key)                   : 根据key得到对应的value，如果没有对应的映射，返回null，如果map允许value为null，返回null可能是有一对key-null的映射或没有对应的映射
    |       V put(K key, V value)               : 往map放入一对key-value映射
    |       V remove(Object key)                : 根据key删除对应映射
    |       void putAll(map<? extends K, ? extends V> m) : 复制一份与参数一样的map
    |       void clear()                        : 清空map中所有的映射
    |       Set<K> keySet()                     : 返回map中所有key的集合
    |       Collection<V> values()              : 返回map中所有value的集合
    |       Set<map.Entry<K, V>> entrySet()     : 返回key-value的集合
    |       boolean equals(Object o)            : 比较调用者与参数是否相等
    |       int hashCode()                      : 计算map的hashcode
    |
    |
    | 内部接口:
    |   Entry<K,V> (interface) : 用于存放键值对,规范了一些功能
    |     包含:
    |           K getKey()                      : 返回对应的key
    |           V getValue()                    : 返回对应的value    　　
    |           V setValue(V value)             : 设置用新value替换旧value，返回值是旧value　　
    |           boolean equals(Object o)        : 如果两个entry的映射一样，返回true　　
    |           int hashCode()                  : 计算entry的hash code
    |
    |     JDK1.8增加了一些静态方法,返回一些比较器
    |
    ├── AbstractMap<K,V> implements map<K,V>  (abstract class) 抽象类
    |   |
    |   |   除了entrySet,几乎实现了所有方法.
    |   |       public abstract Set<Entry<K,V>> entrySet();
    |   |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |                                                                EnumMap                                                             |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   |   EnumMap<K extends Enum<K>, V> extends AbstractMap<K, V> implements java.io.Serializable
    |   |                                                                      Cloneable
    |   |
    |   |
    |   |
    |   |
    |   |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |                                                                HashMap                                                             |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   |   HashMap<K,V> extends AbstractMap<K,V> implements map<K,V>
    |   |   |                                                Cloneable
    |   |   |                                                Serializable
    |   |   |
    |   |   |   HashMap初始是一个
    |   |   |   数组(散列表)
                @see com.jasmine.数据结构.散列表哈希表结构.概念
                +
                单向链表
                @see com.jasmine.数据结构.线性结构.概念
                的组合,但当链表长度大于等于7(TREEIFY_THRESHOLD - 1)时,
                链表会转换为一颗红黑树.
                @see com.jasmine.数据结构.树型结构.概念
                由一个数组A组成,最大长度为1 << 30(1073741824),自动扩容后可达Integer.MAX_VALUE

                HashMap的一个实例有两个影响其性能的参数： 初始容量和负载因子
                    1. 初始容量:
                        是哈希表中的桶数，初始容量只是创建哈希表时的容量。
                    2. 负载因子:
                        是在容量自动增加之前允许哈希表得到满足的度量。 当在散列表中的条目的数量超过了负载因数和电流容量的乘积，哈希表被重新散列
                        （即，内部数据结构被重建），使得哈希表具有桶的大约两倍。
                        作为一般规则，默认负载因子（.75）提供了时间和空间成本之间的良好折中。 更高的值会降低空间开销，但会增加查找成本（反映在HashMap类
                        的大部分操作中，包括get和put ）。 在设置其初始容量时，应考虑地图中预期的条目数及其负载因子，以便最小化重新组播操作的数量。 如果
                        初始容量大于最大条目数除以负载因子，则不会发生重新排列操作。

                <hash冲突的例子>:
                    key为49和key为"1"时,hash值会冲突,这样后put的值会成为前一个Node的next,形成单向链表

                  A
                ┌────┐     ┌────┬────┐     ┌────┬────┐     ┌────┬────┐     ┌────┬────┐     ┌────┬────┐     ┌────┬────┐     ┌────┬────┐
                |  1 | ──> |Node|Next| ──> |Node|Next| ──> |Node|Next| ──> |Node|Next| ──> |Node|Next| ──> |Node|Next| ──> |Node|Next|
                ├────┤     └────┴────┘     └────┴────┘     └────┴────┘     └────┴────┘     └────┴────┘     └────┴────┘     └────┴────┘
                |  2 | ──>
                ├────┤
                |  3 | ──>
                ├────┤
                |  4 | ──>
                ├────┤
                |  5 | ──>
                ├────┤
                |  6 | ──>
                ├────┤
                |  7 | ──>
                ├────┤                              ┌────┐
                |  8 | ──────────────────────────>  |ROOT|
                └────┘                         ┌────┼────┼────┐
                                          ┌─── |left|    |righ| ───┐
                                          |    └────┘    └────┘    |
                                       ┌────┐                    ┌────┐
                                       |Pare|                    |Pare|
                                  ┌────┼────┼────┐          ┌────┼────┼────┐
                                  |left|    |righ|          |left|    |righ|
                                  └────┘    └────┘          └────┘    └────┘



                一. 属性
                    transient Node<K,V>[] table : 存放Node的数组,也是HashMap存放数据的核心.
                    transient Set<map.Entry<K,V>> entrySet :
                    static class Node<K,V> implements map.Entry<K,V> : 其实是一个红黑树

                二. Node详解
                    1. Node
                        1). 属性
                            final int hash      : Key 的 HashCode
                            final K key;        : key
                            V value;            : 存放的值
                            Node<K,V> next;     : 本节点的下一个节点
                        2). 构造方法
                            Node(int hash, K key, V value, Node<K,V> next) {
                                this.hash = hash;
                                this.key = key;
                                this.value = value;
                                this.next = next;
                            }

                            其中 HashCode的生成方法
                            static final int hash(Object key) {
                                int h;
                                //如果Key等于null则为0
                                //否则等于 key.hashCode() ^ (key.hashCode() >>> 16)
                                return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
                            }

                    2. TreeNode
                        1). 属性
                            TreeNode<K,V> parent;  //节点的父节点 red-black tree links
                            TreeNode<K,V> left;    //节点的左子节点
                            TreeNode<K,V> right;   //节点的右子节点
                            TreeNode<K,V> prev;    // needed to unlink next upon deletion
                            boolean red;           //节点是否红色节点



                二. 方法
                    1.构造方法:
                        构造方法并没有创建table数组,而是在put时创建,这样的好处是最大程度节省内存提高效率

                        public HashMap(int initialCapacity, float loadFactor) {
                            if (initialCapacity < 0)
                                throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
                            if (initialCapacity > MAXIMUM_CAPACITY)
                                initialCapacity = MAXIMUM_CAPACITY;
                            if (loadFactor <= 0 || Float.isNaN(loadFactor))
                                throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
                            this.loadFactor = loadFactor;
                            this.threshold = tableSizeFor(initialCapacity);
                        }

                    2.插入

                        public V put(K key, V value) {
                            return putVal(hash(key), key, value, false, true);
                        }


                        * Implements map.put and related methods
                        *
                        * @param hash hash for key
                        * @param key the key
                        * @param value the value to put
                        * @param onlyIfAbsent if true, don't change existing value 如果为真，则不要更改现有值
                        * @param evict if false, the table is in creation mode. 如果为false，则该表处于创建模式。
                        * @return previous value, or null if none
                        final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
                            Node<K,V>[] tab; Node<K,V> p; int n, i;
                            if ((tab = table) == null || (n = tab.length) == 0)
                                n = (tab = resize()).length;
                            //如果数组这个下标为空,则创建一个新的节点
                            if ((p = tab[i = (n - 1) & hash]) == null)
                                tab[i] = newNode(hash, key, value, null);
                            //如果数组发生碰撞,则
                            else {
                                Node<K,V> e; K k;

                                //如果插入的键值已经存在
                                if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                                    e = p;
                                //如果该下标数组中的节点是一个树状解结构,将此节点插入树
                                //HashMap的数是一颗红黑树
                                else if (p instanceof TreeNode)
                                    e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                                else {
                                    for (int binCount = 0; ; ++binCount) {
                                        //寻找链表最后的值
                                        if ((e = p.next) == null) {
                                            //新建链表末尾节点
                                            p.next = newNode(hash, key, value, null);
                                            //如果链表中的数值大于等于7,则将链表转换为树
                                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                                treeifyBin(tab, hash);
                                            break;
                                        }
                                        if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                                            break;
                                        p = e;
                                    }
                                }

                                if (e != null) { // existing mapping for key
                                    V oldValue = e.value;
                                    if (!onlyIfAbsent || oldValue == null)
                                        e.value = value;
                                    afterNodeAccess(e);
                                    return oldValue;
                                }
                            }
                            ++modCount;
                            if (++size > threshold)
                                resize();
                            afterNodeInsertion(evict);
                            return null;
                        }

    |   |   |
    |   |   |
    |   |   |
    |   |   |
    |   |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |   |                                                         LinkedHashMap                                                          |
    |   |   └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   |       LinkedHashMap<K,V> extends HashMap<K,V> implements map<K,V>
                LinkedHashMap其实也是一个HashMap,他继承自HashMap,只不过他保存了记录的插入顺序.

                1. 构造函数
                    public LinkedHashMap() {
                        //调用hashmap的构造函数
                        super();
                        accessOrder = false;
                    }



                    @param  initialCapacity 初始容量
                    @param  loadFactor      负载因子
                    @param  accessOrder     the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
                    public LinkedHashMap(int initialCapacity,float loadFactor,boolean accessOrder) {
                        super(initialCapacity, loadFactor);
                        this.accessOrder = accessOrder;
                    }
    |   |
    |   |
    |   |
    |   |
    |   |
    |   |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |                                                                TreeMap                                                             |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   |   TreeMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>, ,
    |   |                                                    Cloneable
    |   |                                                    java.io.Serializable
    |   |
    |   |   通过红黑树实现的map
    |   |
    |   |
    |   |
    |   |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |                                                              WeakHashMap                                                           |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   |   WeakHashMap<K,V> extends AbstractMap<K,V> implements map<K,V>
    |   |
    |   |
    |   |
    |   |
    |   |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |                                                           ConcurrentHashMap                                                        |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   |   ConcurrentHashMap<K,V> extends AbstractMap<K,V> implements ConcurrentMap<K,V>
    |   |                                                              Serializable
    |   |
    |   |
    |   |
    |   |
    |   ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |   |                                                            IdentityHashMap                                                         |
    |   └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |       IdentityHashMap<K,V> extends AbstractMap<K,V> implements map<K,V>
    |                                                                Cloneable
    |                                                                Java.io.Serializable
    |
    |
    ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                                                  Hashtable                                                         |
    ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   Hashtable<K,V> extends Dictionary<K,V> implements map<K,V>, Cloneable, java.io.Serializable
    |
    ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                                                  SortedMap                                                         |
    ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   SortedMap<K,V> extends map<K,V>  (interface)
    |
    |
    |
    ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                                                ConcurrentMap                                                       |
    ├────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   ConcurrentMap<K, V> extends map<K, V>  (interface)
    |
    |




































































































     */
}
