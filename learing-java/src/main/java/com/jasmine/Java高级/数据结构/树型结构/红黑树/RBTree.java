package com.jasmine.Java高级.数据结构.树型结构.红黑树;

@SuppressWarnings("unchecked")
public class RBTree<T extends Comparable<T>> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private Node<T> mRoot;// 根结点

    /**
     * 返回节点的父节点
     * @param node 节点
     * @return 节点的父节点
     */
    private Node<T> parentOf(Node<T> node){
        if(node!=null)
            return node.parent;
        else
            return null;
    }
    /**
     * 返回节点颜色
     * @param node 节点对象
     * @return 节点的颜色,RED=false,BLACK=true
     */
    private Boolean isRed(Node<T> node){
        return node != null && node.color == RED;
    }
    /**
     * 返回节点的颜色
     * @param node 节点对象
     * @return 点的颜色,RED=false,BLACK=true
     */
    private boolean colorOf(Node<T> node) {
        if(node!=null)
            return node.color;
        else
            return BLACK;
    }
    private boolean isBlack(Node<T> node) {
        return !isRed(node);
    }
    private void setBlack(Node<T> node) {
        if (node!=null)
            node.color = BLACK;
    }
    private void setRed(Node<T> node) {
        if (node!=null)
            node.color = RED;
    }
    private void setParent(Node<T> node, Node<T> parent) {
        if (node!=null)
            node.parent = parent;
    }
    private void setColor(Node<T> node, boolean color) {
        if (node!=null)
            node.color = color;
    }

    /**
     * 新建结点(key)，并将其插入到红黑树中
     * @param key node的键值,实际该是hash值
     */
    public void insert(T key) {
        Node<T> node = new Node<>(key, BLACK, null, null, null);
        insert(node);
    }

    private void insert(Node<T> node) {
        int cmp;
        Node<T> y = null;
        Node<T> x = this.mRoot;//根节点

        // 1. 将红黑树当作一颗二叉查找树，将节点添加到二叉查找树中。
        while (x != null) {
            y = x;
            // integer的compareTo方法,实现了接口Comparable
            // (x < y) ? -1 : ((x == y) ? 0 : 1);
            //-1: node小于x
            // 0: node等于x
            // 1: node大于x
            cmp = node.key.compareTo(x.key);
            //如果node小于x
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }

        node.parent = y;
        if (y!=null) {
            cmp = node.key.compareTo(y.key);
            if (cmp < 0)
                y.left = node;
            else
                y.right = node;
        } else {
            this.mRoot = node;
        }

        // 2. 设置节点的颜色为红色
        node.color = RED;

        // 3. 将它重新修正为一颗二叉查找树
        insertFixUp(node);
    }

    /**
     * 红黑树插入修正函数
     * 在向红黑树中插入节点之后(失去平衡)，再调用该函数；
     * 目的是将它重新塑造成一颗红黑树。
     *
     * @param node 插入的结点        // 对应《算法导论》中的z
     */
    private void insertFixUp(Node<T> node) {
        Node<T> parent, gparent;

        /*
        若 父节点存在，并且父节点的颜色是红色
        也就是有两个相连的红色节点,违反第四点,才会进行调整树
        @see com.jasmine.Java高级.数据结构.树型结构.概念 红黑树的特点第四点
         */
        //首先我们知道节点不是红色就是黑色,默认情况下我们把所有新插入的节点都认为是红色,有和第四点有冲突,也就是父节点为红色,再进行调整.
        while (((parent = parentOf(node))!=null) && isRed(parent)) {
            gparent = parentOf(parent);

            //如果父节点是祖父节点的左子
            if (parent == gparent.left) {
                Node<T> uncle = gparent.right;
                /*
                那么分为两种情况 1.叔叔为红 2.叔叔为黑
                          祖父
                        /     \
                      红父  叔(黑或红)
                 1. 叔叔为红
                 2. 叔叔为黑
                    1.自己是父亲的右子节点
                    2.自己是父亲的左子节点
                */
                /*
                1. 叔叔为红
                          祖父                   红祖父
                        /     \                 /     \
                      红父    红叔    ->      黑父    黑叔
                     /                       /
                  红自己                   红自己

                  这样就解决了,但是祖父同时也是root,只要调整完把root设置为黑即可
                 */
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);//叔叔变黑
                    setBlack(parent);//父亲变黑
                    setRed(gparent);//爷爷变红
                    node = gparent;//如果爷爷变成红色后,爷爷和爷爷的爸爸有违反第四点的情况则继续调整
                    continue;
                }

                /*
                2.1 叔叔为黑,自己是父亲的右子节点
                        1).                              2).                               3).
                             黑祖父                            黑祖父                           黑祖父
                            /     \         父亲左旋          /     \        自己和父亲互换     /     \
                          红父    黑叔        --->         红自己  黑叔           --->        红父    黑叔
                            \                              /                                /
                          红自己                         红父                             红自己

                   这时候我们发现,变成了2.2的情况,自己是父亲的左子,而叔叔是黑的
                 */
                if (parent.right == node) {
                    Node<T> tmp;
                    leftRotate(parent);
                    //自己和父亲引用的对象互换
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                /*
                 2.2 叔叔为黑,自己是父亲的左子节点
                         1).                          2).                              3).
                                黑祖父                      红祖父                           黑父
                               /     \                     /     \        祖父右旋         /     \
                             红父    黑叔    转换颜色     黑父    黑叔       --->        红自己   红祖父
                             /                --->       /                                         \
                          红自己                       红自己                                      黑叔
                 */
                setBlack(parent);//父亲变黑
                setRed(gparent);//爷爷变红
                rightRotate(gparent);//右旋爷爷

            //若z的父节点是z的祖父节点的右孩子
            } else {
                // Case 1条件：叔叔节点是红色
                Node<T> uncle = gparent.left;
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }
                // Case 2条件：叔叔是黑色，且当前节点是左孩子
                if (parent.left == node) {
                    Node<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子。
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }

        // 将根节点设为黑色
        setBlack(this.mRoot);
    }

    /**
     * 对红黑树的节点(x)进行左旋转,逆时针转
     *
     * 左旋示意图(对节点x进行左旋)：
     *      px                                px
     *     /                                 /
     *    x                                  y
     *   /  \           --(左旋)-.          / \
     *  lx   y                             x  ry
     *     /   \                          / \
     *    ly   ry                        lx ly
     *
     *
     */
    private void leftRotate(Node<T> x) {
        // 设置x的右孩子为y
        Node<T> y = x.right;

        // 将 y的左孩子 设为 x的右孩子；
        // 如果y的左孩子非空，将 x 设为 y的左孩子的父亲
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;

        // 将 x的父亲 设为 y的父亲
        y.parent = x.parent;

        if (x.parent == null) {
            this.mRoot = y;            // 如果x的父亲是空节点,也就是x为根节点,则将y设为根节点
        } else {
            if (x.parent.left == x)
                x.parent.left = y;    // 如果x是他爸的左子,则y成为他爸的左子
            else
                x.parent.right = y;    // 如果 x是它父节点的左孩子，则将y设为x的父节点的左孩子
        }

        // 将 x 设为 y 的左孩子
        y.left = x;
        // 将 x 的父节点 设为 y
        x.parent = y;
    }

    /**
     * 对红黑树的节点(y)进行右旋转
     *
     * 右旋示意图(对节点y进行左旋)：
     *            py                               py
     *           /                                /
     *          y                                x
     *         / \          --(右旋)-.          / \
     *        x  ry                           lx   y
     *       / \                                  / \
     *      xl  xr                               xr yr
     *
     */
    private void rightRotate(Node<T> y) {
        // 设置x是当前节点的左孩子。
        Node<T> x = y.left;

        // 将 x的右孩子 设为 y的左孩子；
        // 如果"x的右孩子"不为空的话，将 y 设为 x的右孩子的父亲
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;

        // 将 y的父亲 设为 x的父亲
        x.parent = y.parent;

        if (y.parent == null) {
            this.mRoot = x;            // 如果 y的父亲 是空节点，则将x设为根节点
        } else {
            if (y == y.parent.right)
                y.parent.right = x;    // 如果 y是它父节点的右孩子，则将x设为y的父节点的右孩子
            else
                y.parent.left = x;    // (y是它父节点的左孩子) 将x设为x的父节点的左孩子
        }

        // 将 y 设为 x的右孩子
        x.right = y;

        // 将 y的父节点 设为 x
        y.parent = x;
    }

    /**
     * 根据键从x位置开始查询节点
     * @param x 开始位置
     * @param key 键值
     * @return 节点
     */
    private Node<T> search(Node<T> x, T key) {
        if (x==null)
            return x;

        int cmp = key.compareTo(x.key);
        //小于找左
        if (cmp < 0)
            return search(x.left, key);
            //大于找右
        else if (cmp > 0)
            return search(x.right, key);
            //等于返回
        else
            return x;
    }

    /**
     * 根据键从头查询节点
     * @param key 键
     * @return 节点
     */
    public Node<T> search(T key) {
        return search(mRoot, key);
    }

    /**
     * 前序遍历红黑树
     * @param root 节点
     */
    public void preorderTraversalRec(Node<T> root){
        if(root == null){
            return;
        }
        System.out.print(root.color == BLACK ? "黑" : "红" );
        System.out.print(root.key + " ");
        preorderTraversalRec(root.left);
        preorderTraversalRec(root.right);
    }

    /**
     * 删除结点(z)，并返回被删除的结点
     * @param key 删除的节点
     */
    public void remove(T key) {
        Node<T> node;

        if ((node = search( key)) != null)
            remove(node);
    }

    /**
     * 删除结点(node)，并返回被删除的结点
     * @param node 节点
     */
    private void remove(Node<T> node) {
        Node<T> child, parent;
        boolean color;

        // 被删除节点的"左右孩子都不为空"的情况。
        if ( (node.left!=null) && (node.right!=null) ) {
            // 被删节点的后继节点。(称为"取代节点")
            // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
            Node<T> beRemove = node;

            /* **************************************************************************************************************
            1. 查询到取代节点,也就是大于被删除节点的最小节点,用于替换被删除节点
             **************************************************************************************************************/
            // 获取被删除节点的右子节点
            beRemove = beRemove.right;
            // 获取左子节点的最后一个左节点
            while (beRemove.left != null)
                // 遍历后的beRemove节点其实就是大于被删除节点中最小的那一个
                // 也就是取代节点
                beRemove = beRemove.left;

            /* **************************************************************************************************************
            2. 取代节点替代被删节点,成为被删节点父亲的孩子
             **************************************************************************************************************/
            // 被删的节点不是根节点(存在父节点)
            if (parentOf(node)!=null) {
                // 如果被删节点父节点等于被删节点,说白了就是被删的是左子节点,那取代节点变成他爸的左子节点
                if (parentOf(node).left == node)
                    parentOf(node).left = beRemove;
                // 否则取代节点成为被删他爸的右子节点
                else
                    parentOf(node).right = beRemove;
            // 被删节点是根节点，则取代节点为根节点
            } else {
                this.mRoot = beRemove;
            }

            /* **************************************************************************************************************
            3. 将取代节点的右孩子设为取代他爸的左孩子
             **************************************************************************************************************/
            // child是取代节点的右孩子，也是需要调整的节点。,
            // "取代节点"肯定不存在左孩子！因为它是一个后继节点。

            child = beRemove.right;//取代的右儿子
            parent = parentOf(beRemove);//取代他爸
            color = colorOf(beRemove);// 取代的颜色

            /*
            被删节点是取代节点的父节点
            例如:
                           B50
                       /        \
                    R20        B80      删除R20,那么替代节点就是B30,
                   /  \         /       那么取代他爸就是被删除节点,那取
                 B10  B30      R70      代就去到原来被删的位置

              */
            if (parent == node) {
                parent = beRemove;
            } else {
                /*
                    如果取代节点有右孩子
                    那右孩子的父亲就变成取代节点的父亲
                    例如:
                           B30                                                                      B30
                       /        \                                                               /        \
                    B20        B50          删除R20,那么取代节点就是R22,                        B20        B50
                   /  \        /  \         那么取代他爸就是B25                                /  \        /  \
                 B10  R25    B35  B80       1.取代的右子节点R23他爸就变成取代他爸R25          B10  R25    B35  B80
                     /   \        /         2.取代他爸R25的左子就变成R23                         /   \        /
                   B22   B27    R70         3.被删除的右子节点变成被取代的右子节点              R23   B27    R70
                     \                      4.被删的右子他爸变成取代
                     R23

                    这时候我们看到R23和R25冲突了,先别急,后面会调整
                 */
                if (child!=null)
                    //1
                    child.parent = parent;
                //2
                parent.left = child;
                //3
                beRemove.right = node.right;
                //4
                node.right.parent = beRemove;
            }


            /* **************************************************************************************************************
            4.取代的一切属性都替换成被删除的属性
              包含:
                   父亲节点
                   颜色
                   左子节点
                   被删的右子节点的父亲变成替代节点
            **************************************************************************************************************/
            beRemove.parent = node.parent;
            beRemove.color = node.color;
            beRemove.left = node.left;
            node.left.parent = beRemove;

            //如果取代节点的颜色为黑色,调整树
            if (color == BLACK)
                removeFixUp(child, parent);
            node = null;
            return ;
        }

        /*
        如果被删除的节点有左子
         */
        if (node.left !=null) {
            child = node.left;
        } else {
            child = node.right;
        }

        parent = node.parent;
        // 保存"取代节点"的颜色
        color = node.color;

        if (child!=null)
            child.parent = parent;

        // "node节点"不是根节点
        if (parent!=null) {
            if (parent.left == node)
                parent.left = child;
            else
                parent.right = child;
        } else {
            this.mRoot = child;
        }

        if (color == BLACK)
            removeFixUp(child, parent);
        node = null;
    }

    /**
     * 红黑树删除修正函数
     *
     * 在从红黑树中删除插入节点之后(红黑树失去平衡)，再调用该函数；
     * 目的是将它重新塑造成一颗红黑树。
     *
     * @param node 待修正的节点
     * @param parent
     */
    private void removeFixUp(Node<T> node, Node<T> parent) {
        Node<T> other;

        while ((node==null || isBlack(node)) && (node != this.mRoot)) {
            if (parent.left == node) {
                other = parent.right;
                if (isRed(other)) {
                    // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.right==null || isBlack(other.right)) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.mRoot;
                    break;
                }
            } else {

                other = parent.left;
                if (isRed(other)) {
                    // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.left==null || isBlack(other.left)) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }

                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    rightRotate(parent);
                    node = this.mRoot;
                    break;
                }
            }
        }
        if (node!=null)
            setBlack(node);
    }


    /**
      * 打印"红黑树"
      *
      * key        -- 节点的键值
      * direction  --  0，表示该节点是根节点;
      *               -1，表示该节点是它的父结点的左孩子;
      *                1，表示该节点是它的父结点的右孩子。
      */
    private void print(Node<T> tree, int direction) {
        if(tree != null) {
            if(direction==0)    // tree是根节点
                System.out.printf("(B%d) 是 root\n", tree.key);
            else                // tree是分支节点
                System.out.printf("(%s%d) 是 (%s%d) 的 %3s \n",
                        isRed(tree)?"R":"B", tree.key,
                        isRed(tree.parent)?"R":"B",tree.parent.key,
                        direction==1?"右" : "左");
                print(tree.left,-1);
                print(tree.right,1);
        }
    }

    /**
    * 打印"红黑树"
    */
    public void print() {
        if (mRoot != null)
            print(mRoot, 0);
    }

    @SuppressWarnings("all")
    public static void main(String[] args) {
        RBTree rbt = new RBTree();
        System.out.println("前序遍历:");

        /*
        例1:删除只会简单修正.

        rbt.insert(50);
        rbt.inser+64
        1 t(20);
        rbt.insert(80);
        rbt.insert(70);
        rbt.insert(10);
        rbt.insert(30);
        rbt.insert(25);
        rbt.insert(35);
        rbt.insert(27);
        rbt.insert(22);
        rbt.insert(23);
        rbt.print();
        rbt.remove(20);
        */
        /*
        例二:删除会复杂修正
         */

        rbt.insert(10);
        rbt.insert(8);
        rbt.insert(15);
        rbt.insert(4);
        rbt.insert(5);
        rbt.insert(3);
        rbt.insert(2);
        rbt.print();
        System.out.println("\n删除后:");
        rbt.remove(10);
        rbt.print();
    }



    class Node<T extends Comparable<T>>{
        boolean color;        // 颜色
        T key;                // 关键字(键值)
        Node<T> left;    // 左孩子
        Node<T> right;    // 右孩子
        Node<T> parent;    // 父结点

        public Node(T key, boolean color, Node<T> parent, Node<T> left, Node<T> right) {
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

    }
}


