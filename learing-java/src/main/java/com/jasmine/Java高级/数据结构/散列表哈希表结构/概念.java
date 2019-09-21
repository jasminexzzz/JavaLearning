package com.jasmine.Java高级.数据结构.散列表哈希表结构;


import com.jasmine.Other.基础数学知识.基础名词.互质数;

public class 概念 {
    /**
     *
     * 部分取自:
     * http://www.cnblogs.com/1-2-3/archive/2010/10/11/hash-table-part1.html : 白话算法(6) 散列表(Hash Table)从理论到实用（上）
     * http://www.cnblogs.com/1-2-3/archive/2010/10/12/hash-table-part2.html : 白话算法(6) 散列表(Hash Table)从理论到实用（中）
     * http://www.cnblogs.com/1-2-3/archive/2010/10/18/hash-table-part3.html : 白话算法(6) 散列表(Hash Table)从理论到实用（下）
     *
     * @author WangYunfei
     * ----------------------------------------------
    一.什么是hashCode
        1.  hashCode() 的作用是获取哈希码, 也称为散列码；它实际上是返回一个int整数.这个哈希码的作用是确定该对象在哈希表中的索引位置.
        2.  hashCode() 定义在JDK的Object.java中, 这就意味着Java中的任何类都包含有hashCode() 函数.
        3.  虽然, 每个Java类都包含hashCode() 函数.但是, 仅仅当创建并某个“类的散列表”时, 该类的hashCode() 才有用(作用是：确定该类的每
            一个对象在散列表中的位置；其它情况下(例如, 创建类的单个对象, 或者创建类的对象数组等等), 类的hashCode() 没有作用.上面的散列表,
            指的是：Java集合中本质是散列表的类, 如HashMap, Hashtable, HashSet.也就是说：hashCode() 在散列表中才有用, 在其它情况下没用.
            在散列表中hashCode() 的作用是获取对象的散列码, 进而确定该对象在散列表中的位置.

        说白了,<哈希表就是一个根据存放元素的hashcode值来确定存储位置的数组>

    二. O(n) 查找和 O(1) 查找, 两个模型

            如果想知道在《Java从入门到放弃》这本书里是否有一个“完蛋”两个字, 该怎么做呢？我们只有从第一页的第一行开始, 一个字一个字地向后看去, 直到找到
        这个字为止.如果直到最后一页的最后一个字都没有找到它, 我们就知道这本书里根本没有这个字.所以, 这项工作的复杂度是 O(n).

            假设一本字帖, 它只有9页, 每一页上有一个大写的数字：

                 一       二       三       四       五       六       七       八       九
                第1页    第2页    第3页    第4页    第5页    第6页    第7页     第8页    第9页

            当想要练习“七”字时, 只要她事先知道页码和内容的对应关系, 就可以直接翻到第7页, 实现 O(1) 复杂度的查找.通过这个模型我们知道, 要想达成 O(1)
        复杂度的查找, 必须满足3个条件：
            1). 存储单元（例如一页纸）中存储的内容（例如大写数字）与存储单元的地址（例如页码）必须是一一对应的.
            2). 这种一一对应的关系（例如大写数字“柒”在第7页）必须是可以预先知道的.
            3). 存储单元是可以随机读取的.这里“随机读取”的意思是可以以任意的顺序读取每个存储单元, 并且每次读取所需时间都是相同的.与此相对的, 读取磁带
                里的一首歌就不是随机的——想听第5首歌就不如听第一首歌来得那么方便.

        1. 在计算机上实现 O(1) 查找

            先来看计算机的硬件设备.计算机的内存支持随机存取, 从它的名字 RAM(random-access memory) 可以看得出对于这一点它还真有一点引以为傲呢.
            既然硬件支持, 我们就可以准备在计算机上模拟会计专业字帖了.第一项任务是向操作系统申请9个存储单元.这里有个小问题, 我们得到的存储单元的地址很
        可能并不是从1到9, 而是从134456开始的.好在我们并不需要直接跟操作系统打交道, 高级语言会为我们搞定这些琐事.当我们使用高级语言创建一个数组时, 相
        当于申请了一块连续的存储空间, 数组的下标是每个存储单元（抽象）的地址.这样我们第一个 O(1) 复杂度的容器 SingleIntSet 很容易就可以完成了, 它只
        能存储 0～9 这10个数字：
            @see com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞.SingleIntSet

            ★新需求！同样只需要保存10个数字, 只不过这次不是保存0～9, 而是需要保存10～19, 怎么办？很简单, 实现一个槽里的值与地址的映射函数 H() 即可：
            @see com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞.SingleIntSet2

            ★★新需求！新需求！还是存储10个数字, 只不过数字的范围是0～19.如何把20个数字存放到10个槽里？还能怎么办, 2人住1间咯.略微修改一下 H() 函数
            @see com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞.SingleIntSet3
            最后一行的结果不对！2人住1间是行不通的, 数据受不了这委屈.但是米有办法, 除非
                1). 我们预先知道所有的10个输入
                2). 这10个输入一旦决定就不再更改, 否则无论怎么设计 H() 函数都无法避免2人住一间的情况, 这时我们就说发生了 <碰撞>(collision).





        ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        ★★★★★                                         ★★★★★
        ★★★★★      散列表的重点, 就在于如何处理碰撞     ★★★★★
        ★★★★★                                         ★★★★★
        ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★


        ┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
        |                                                             <链接法>                                                               |
        └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

        2. 用链接法处理碰撞

            处理碰撞最简单的一个办法是<链接法>(chaining).链接法就是让发生碰撞的2人住2间, 但是共用1个公共地址.为了简单起见, 可以让数组的每个槽都指向一个链表：
            通俗的讲就是
                数组Object[0] = linkedlist, LinkedList中存放的是0, 10
                数组Object[1] = linkedlist, LinkedList中存放的是1, 11
                数组Object[2] = linkedlist, LinkedList中存放的是2, 12
                @see com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞.SingleIntSet4

            1). 如何让21亿人使用10个地址？
                好吧, 有了链接法, 我们有了足够的房子以应对可能发生的碰撞.但是我们仍然希望碰撞发生的几率越小越好, 特别是当我们把数值范围由 0～19 扩大到
            0～int.MaxValue (2147483647:21亿) 的时候.有什么办法能把21亿个数值映射成10个数值, 并且尽量减少碰撞？

            2). <除法散列法>
                h(k) = k mod m (h = k / m 的余数)
                例如 : 如果散列表的大小为 m=12, 所给关键字为 k=100, 则h(k)=4;
                其中, k为槽中被操作的数值, m是数组的大小.这样我们得到第一个正整数范围内通用的 IntSet：
                @see com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞.SingleIntSet4 使用除法散列法计算H()的返回值

                只发生了一次碰撞！它竟然与手写版的 ﻿﻿﻿﻿﻿﻿SingleIntSet4.H() 工作得一样好.除法散列法为什么有效呢？魔术一旦揭开谜底总是显得平平无奇：
                    (1).    如果小学课程还想得起来的话, 应该还记得再大的数除以10的余数都一定介于0～9之间, 以此作为下标访问数组自然不用担心越界啦.
                    (1).    让 h() 得出 1 的 k 的数量与让 h() 得出 2 的 k 的数量相同, 这样才不容易产生碰撞.
                    (1).    让 h() 得出 1 的 k 是 1、11、21、31……101、111、121……也就是说导致碰撞的 k 值比较分散.这是很重要的, 因为在实际使用 IntSet 的时候,
                            存储的值经常是紧挨着的, 譬如年龄、序号、身份证号码等等.也就是说如果数据紧密, 则会碰撞数据就要比较分散.

                ★需要注意的是 m 不应是 2 的幂即 2^p 的形式, 此时 h(k) 将等于 k 的二进制的最低 p 位
                ★通常希望 h(k) 的值依赖于 k 的所有位而不是最低 p 位

                例一:
                    以 m = 2^3 = 8 为例, 如下图所示：
                    k(10进制)       k(2进制)      h(k)
                        168         10101000        0
                        169         10101001        1
                        170         10101010        2
                        171         10101011        3
                        172         10101100        4
                        173         10101101        5
                        174         10101110        6
                        175         10101111        7
                        176         10110000        0
                        177         10110001        1
                        178         10110010        2
                        179         10110011        3

                    以 k = 170 为例
                    h(k) = 170 mod 8 (m为2^3)
                    ----------------------------------二进制计算
                         = 10101010 mod 1000
                         = 10100000 mod 1000    +    1010 mod 1000
                         = 0 + 2
                         = 2
                         = 010
                    ----------------------------------十进制计算
                         = 160 mod 8   +   10 mod 8
                         = 0 + 2
                         = 2
                     在以二进制计算时, h(k) = k 的 p(2^p=m) 位, 也就是3位, 此时计算余数只会依赖最低3位



                例二:
                    以 m = 2^4 = 16 为例, 如下图所示：
                    k(10进制)       k(2进制)      h(k)
                        168         10101000        8
                        169         10101001        9
                        170         10101010        10
                        171         10101011        11
                        172         10101100        12
                        173         10101101        13
                        174         10101110        14
                        175         10101111        15
                        176         10110000        0
                        177         10110001        1
                        178         10110010        2
                        179         10110011        3

                    以 k = 173 为例
                    h(k) = 173 mod 16 (m为2^4)
                    ----------------------------------二进制计算
                         = 10101101 mod 10000
                         = 10100000 mod 10000   +   01101 mod 10000
                         = 0 + 13
                         = 13
                    ----------------------------------十进制计算
                         = 160 mod 16   +   13 mod 16
                         = 0 + 13
                         = 13
                在以二进制计算时, h(k) = k 的 p(2^p=m) 位, 也就是4位, 此时计算余数只会依赖最低4位
                为了避免, 当用户指定数组的大小之后, 我们要找到一个与之最接近的质数作为实际的 m 值, 为了速度, 我们把常用的质数预存在一张质数表中,
                新的 IntSet2 允许用户指定它的容量：
                @see com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞.IntSet2

            3). <乘法散列法>
                h(k) = ⌊m(kA mod 1)⌋
                其中, A 是一个大于0小于1的常数, 例如可以取 A = 2654435769 / 232.kA mod 1 的意思是取 kA 的小数部分.



        ───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────



        至此, 链接法结束
        但是!!!链接法不是处理碰撞的唯一方法, 还有更好的方法可以使用!!

        不用链接法, 还有别的方法能处理碰撞吗？
        链接法直接, 简单, 为什么要用其他方法处理碰撞呢?
        技术的进步不是我等渣渣可以阻挡的.
        永远有大神们, 在优化的路上一去不复返.

        ┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
        |                                                            <开放寻址法>                                                             |
        └────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

        1.开放寻址法处理碰撞
            不用额外的链表, 以及任何其它额外的数据结构, 就只用一个数组, 在发生碰撞的时候怎么办呢？
            答案只能是, 再找另一个空着的槽啦！这就是<开放寻址法>(open addressing).

            但是这样难道不是很不负责任的吗？

            想象一下, 有一趟对号入座的火车, 假设它只有一节车厢.
            上来一位坐7号座位的旅客.
            过了一会儿, 又上来一位旅客, 他买到的是一张假票, 也是7号座位, 这时怎么办呢？
            列车长想了想, 让拿假票的旅客去坐8号座位.
            过了一会儿, 应该坐8号座位的旅客上来了, 列车长对他说8号座位已经有人了, 你去坐9号座位吧.
            哦？9号早就有人了？10号也有人了？那你去坐11号吧.
            可以想见, 越到后来, 当空座越来越少时, 碰撞的几率就越大, 寻找空座愈发地费劲.
            但是, 如果是火车的上座率只有50%或者更少的情况呢？也许真正坐8号座位的乘客永远不会上车, 那么让拿假票的乘客坐8号座位就是一个很好的策略了.


            所以, 这是一个空间换时间的游戏.


            玩好这个游戏的关键是, <让旅客分散地坐在车厢里>.


            如何才能做到这一点呢？
            答案是, 对于每位不同的旅客使用不同的探查序列.
            例如:
            对于旅客A, 探查座位 7, 8, 23, 56……直到找到一个空位；
            对于旅客B, 探查座位 25, 66, 77, 1, 3……直到找到一个空位.
            如果有 m 个座位, 每位旅客可以使用 <0,  1,  2,  ...,  m-1> 的 m!(m的阶乘 m!=1×2×3×...×m.) 个排列中的一个.
            显而易见，最好减少两个旅客使用相同的探查序列的情况。也就是说，希望把每位旅客尽量分散地映射到 m! 种探查序列上。
            换句话说，理想状态下，如果能够让每个上车的旅客，使用 m! 个探查序列中的任意一个的可能性是相同的，我们就说实现了<一致散列>。
            (这里没有用“随机”这个词儿，因为实际是不可能随机取一个探查序列的，因为在查找这名旅客时还要使用相同的探查序列）。

            真正的一致散列是难以实现的，实践中，常常采用它的一些近似方法。
            常用的产生探查序列的方法有：
                线性探查.
                二次探查.
                以及双重探查.

            这些方法都不能实现一致散列，因为它们能产生的不同探查序列数都不超过 m2 个（一致散列要求有 m! 个探查序列）。
            在这三种方法中，双重散列能产生的探查序列数最多，因而能给出最好的结果.

            在除法散列法中，我们实现了一个函数 h(k)，它的任务是把数值 k 映射为一个数组（尽量分散）的地址。
            这次，我们使用开放寻址法
            需要实现一个函数 h(k, i)，它的任务是把数值 k 映射为一个地址序列，
            序列的第一个地址是 h(k, 0)，
            第二个地址是 h(k, 1)
            序列中的每个地址都要尽可能的分散。


            1).<线性探查>
                有这样一个可以用 10 个槽保存 0～int.MatValue （但是不能处理碰撞）的 IntSet1:
                @see com.jasmine.数据结构.散列表哈希表结构.开放寻址法解决碰撞.IntSet1
                如果第一个地址被占用了,就探查下一个紧挨着的地址，如果还是不能用，就探查下一个紧挨着的地址，如果到达了数组的末尾，就卷绕到数组的开头，
            如果探查了 m 次还是没有找到空槽，就说明数组已经满了，这就是线性探查（linear probing）

                例如:
                如果发现 values[0] 已经被占用了，就看看 values[1] 是否空着，如果 values[1] 也被占用了，就看看 values[2] 是不是还空着.

                但是:
                比较麻烦的是 Remove() 函数。
                不能简单地把要删除的槽设为 null，那样会导致 Contains() 出错。
                举个例子:
                如果依次把 3，13，23 添加到 IntSet2 中，会执行 values[3] = 3，values[4] = 13，values[5] = 23。
                然后，Remove(13) 执行 values[4] = null。
                这时，再调用 Contains(23)，会依次检查 values[3]、values[4]、values[5] 直到找到 23 或遇到 null,由于 _values[4] 已经被设为 null 了，
            所以 Contains(23) 会返回 false。有一个解决此问题的方法是，在 Remove(23) 时把 _values[4] 设为一个特殊的值（例如 -1）而不是 null。这样
            Contains(23) 就不会在 values[4] 那里因为遇到 null 而返回错误的 false 了。并且在 Add() 里，遇到 null 或 -1 都视为空槽，修改之后的代码
            如下：
                @see com.jasmine.数据结构.散列表哈希表结构.开放寻址法解决碰撞.IntSet2

                但是这种实现 Remove() 函数的方法有个很大的问题。想象一下，如果依次添加 0、1、2、3、4、5、6、7、8、9,
            然后再 Remove 0、1、2、3、4、5、6、7、8，这时再调用 Contains(0)，此函数会依次检查 _values[0]、_values[1]..._values[9]，这是完全无法
            接受的！这个问题先放一放.
                线性探查法虽然比较容易实现，但是它有一个叫做<一次群集>（primary clustering）的问题。就像本文开篇所讨论的，如果 7、8、9 号座位已被占
            用，下一个上车的旅客，无论他的票是7号、8号还是9号，都会被安排去坐10号；下一个上车的旅客，无论他的票是7号、8号、9号还是10号，都会被安排
            去坐11号……如果有 i 个连续被占用的槽，下一个空槽被占用的概率就会是 (i + 1)/m，就像血栓一样，一旦堵住，就会越堵越厉害。这样，使用线性探
            查法，很容易产生一长串连续被占用的槽，导致 Contains() 函数速度变慢。
                对于线性探查法，由于初始位置 LH(k, 0) = H(k) 确定了整个探查序列，所以只有 m 种不同的探查序列。

            2). <二次探查>
                可以在发生碰撞时，不像线性探查那样探查下一个紧挨着的槽，而是多偏移一些，以此缓解一次群集的问题。二次探查（quadratic probing）让这
            个偏移量依赖 i 的平方：
                h(k, i) = (h'(k) + c1i + c2i^2) mod m
                其中，c1 和 c2 是不为0的常数。例如，如果取 c1 = c2 = 1，二次探查的散列函数为：

                函数为:
                private int QH(int value, int i){
                    return (H(value) + i + i * i) % 10;
                }
                @see com.jasmine.数据结构.散列表哈希表结构.开放寻址法解决碰撞.IntSet3

                对于数值 7，QH() 给出的探查序列是 7、9、3、9……由于初始位置 QH(k, 0) = H(k) 确定了整个探查序列，所以二次探查同样只有 m 种不同的探查序列。
            通过让下一个探查位置以 i 的平方偏移，不容易像线性探查那样让被占用的槽连成一片。但是，由于只要探查的初始位置相同，探查序列就会完全相同，所以会
            连成一小片、一小片的，这一性质导致一种程度较轻的群集现象，称为<二次群集>（secondary clusering）。


            3). <双重散列>
                造成线性探查法和二次探查法的群集现象的罪魁祸首是一旦初始探查位置相同，整个探查序列就相同。这样，一旦出现碰撞，事情就会变得更糟。
            是什么造成一旦初始探查位置相同，整个探查序列就相同呢？是因为线性探查法和二次探查法都是让后续的探查位置基于初始探查位置（即 H(k)）向后偏移几
            个位置，而这个偏移量，不管是线性的还是二次的，都仅仅是 i 的函数，但是只有 k 是不同的对不对？所以必须想办法让偏移量是 k 的函数才行。以线性探
            查为例，要想办法让 LH(k, i) 是 k 和 i 的函数，而不是 H(k) 和 i 的函数。
                -------------------------------------------------------------------------
                我们试着把线性探查
                H(k) = k % 10
                LH(k, i) = (H(k) + i) % 10
                改造一下，先试试把 k 乘到 i 上面去，即
                H(k) = k % 10
                LH(k, i) = (H(k) + i * k) % 10

                这有效果吗？很不幸，
                LH(k, i) = (H(k) + i * k) % 10
                         = (H(k) + i * (k%10) % 10
                         = (H(k) + i * H(k)) % 10
                         = (H(k) * (1 + i)) % 10
                结果 LH(k, i) 还是 H(k) 和 i 的函数。
                -------------------------------------------------------------------------
                再试试把 k 加到 i 上，即
                H(k) = k % 10
                LH(k, i) = (H(k) + i + k) % 10
                这个怎么样？
                LH(k, i) = (H(k) + i + k) % 10
                         = (H(k) + i + k%10) % 10
                         = (H(k) + i + H(k)) % 10
                         = (2*H(k) + i) % 10
                太不幸了，LH(k) 仍然是 H(k) 和 i 的函数。好像怎么折腾都不行,除非把 H(K) 变成乘法散列法.
                -------------------------------------------------------------------------
                或者使用<双重散列法>(double hashing)：
                h(k, i) = (h1(k) + i*h2(k)) mod m

                其中 h1(k) 和 h2(k) 是两个不同的散列函数。例如可以让
                h1(k) = k mod 13
                h2(k) = k mod 11
                h(k, i) = (h1(k) + i*h2(k)) mod 10
                这样，h(7, i) 产生的探查序列是 7、4、1、8、5……
                h(20, i) 产生的探查序列是 7、6、5、4、3……
                这回终于达到了初始探查位置相同，但是后续探查位置不同的目标。
                -------------------------------------------------------------------------
            　　h2(k) 的设计很有讲究，搞不好会无法探查到每个空槽。以刚刚实现的 h(k, i) 为例，h(6, i) 的探查序列是“6、2、8、4、0、6、2、8、4、0”，
            如果恰巧数组中的“6、2、8、4、0”这几个位置都被占用了，将会导致程序在还有空槽的状态下抛出“集合溢出”的异常。要避免这种情况，要求 h2(k) 与 m 必须
            互质。可以看一看如果 h2(k) 与 m 不是互质的话，为什么会有无法探查数组的所有的槽的后果。
                @see 互质数

                例如 h2(6)=6 与 10 有公约数2，把它们代入 h(k, i)：
                h(6, i) = (h1(6) + i * h2(6)) mod 10
                        = (6 + i * 6) mod 10
                        = (6 + (i * 6) mod 10) mod 10
                        = (6 + 2*((i*3) mod 5)) mod 10
                由于 (i*6) mod 5 只有 5 个不同的值，所以 h(6, i) 也只有 5 个值。而 h(16, i) = (3 + 5*((i*5) mod 2)) mod 10 只有2个值，真是太糟糕了。
            　　要想让 h2(k) 与 m 互质，有2种方法。一种方法是让 m 为 2 的幂，并且设计一个总是产生奇数的 h2(k)，利用的是奇数和 2 的 m 次幂总是互质的原理。
            另一种方法是让 m 为质数，并设计一个总是产生比 m 小的正整数的 h2(k)。可以这么实现后一种方法：首先使用上一篇实现的 GetPrime() 函数取得一个合适
            的质数作为 m，然后让
                h1(k) = k mod m
                h2(k) = 1 + (k mod (m-1))
                在 h2(k) 里之所以要把 (k mod (m-1)) 加上个 1 是为了让 h2(k) 永不为0。因为 h2(k) 为 0 会让 i 不起作用，一旦正巧 h1(k) 产生碰撞就无法取
            得下一个空槽了。
                @see com.jasmine.数据结构.散列表哈希表结构.开放寻址法解决碰撞.IntSet4

    ────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────



    三. 解决遗留问题,完善整个散列表

        经过前面2点，我们也来到了第3阶段。让我们深吸一口气，把遗留下来的这几个问题全部搞定吧。
        　　1）能不能支持所有的对象而不仅限于整数？
        　　2）如何支持所有整数而不只是正整数？
        　　3）被删除了的槽仍然占用查找时间。
        　　4）随着时间的推移，被标记为碰撞的槽越来越多，怎么办？
        　　5）必须在创建容器的时候指定大小，不能自动扩张。
        　　6）只是一个 HashSet，而不是HashTable。

        要想支持所有对象而不只是整数，就需要一个能把各种类型的对象变换成整数的方法.Java的工程师们当然想到了这个问题,而所有的对象都可能存放到hash表中于是,他们给了所有类一
    个方法.
        @see Object
        在Java的Object类中有一个方法:
            public native int hashCode();

        根据这个方法的声明可知，该方法返回一个int类型的数值，并且是本地方法，因此在Object类中并没有给出具体的实现.
        对于包含容器类型的程序设计语言来说，基本上都会涉及到hashCode。hashCode方法的主要作用是为了配合基于散列的集合一起正常运行，这样的散列集合包括HashSet、HashMap以
    及HashTable。

        那么java是如何生成具体的hash值的呢?
        还有hashCode方法被重写后,hash值又是如何计算的呢?这个我们留在后面再说.让我们先解决上面的几个问题



        1). 能不能支持所有的对象而不仅限于整数？
            现在我们只用Java已经为我们封装好的hashCode生成方法.来重写IntSet4,解决第一个问题
            @see com.jasmine.数据结构.散列表哈希表结构.支持任何对象.HashSet1

        2). 让 HashSet 支持所有整数
            hashCode() 的返回值是 int 类型，也就是说可能为负数。这时，因为数组的大小 m 是正数，所以由 k mod m 计算得出的数组下标也为负数，这可不行。
            例如: Integer i = -100; i.hashCode() = -100;
            -100作为下标会报ArrayIndexOutOfBoundsException错误

            解决方法是先hashCode() 的返回值变换成正数再赋值给 k，HashTable 做法是：
            @see java.util.Hashtable.private void addEntry(int hash, K key, V value, int index);

            tab = table;
            hash = key.hashCode();
            index = (hash & 0x7FFFFFFF) % tab.length;

            @see com.jasmine.数据结构.散列表哈希表结构.支持任何对象.HashSet2
            这样如果添加-17就会和2147483631发生碰撞,因为两个数计算后的index相同,但实际添加的数总是比较连贯,所以这个缺点也不太容易造成太大的性能问题。

            (1).为什么要hash & 0x7FFFFFFF呢?
                我认为不简单的是让寻找的数组下标为负数,随便实现一种算法都可以使,比如Math.ABS(),但为什么不用呢?
                我认为这样也会让h(k) = k mod m 中k尽量少的重复,为的是可以减少碰撞的发生.




            ───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            注:
            &为按位与运算
            参加运算的两个数据，按二进制位进行“与”运算,注意,二进制运算实际均为补码运算
            运算规则：0&0=0;  0&1=0;   1&0=0;    1&1=1;
                  即：两位同时为“1”，结果才为“1”，否则为0
            例如：
                3  & 5          即
                0000 0011
                0000 0101
                ───────────────────────────────
                00000001  因此，3&5的值得1
                ----------------------------------------------------------------------------------
                15 & 0x7FFFFFFF 即
                01111111111111111111111111111111
                                            &&&&
                00000000000000000000000000001111
                ────────────────────────────────
                00000000000000000000000000001111 因此，15 &0x7FFFFFFF  的值得15
                ----------------------------------------------------------------------------------
                -17 & 0x7FFFFFFF 即
                01111111111111111111111111111111
                                           &&&&&
                10000000000000000000000000010001
                11111111111111111111111111101110 即反码
                11111111111111111111111111101111 即补码
                ────────────────────────────────
                01111111111111111111111111101111 即 2147483631,可用System.out.println(0x7fffffff & -17)来检测手工计算是否准确
                因此，-17 & 0x7FFFFFFF 的值得2147483631


            | 为按位或运算
            参加运算的两个数据，按二进制位进行“与”运算,注意,二进制运算实际均为补码运算
            运算规则：0|0=0;  0|1=1;   1|0=1;    1|1=1;
                  即：两位同时为“1”，结果才为“1”，否则为0
            例如：
                -15 & 0x80000000 即
                10000000000000000000000000000000
                                            ||||
                10000000000000000000000000001111
                11111111111111111111111111110001 补码
                ────────────────────────────────
                11111111111111111111111111110001 补码
                11111111111111111111111111110000 反码
                10000000000000000000000000001111 源码 因此，-15 & 0x80000000 的值得 -15
                ----------------------------------------------------------------------------------
                15 & 0x80000000 即
                10000000000000000000000000000000
                                            ||||
                00000000000000000000000000001111
                ────────────────────────────────
                10000000000000000000000000001111 计算完是补码
                10000000000000000000000000001110 反码
                11111111111111111111111111110001 源码 因此，15 & 0x80000000 的值得-2147483663
            ───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────


        3). 减少已删除的槽对查找时间的影响
            我们在上一篇举过这样一个例子：假设有一个容量 m 为10 的 HashSet，先依次添加 0、1、2、3、4、5、6、7、8、9，然后再删除 0、1、2、3、4、5、
        6、7、8，这时调用 Contains(0)，此函数会依次检查 values[0]、values[1]...values[9]，也就是把整个数组遍历了一遍！为什么会这样呢？因为我们
        在删除一个数字的时候，由于不知道这个数字的后面是否还有一个因与它碰撞而被安排到下一个空槽的数字，所以我们不敢直接把它设为 null。为了解决这个问
        题，需要一种方法可以指出每个槽是否发生过碰撞。最直接的方法是增加一个 bool 类型的变量，不过为了节省空间，我们将利用在解决问题(2)的时候预留出来
        的 k 的最高位。如果新添加的项与某个槽发生了碰撞，就把那个槽的碰撞位设为1。有了碰撞位，就可以知道：
                1）如果碰撞位为0，说明要么没发生过碰撞，要么它是碰撞链的最后一个槽.
                2）如果碰撞位为1，说明它不是碰撞链的最后一个槽。
            对于碰撞位为0的槽，删除时可以直接把 Key 设为 null；对于碰撞位为1的槽，因为它不是碰撞链的最后一个槽，所以在删除时还是不能把它的 Key 设为
        null，而是设为 buckets 表示已删除，并且要保留 k 的最高位为 1，把 k 的其它位设为0。由于我们其实是把一个 int 类型的变量 _k 当成了一个 bool
        类型的变量和一个正整数类型的变量来用，所以要先改造一下 Bucket。






















































        下面是HotSpot JVM中生成hash散列值的实现：

            static inline intptr_t get_next_hash(Thread * Self, oop obj) {
                intptr_t value = 0 ;
                if (hashCode == 0) {
                    // This form uses an unguarded global Park-Miller RNG,
                    // so it's possible for two threads to race and generate the same RNG.
                    // On MP system we'll have lots of RW access to a global, so the
                    // mechanism induces lots of coherency traffic.
                    value = os::random() ;
                } else if (hashCode == 1) {
                    // This variation has the property of being stable (idempotent)
                    // between STW operations.  This can be useful in some of the 1-0
                    // synchronization schemes.
                    intptr_t addrBits = intptr_t(obj) >> 3 ;
                    value = addrBits ^ (addrBits >> 5) ^ GVars.stwRandom ;
                } else if (hashCode == 2) {
                    value = 1 ;            // for sensitivity testing
                } else if (hashCode == 3) {
                    value = ++GVars.hcSequence ;
                } else if (hashCode == 4) {
                    value = intptr_t(obj) ;
                } else {
                    // Marsaglia's xor-shift scheme with thread-specific state
                    // This is probably the best overall implementation -- we'll
                    // likely make this the default in future releases.
                    unsigned t = Self->_hashStateX ;
                    t ^= (t << 11) ;
                    Self->_hashStateX = Self->_hashStateY ;
                    Self->_hashStateY = Self->_hashStateZ ;
                    Self->_hashStateZ = Self->_hashStateW ;
                    unsigned v = Self->_hashStateW ;
                    v = (v ^ (v >> 19)) ^ (t ^ (t >> 8)) ;
                    Self->_hashStateW = v ;
                    value = v ;
                }
                value &= markOopDesc::hash_mask;
                if (value == 0) value = 0xBAD ;
                assert (value != markOopDesc::no_hash, "invariant") ;
                TEVENT (hashCode: GENERATE) ;
                return value;
            }



     */
}
