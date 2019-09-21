package com.jasmine.Java高级.数据结构.树型结构;

import com.jasmine.Java高级.数据结构.树型结构.二叉树.BinaryTree;

public class 概念 {
    /**
    ┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                                                 Tree                                                             |
    ├──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   树 (Tree)
    |   是n(n>=0)个结点的有限集.
    |   1. 有且仅有一个特定的称为根(Root)的结点；
    |
    |   2. 当n>1时,其余结点可分为m(m>0)个互不相交的有限集T1,T2,...,Tn,其中每个集合本身又是一棵树,并称为根的子树(SubTree)
    |
    |    层次                         树
    |     1            root<────      A                       ↑                ─────────┐
    |                             /   |   \         D是A的<孩子>(Child)     A是D的<双亲>(Parent)
    |     2                      B    C    D         ─────────┘                         ↓
    |                           / \   |  / | \
    |     3                    E   F  G  H I J      E是F的<兄弟>(同双亲),F是G的<堂兄弟>(双亲为兄弟),D是G的<叔父>
    |                         / \        |
    |     4                  K   L       M          <祖先>是从根到该结点所经分支上的所有结点.如:上图M的祖先为A、D、H
    |
    |     1) .度
    |         (1). 结点拥有的子树称为结点的<度>(Degree)如:上图A的度为3,C的度为1,F的度为0.
    |         (2). 度为0的结点称为<叶子>(Leaf)或<终端结点>,如:上图K,L,F,G,M,I,J都是树的叶子
    |         (3). 度不为0的结点称为非终端结点或分支结点,如:上图A,B,C,D,E,H
    |       ★树的度是树内各节点的度的最大值,如:上图的树的度为 3 .
    |
    |     2) .结点
    |         (1). 结点的子树的根称为该结点的孩子(Child),该结点称为孩子的双亲(Parent)如:上图D是A的孩子,A是D的双亲.
    |         (2). 同一个双亲的孩子叫兄弟(Sibling)如:上图H,I,J为互为兄弟
    |         (3). 其双亲在同一层的结点互为堂兄弟.如上图G与E、F、H、I、J互为堂兄弟
    |         (4). 结点的祖先是从根到该结点所经分支上的所有结点.如:上图M的祖先为A、D、H
    |
    |     3). 层次
    |         (1). 结点的层次(Level)从根开始定义起,根为第一层,根的孩子为第二层,
    |
    |     4). 深度
    |         (1). 深度是从根节点往下,树中结点的最大层次成为树的<最大深度>(Depth)或高度.\
    |              如:上图树的最大深度为4,B的深度为2
    |
    |     5). 高度
    |         (1). 从叶子节点往上数
                   如:上图E的高度为2,最大高度为4
    |
    |     6). 有序树与无序树(长子,次子..)
    |         (1). 树中结点的各子树看成从左至右是有次序的(即不能互换),则称该树为有序树,否则成为无序树.
    |         (2). 有序树中最左的子树的根称为第一个孩子,最右边的称为最后一个孩子.(毕竟有序,排好了谁是老大,谁是老二,长兄有序嘛)
    |       ★森林(Forest)是m(m>=0)棵互不相交的树的集合.对树中每个结点而言,其子树的集合即为森林,对于A来说,BCD即为森林
    |         由此也可以森林和树相互递归的定义来描述树.
    |
    |         树的结点包含一个数据元素及若干指向其子树的分支.
    |
    ├──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                                          Binary Tree                                                             |
    ├──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |   二叉树
    |   每个结点只有两颗子树的树是二叉树
    |
    |    层次                        树
    |     1                          A
    |                             /     \
    |     2                      B       D
    |                           / \     / \
    |     3                    E   F   H   J
    |                         / \      
    |     4                  K   L      
    |
    |   一. 特点
    |       1. 每个结点至多只有两棵子树 ( 即二叉树中不存在度大于2的结点 ).
    |       2. 二叉树的子树有左右之分,其次序不能随意颠倒.
    |
    |   二. 性质
    |       1. 二叉树的第 i 层上至多有 2^(i-1)个结点 (i >=1)
    |           第一层 ：1(2^0)    
    |           第二层 ：2(2^1)    
    |           第三层 ：4(2^2)    
    |           第四层 ：8(2^3)
    |
    |       2. 深度为 k 的二叉树至多有 2^(k-1) 个结点 (k >=1)
    |           由性质一得：第k层,2^k - 1 个结点
    |           1+2+2^2+....+2^(k-1) = ( 1 - 2^k)/(1-2) = 2^k - 1 
    |
    |       3. 对任何一棵二叉树 T ,如果其终端结点数为 n0 ,度为 2 的结点数位 n2,则 n0=n2+1.
    |           式一 ：n = n0 + n1 + n2 (结点总数 = 度为0 + 度为1 + 度为2)
    |           式二 ：n = n0 + 2*n2 +1 (n = 分支总数+1  ；分支总数 = n1+n2 (分支是由度为1,度为2的结点射出的))
    |           式二 - 式一得： n0 = n2 + 1
    |
    |   三. <二叉搜索树> B树 (Balance Tree)
    |
    |                   0
    |                /     \
    |              -1       7
    |              /       / \
    |            -3       4   8
    |              \     / \   \
    |              -2   3   5   9
    |       1. 特点
    |           每个节点的左子节点 < 自己,右子节点 > 自己
    |
    |       2. Java体现
    |
    |           1). 构造
    |               int val         :   用于计算节点在树中的位置,一般为一个Hash值
    |               Object value    :   节点的内容
    |               TreeNode left   :   节点的左节点
    |               TreeNode right  :   节点的右节点
    |
                2). 插入
                    @see BinaryTree.insert

                3). 查询
                    (1). 前序遍历
                            递归前 就输出自己.

                            口诀:
                            砍了自己砍左边.
                            还有继续砍左边.
                            左边子孙砍一遍.
                            没有我再砍右边.


                            前序遍历递归解法
                            @see BinaryTree.preorderTraversalRec
                            前序遍历迭代解法
                            @see BinaryTree.preorderTraversal

                    (2). 中序遍历
                            递归左边找不到了 就输出自己

                            口诀:
                            靠左一路走到底.
                            砍了左边砍自己.
                            砍了自己砍右边.
                            然后父亲变自己.

                            砍了自己砍右边.
                            若是右边有左边.
                            刀下留情莫着急.
                            继续靠左走到底.

                            @see BinaryTree.inorderTraversalRec

                    (3). 后序遍历
                            有左就递归左,有右就递归右,左右都找不到 就输出自己.

                            口诀:
                            靠左一路走到底.
                            砍了自己砍兄弟.
                            兄弟若是有孩子.
                            先砍孩子再兄弟.

                            @see BinaryTree.postorderTraversalRec


                    (4). 分层遍历二叉树
                            借助一个LinkedList来遍历.
                            将根节点插入到链表
                            然后取出链表的第一个节点并将该节点的左右子节点插入到链表末尾

                            @see BinaryTree.levelTraversal


        四. <平衡二叉树> AVL树

            1. 性质
                1). 任何一个节点的左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树。

            这个方案很好的解决了二叉查找树退化成链表的问题，把插入，查找，删除的时间复杂度最好情况和最坏情况都维持在O(logN)。
            但是频繁旋转会使插入和删除牺牲掉O(logN)左右的时间，不过相对二叉查找树来说，时间上稳定了很多。

            例如:
                    0                                                        3
                     \                                                     /   \
                      1                                                   1     4
                       \                                                 / \     \
                        2             这也是一颗二叉查找树,但明显这       0   2     5           平衡二叉树的效率稳定了许多.
                         \            样子就是一张链表.
                          3           左边转化为平衡二叉树为:
                           \
                            4
                             \
                              5





    |   五. <满二叉树> 完美二叉树
    |       一颗深度为k,且有(2^k)-1个节点的树是满二叉树
    |       翻译为人话: 所有叶子节点在同一层,除了叶子节点都有左右两个子节点.
    |
    |                       A                       树高：h=log2(n+1)。
    |                 /           \                 深度: H = 4
    |                B             C                最大层数: K = 4
    |              /   \         /   \              叶子数: 2^(h-1) = 2^(4-1)
    |             D     E       F     G             第K层的节点: 2^(k-1) = 2^(3-1)
    |            / \   / \     / \   / \            总结点数是: 2^k-1 = 2^4-1
    |           H   I J   K   L   M N   O           总节点数一定是奇数
    |
    |       1. 性质
    |           1). 如果一颗树深度为h,最大层数为k,且深度与最大层数相同,即k=h;
    |           2). 它的叶子数是： 2^(h-1)
    |           3). 第k层的结点数是： 2^(k-1)
    |           4). 总结点数是： 2^k-1 (2的k次方减一)
    |           5). 总节点数一定是奇数。
    |           6). 树高：h=log2(n+1)。
    |
    |
    |
    |
    |
    |   六. <完全二叉树>
    |
    |       完全二叉树是由满二叉树而引出来的。对于深度为K的,有n个结点的二叉树,当且仅当其每一个结点都与深度为K的满二叉树中编号从1至n的结点一一
    |   对应时称之为完全二叉树。
    |       翻译为人话: 从根结点到倒数第二层满足完美二叉树,最后一层可以不完全填充,其叶子结点都靠左对齐
    |
    |                       A
    |                 /           \
    |                B             C
    |              /   \         /   \
    |             D     E       F     G    ←-- 截止到这一层,也就是倒数第二层,就是一个满二叉树
    |            / \   / \     /
    |           H   I J   K   L            ←-- 到最后一层,叶子节点全部靠左
    |
    |
    |
    |
    |
    |   七. <哈夫曼树> 最优二叉树
    |
    |       每个节点要么有两个子节点,要么没有节点
    |                       A
    |                    /     \
    |                   B       C
    |                 /   \   /   \
    |                D     E F     G
    |               / \
    |              H   I

    ├──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
    |                                                          Red-Black Tree                                                          |
    ├──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    |                          B_35
    |                 /                   \
    |               B18                   B70
    |            /      \              /       \
    |           B9      B21          R60       B90
    |         /           \         /   \     /   \
    |        R4           R30     B45   B64 R85   R90
    |                               \
    |                               R50
    |
    |   <红黑树>
    |   R-B Tree,全称是Red-Black Tree,又称为“红黑树”,它一种特殊的二叉查找树。红黑树的每个节点上都有存储位表示节点的颜色,可以是红(Red)或黑(Black)。
    |
    |   一. 特点
    |       1. 每个节点或者是黑色,或者是红色。
            2. 根节点是黑色。
            3. 每个叶子节点是黑色。 [注意：这里叶子节点，是指为空的叶子节点！]
            4. 红色节点不能连续（也即是,红色的孩子不能是红色）。
            5. 对于每个节点,从该点至null（树尾端）的任何路径,都含有相同个数的黑色节点。

        例如:
            此时如果添加一个51,他会在R50节点右子节点,此时要判断51是红色还是黑色
            如果是红色R51,则违反第4点,R50和R51颜色相同.
            如果是黑色B51,则会违反第5点,因为B35到除B51外的所有节点所经过的黑色节点都为3,而到B51的黑色节点为4,所以错误.

        那么要怎么办呢?
        这就是红黑树最复杂的地方: 调整树.

        调整树有两种办法:
            1. 改变某些节点的颜色.
            2. 对某些节点进行旋转.

        现在我们明白该做什么了,但怎么样实现呢
        还是那个问题,51究竟该是R还是B?

        二. 插入
            1. 将红黑树当做一颗二叉查找树来插入节点.
                红黑树本身是一颗二叉查找树,将节点插入后,仍然是二叉查找树,无论树要左旋还是右旋,还是对节点重新着色,他都是二叉查找树.
            2. 将插入的节点设为红色.
                为什么要设为红色呢?
                因为判断第四点,红色节点相连的成本比判断第五点的成本要低很多.
                想判断第四点,只要将插入节点设为红,然后判断父亲节点是否为红色就好,而判断第五点则要判断本节点所在路径的黑色节点与其他路径黑色节点数
            是否相同.想想就知道代码会复杂不少.
            3. 通过一系列旋转或者着色等操作,使之成为一颗红黑树.

            @see com.jasmine.数据结构.树型结构.红黑树.RBTree.insertFixUp

            这段代码很复杂,但我们如果整理好逻辑,其实也很好懂.

            我和父亲都为红
            父在左右不相同 (父节点在爷节点的左右时,情况是相反的)
            看完父亲看昂口 (叔叔节点 uncle)
            昂口红黑也不同 (叔叔的颜色是红色还是黑色也是不同的)

            昂口为红我轻松
            叔父变黑爷变红 (叔叔为红色时,只需要把父亲和叔叔变成黑色,把爷爷变成红色,如果爷爷是根节点,那仍然为黑色)
            昂扣为黑也不怂
            看我左右大不同 (当叔叔为非洲人(只是个梗,没有种族歧视)时,那就坑了我,需要再看我是在父亲的左边还是右边)

            我和我爸在一边 (如果我和父节点相同,例如都是左子节点)
            我爸变黑爷变红 (那么先把父节点变黑,爷节点变红)
            爷爷旋转与我反 (然后爷爷节点旋转,我是父的左子,父是爷的左子,则爷爷右旋; 我是父的右子,父是爷的右子,则爷爷左旋)
            转完爷孙辈相同 (旋转后其实爷爷节点变成了的与我相同父亲但方向不同的子节点)

            父与我不在一边 (如果父亲与我不在一边,例如我是父亲的右子,父亲是爷爷的左子)
            父亲旋转与我反 (如果我是右子,父亲就左旋,如果我是左子,父亲就右旋)
            我和我爸互交换 (旋转完其实我就变成了父亲,父亲变成了与我方向不同的儿子)
            我爸变黑爷变红 (这样就成了和第三段一样的情况)

        三. 删除


            @see com.jasmine.数据结构.树型结构.红黑树.RBTree.remove






















































    */
}
