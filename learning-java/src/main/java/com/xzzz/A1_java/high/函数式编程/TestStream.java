package com.xzzz.A1_java.high.函数式编程;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Java8版本
 */
public class TestStream {
    /**
     Stream的操作符大体上分为两种：中间操作符和终止操作符

     中间操作符
     1. map
        转换操作符,把比如A->B,这里默认提供了转int,long,double的操作符.
        mapToInt
        mapToLong
        mapToDouble

     2. flatMap
        拍平操作比如把 int[]{2,3,4} 拍平变成 2,3,4 也就是从原来的一个数据变成了3个数据,这里默认提供了拍平成
        flatmapToInt
        flatmapToLong
        flatmapToDouble

     3. limit
        限流操作,比如数据流中有10个我只要出前3个就可以使用

     4. distint
        去重操作,对重复元素去重,底层使用了equals方法.

     5. filter
        过滤操作,把不想要的数据过滤.

     6. peek
        挑出操作,如果想对数据进行某些操作,如：读取、编辑修改等.

     7. skip
        跳过操作,跳过某些元素.
        可以理解为与limit意思相反

     8. sorted
        排序操作,对元素排序,前提是实现Comparable接口,当然也可以自定义比较器.


     终止操作符
            数据经过中间加工操作,就轮到终止操作符上场了;终止操作符就是用来对数据进行收集或者消费的,数据到了终止操作这里就不会向
        下流动了,终止操作符只能使用一次.

     1. collect

     2. count

     3. findFirst,findAny

     4. noneMatch,allMatch,anyMatch

     5. min,max

     6. reduce

     7. forEach,forEachOrdered

     8. toArray
     */

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,3,4,5,6};
        List<Game> games = getGames();
//        Stream<Game> stream1 = games.stream();                                // 1. 顺序流
//        Stream<Game> stream2 = games.stream();                                // 2. 并行流
//        IntStream intStream = Arrays.stream(arr);                             // 3. 整形流
//        Stream<String> stream3 = Stream.of("1","2","3","4");                  // 4. of创建
        // Stream.iterate(0,t->t+5).forEach(System.out::println);          // 每隔5个数取一个,从0开始,无限循环
        // Stream.iterate(0,t->t+5).limit(5).forEach(System.out::println); // 每隔5个数取一个,从0开始,只取前五个


        /* ============================================================================================================
         * 排序
         * ========================================================================================================= */
        // 普通执行
        System.out.println("====================== 普通执行 =====================");
        games.forEach(game -> game.setId(game.getId() + 100));
        List<Game> l2 = games.stream().map(game -> {
            game.setId(300);
            return game;
        }).collect(Collectors.toList());

        l2.forEach(item -> {
            System.out.println(item.getId());
        });




        /* ===========================================================================================================
         * 分组
         * ======================================================================================================== */
        System.out.println("====================== 分组 =====================");
        // 例1 : 根据字段分组,结果为Map
        Map<Integer, List<Game>> map = games.stream().collect(Collectors.groupingBy(Game::getType));
        System.out.println("type为1的数量 : " + map.get(1).size());
        for (Map.Entry<Integer, List<Game>> entry : map.entrySet()) {
            System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());
        }

        // 分组相加
        System.out.println("============ 分组相加 ============");
        Map<Integer, DoubleSummaryStatistics> collect = games.stream().collect(Collectors.groupingBy(Game::getType, Collectors.summarizingDouble(Game::getAmt)));

        collect.forEach((k,v) -> System.out.println(k + " : " + v.getSum()));

        /* ============================================================================================================
         * 过滤
         * 将某个值的顾虑掉
         * ========================================================================================================= */


        /* ============================================================================================================
         * 相加
         * ========================================================================================================= */
        System.out.println("====================== 某个字段相加 =====================");
        // 例1 : 集合中的某个字段相加
        Double sum = games.stream().mapToDouble(Game::getAmt).sum();
        System.out.println("相加的和为 : " + sum);



        /* ============================================================================================================
         * 提取
         * ========================================================================================================= */
        System.out.println("====================== 提取 =====================");
        // 例1 : 提取对象中的某个字段
        // 方法1
        List<Integer> list = games.stream().map(Game::getId).collect(Collectors.toList());
        System.out.println(Arrays.toString(list.toArray()));
        // 方法2
        System.out.println(Arrays.toString(games.stream().map(Game::getId).toArray()));



        /* =========================================================================================================
         * 判断
         * 判断集合中的字符串是否包含某个字符
         * ========================================================================================================= */
        System.out.println("====================== 判断 =====================");
        // 例1 : 判断集合中的字符串是否包含某个字符
        String requestUrl = "/logout";
        List<String> l = new ArrayList<>();
        l.add("/logout");
        l.add("/login");
        boolean exist = l.stream().anyMatch(requestUrl::contains);     // 判断字符串是否存在
        boolean notExist = l.stream().noneMatch(requestUrl::contains); // 判断字符串是否不存在


        /* ============================================================================================================
         * 转换
         * ========================================================================================================= */
        // list 转 map
        Map<Integer,Game> gameMap;
        gameMap = games.stream().collect(Collectors.toMap(Game::getId, game -> game));



        /* ============================================================================================================
         * 排序
         * ========================================================================================================= */
        System.out.println("====================== 排序 =====================");
        List ups   = games.stream().sorted(Comparator.comparing(Game::getAmt)).collect(Collectors.toList()); // 升序
        List downs = games.stream().sorted(Comparator.comparing(Game::getAmt).reversed()).collect(Collectors.toList()); // 降序


        System.out.println(ups);
    }


    private static List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        games.add(new Game(1,"美国末日",200.0,1));
        games.add(new Game(2,"GTA5",   100.0,2));
        games.add(new Game(3,"神秘海域",200.0,1));
        games.add(new Game(4,"极品飞车",150.0,3));
        games.add(new Game(5,"实况足球",300.0,4));
        return games;
    }


    static class Game {
        private Integer id;
        private String name;
        private Double amt;
        private Integer type;

        Game(Integer id, String name, Double amt, Integer type) {
            this.id = id;
            this.name = name;
            this.amt = amt;
            this.type = type;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getAmt() {
            return amt;
        }

        public void setAmt(Double amt) {
            this.amt = amt;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

}
