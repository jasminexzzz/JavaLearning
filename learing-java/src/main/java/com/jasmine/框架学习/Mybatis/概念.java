package com.jasmine.框架学习.Mybatis;

/**
  * @author : jasmineXz
  */
public class 概念 {
    /**

      一. Mapper XML 映射文件 :
      1. 常用元素有:
         select      : 映射查询语句.
         insert      : 映射插入语句.
         update      : 映射更新语句.
         delete      : 映射删除语句.
         sql         : 可被其他语句引用的可重用语句块.
         cache       : 给定命名空间的缓存配置.
         cache-refo  : 其他命名空间缓存配置的引用.
         resultMap   : 最复杂也是最强大的元素,用来描述如何从数据率结果集中加载对象.
         提示:
         parameterMap: 已废弃! 老式风格的参数映射.
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        1). select元素 <select></select>
            例如:
             <select id="selectUser" parameterType="int" resultType="hashmap">
                SELECT   FROM TB_USER WHERE ID = #{id}
             </select>

            解释:
            这个语句被称作selectUser
            接受一个int (或Integer) 类型的参数.
            并返回一个HashMap 类型的对象,HashMap中的键是列名,值便是结果行中的对应值.

            注意 : 参数符号#{id},这是告诉MyBatis创建一个预处理语句参数.通过JDBC,这样的一个参数在SQL中会由一个“?”来标识,并被
            传递到一个新的预处理语句中.
            以上MyBatis配置文件执行时会生成如下JDBC 代码:
             string selectUser = "SELECT   FRON TB_USER WHERE ID=?";
             Preparedstatement ps = conn.prepareStatement(selectUser) ;
             ps.setInt(1,id);

             select 元素的属性描述如下:
             id              : 在命名空间中唯一的标识符,可以被用来引用这条语句.

             parameterType   : 将会传入这条语句的参数类的完全限定名或别名.这个属性是可选的,因为MyBatis 可以通过TypeHandler 推
                               断出具体传入语句的参数,默认值为unset.

             resultType      : 从这条语句中返回的期望类型的类的完全限定名或别名.注意如果是集合情形,那应该是集合可以包含的类型,
                               而不能是集合本身.返回时可以使用resultType或resultMap,但不能同时使用.

             resultMap       : 外部resultMap 的命名引用.结果集的映射是MyBatis 最强大的特性,对其有一个很好的理解的话,许多复杂
                               映射的情形都能迎刃而解.返回时可以使用resultMap 或resultType,但不能同时使用.

             flushCacheo     : 如果设置为true,则任何时候只要语句被调用,都会导致本地缓存和二级缓存都被清空,默认值为false.

             useCache        : 如果设置为true,将会导致本条语句的结果被二级缓存,在select 元素当中默认值为true.

             timeout         : 这个设置是在抛出异常之前,驱动程序等待数据库返回请求结果的秒数.默认值为unset (依赖驱动).

             fetchSize       : 这是尝试影响驱动程序每次批量返回的结果行数和这个设置值相等.默认值为unset (依赖驱动).

             statementType   : 值为STAT EMENT.PREPARED 或CALLABLE.这会让MyBatis分别使用JDBC 中的Statement、PreparedStatement
                               或CallableStatement,默认值为PREPARED.

             resultSetType   : 结果集的类型,值为FORWARD_ONLY、SCROLL_SENSITIVE 或SCROLLINSENSITIVE,默认值为unset (依赖驱动).

             databaseld      : 如果配置了databascldProvider,MyBatis 会加载所有的不带databaseld 或匹配当前databaseld的语句; 如果
                               带或者不带的语句都有,则不带的会被忽略.

             resultOrdered   : 这个设置仅针对嵌套结果select 语句适用: 如果为true,就是假设包含了嵌套结果集或是分组了,这样的话当返
                               回一个主结果行的时候,就不会发生对前面结果集引用的情况.这就使得在获取嵌套的结果集时不至于导致内存不够
                               用.默认值为false.

             resultSets      : 这个设置仅对多结果集的情况适用,它将列出语句执行后返回的结果集并给每个结果集一个名称,名称是逗号分隔的.


      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

        2). insert、update、delete元素
            <insert></insert>
            <update></update>
            <delete></delete>

             insert,update 和delete元素的属性大多和select一致,它们特有的属性描述如下:
             useGeneratedKeys : (仅对insert 和update 有用) 这会令MyBatis使用JDBC的getGeneratedKeys方法来获取由数据库内部生成的主
                                键(比如,像MySQL和SQL Server这样的关系数据库管理系统的自动递增字段),默认值为false
             keyProperty      : (仅对insert和update 有用) 唯一标记一个属性,MyBatis会通过getGeneratedKeys的返回值或者通过insert语句的
                                selectKey子元素设置它的键值,默认为unset.如果希望得到多个生成的列,也可以是逗号分隔的属性名称列表.
             keyColumn        : (仅对insert和update 有用) 通过生成的键值设置表中的列名,这个设置仅对某些数据库(像PostgreSQL) 是必须的,当
                                主键列不是表中的第一列时需要设置.如果希望得到多个生成的列,也可以是逗号分隔的属性名称列表.

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
             下面是insert.update和delete语句的示例:
             插入:
             <insert id="insertUser">
                 insert into TB_USER (id,username,password,email,address)
                 values (#{id},#{username},#{password},#{emai},#{address})
             </insert>
             修改:
             <update id="updateUser" >
                 update TB_USER set
                    username = #{username},
                    password = #{password},
                    email = #{email},
                    address = #{address}
                 where id = #{id}
             </update>
             删除:
             <delete id="deleteUser"
                delete from TB_USER where id = #{id}
             </delete>
            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             而插入语句的配置规则更加丰富,因为在插入语句执行时很多时候是需要返回插入成功的数据生成的主键值的,所以<insert.../>元素里面
             有一些额外的属性和子元素用来处理主键的生成,而且根据数据库的主键生成策略不同,配置也有多种方式.

             首先,如果数据库支持自动生成主键的字段(比如MySQL和SQLServer),那么可以设置useGeneratedKeys="true”,然后再把keyProperty设
             置到目标属性上就可以了(一般会设置到id属性上).例如,如果上面的TB_USER 表已经对id 使用了自动生成的列类型,那么语句可以修改为:
             <insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
                 insert into TB_USER (username,password,email,address)
                 values (#{username},#{password},#{email},#{address})
             </insert>


             对于不支持自动生成类型的数据库(比如Oracle) 或可能不支持自动生成主键的JDBC驱动来说,MyBatis有另外一种方法来生成主键.
             <insert idm"insertUser">
                 <selectKey keyProperty="id" resultType="int" order="BEFORE">
                 select SEQUENCE TB_USER.nextval as id from dual
                 </selectKey>
                 insert into TB_USER
                 (id,username,password,email,address)
                 values
                 (#{id},#{username},#{password},#{email},#{address})
             </insert>
             在上面的示例中,selectKey元素将会首先运行,其通过查询SEQUENCB序列,TB_USER的id会被设置,然后插入语句会被调用.


             selectKey 元素描述如下:
             <selectKey keyProperty= "id" resultType="int" order="BBFORE" statementType="PREPARED">

             keyProperty   : selectKey 语句结果应该被设置的目标属性(一般会设置到id 属性上).如果希望得到多个生成的列,也可以是
                             逗号分隔的属性名称列表.

             keyColumn     : 匹配属性的返回结果集中的列名称.如果希望得到多个生成的列,也可以是逗号分隔的属性名称列表.

             resultType    : 结果的类型.MyBatis 通常可以推算出来,但是为了更加确定,建议明确写出.MyBatis 允许任何简单类型用作
                             主键的类型,包括字符串.如果希望作用于多个生成的列,则可以使用一个包含期望属性的Object 或一个Map.

             order         : 可以被设置为BEFORE 或AFTER.如果设置为BEFORE,那么它会首先选择主键,设置keyProperty 然后执行插入语
                             句.如果设置为AFTER,那么先执行插入语句,然后是selectKey元素.

             statementType : 与前面相同,MyBatis支持STATEMENT、PREPARED 和CALLABLE语句的映射类型,分别代表Statement,PreparedStatement
                             和CallableStatement 类型.


━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        3). sql元素 <sql></sql>

             sql元素可以被用来定义可重用的SQL代码段,可以包含在其他语句中.它可以被静态地(在加载参数时) 参数化.不同的属性值通过包含的实例发
             生变化.例如:
             <sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
             这个SQL片段可以被包含在其他语句中,例如:
             <select id="selectUsers" resultType="map">
                 select
                 <include refid="userColumns" ><property name="alias" value="t1" /></include>
                 from some_table tl
             </select>

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
             属性值可以用于包含的refid属性或者包含的字句里面的属性,例如:
             <sql id="sometable">
                ${prefix}Table
             </sql>

             <sql id="someinclude">
                 from
                 <include refid="${include_target}"/>
             </sql>

             <select id="select" resultType="map">
                 select
                 fieldl,field2,field3
                 <include refid="someinclude">
                     <property name="prefix" value="some"/>
                     <property name=" include_target "value="sometable" />
                 </include>
             </select>

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
             上述语句意思为 :
             <select id="select" resultType="map">
                 select fieldl,field2,field3
                   from sometable
             </select>


━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        4). 参数
            前面的所有语句中所见到的都是简单参数的例子,实际上参数是MyBatis非常强大的元素.对于简单参数的使用,大多数情况下参数都很少,例如:
             <select id="selectUsers" parameterType="int" resultType="User">
                 select id,username,password
                 from users
                 where id = #{id}
             </select>

            上面的这个示例说明了一个非常简单的命名参数映射.参数类型被设置为int,这样这个参数就可以被设置成任何内容.原生的类型或简单数据类型
            (比如整型和字符串),因为没有相关属性,它会完全用参数值来替代.但是,如果传入一个复杂的对象(比如User),行为就会有一点不同了.例如:
             <insert id="insertUser" parameterType="User">
                 insert into users (id,username,password)
                 values (#{id},#{username},{password})
             </insert>
            如果User 类型的参数对象被传递到了语句中,如#{id}语句则会查找参数对象User的id属性,#{username}和#{password}也是一样,然后将它们
            的值传入预处理语句的参数中.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        5). resultMap元素 <resultMap></resultMap>
            resultMap元素是MyBatis中最重要最强大的元素.它的作用是告诉MyBatis将从结果集中取出的数据转换成开发者所需要的对象.
             <select id="selectUser" resultType="map">
                SELECT   FROM TB_USER
             </select>

            resultType="map"表示返回的数据是一个Map 集合(使用列名作为key,列值作为value).
            虽然数据被封装成Map集合返回,但是Map集合并不能很好地描述一个领域模型.我们更加建议使用JavaBeans或POJOs (Plain Old Java Objects,
            普通Java 对象) 来作为领域模型描述数据.例如:
             <select id="selectUser" resultType="cn.mybatis.domain.User">
                SELECT * FROM TB_USER
             </select>

            默认情况下,MyBatis 会将查询到的数据的列和需要返回的对象(User)的属性逐一进行匹配赋值,但是如果查询到的数据的列和需要返回的对象(User)
            的属性不一致,则MyBatis就不会自动赋值了,这时,可以使用resultMap 进行处理.
             <resultMap id="userResultMap" type="cn.mybatis.domain.User >
                 <id property="id" column="user_id" />
                 <result property="name" column="user_name"/>
                 <result property="sex" column="user_sex"/>
                 <result property="age" column="user_age"/>
                 < !-- 关联映射 -->
                 <association property="roleList" column="user_Id" javaType="cn.mybatis.domain.Role" select="selectRoles" />
             </resultMap>
             <!-- resultMap-"userResultMap"表示引用上面的resultMap进行数据库表和返回类型对象的映射-->
             <select id="selectUser" resultMap="userResultMap">
                SELECT * FROM TB_USER
             </select>
             <!-- resultMap-"userResultMap"表示引用上面的resultMap进行数据库表和返回类型对象的映射-->
             <select id="selectRoles" resultMap="cn.mybatis.domain.Role">
                SELECT * FROM TB_ROLE
             </select>



             (1). 上面使用了一个新的元素<resultMap.../>,该元素常用属性如下:
                  id   : resultMap 的唯一标示符.
                  type : resultMap 实际返回的类型.

             (2). 上面使用了<resultMap.../>的两个子元素id和result.
                  id : 表示数据库表的主键
                       property : 表示数据库列映射到返回类型的属性.
                       column   : 属性表示数据库表的列名.
                  result : 表示数据库表的普通列
                       property : 表示数据库列映射到返回类型的属性.
                       column   : 属性表示数据库表的列名

             (3). <association.../>元素的解释如下:
                  property : 表示返回类型User的属性名roleList.
                  column   : 表示数据库表的列名,User表和Role表的关联字段.
                  javaType : 表示该属性对应的类型名称,本示例是一个Role类型.
                  select   : 表示执行一条查询语句,将查询到的数据封装到property所代表的类型对象当中.上面的selectRoles就是执行一条SQL
                             语句,将用户的user_Id作为参数查询对应的班级信息.


             在实际项目开发中,还有更加复杂的情况,例如执行的是一个多表查询语句,而返回的对象关联到另一个对象,此时简单的映射已经无法解决问题,必须
             使用<resultMap.../>元素来完成关联映射.



================================================================动态sql================================================================




     二. 动态sql
         1. if 语句
            1). 例子:
                <select id="selectUsers" resultType="User">
                    SELECT * FROM USER WHERE USER_STATE = '1'
                    <if test="id != null">
                    AND ID = #{id}
                    </if>
                <select/>
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
         2. choose(when otherwise) 语句
            1). 例子:
             <select id="selectUsers" resultType="User">
                 SELECT * FROM USER WHERE USER_STATE = '1'
                 <choose>
                     <when test="id != null">
                         AND ID = #{id}
                     <when/>
                     <when test="name != null">
                         AND NAME = #{name}
                     <when/>
                     <otherwise>
                         AND SEX = '男'
                     </otherwise>
                 <choose/>
             <select/>
            2). 说个人话:
                有when执行when,全都没有就执行otherwise
                相当于:
                if(id != null) {

                }
                if(name != null) {

                }
                if(id == null && name == null){

                }
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
         3. where 语句
            1). 例子:
                <select id="selectUsers" resultType="User">
                    SELECT * FROM USER
                    <where>
                        <if test="state != null">
                            USER_STATE = #{state}
                        </if>
                        <if test="id != null">
                            AND ID = #{id}
                        </if>
                    <where/>
                <select/>
                where元素知道只有在一个以上的if条件有值的情况下才去插入WHERE子句.而且,若最后的内容是“AND”或“OR”开头,则where元素也知道如何将
                它们去除.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
         4. set 元素
            1). 例子:
                <update id="updateUser" parameterType="cn.mybatis.domain.User">
                    update TB_USER
                    <set>
                        <if test="passWord != null">password=#{password} ,</if>
                        <if test="name != null">name=#{name},</if>
                        <if test="sex != null">sex=#{sex},</if>
                        <if test="age != null">age=#{age},</if>
                        <if test="state != null">state=#{state}</if>
                    </set>
                    where userId = #{id}
                </update>
                set元素会动态前置SET关键字,同时也会消除无关的逗号,因为使用了条件语句之后很可能就会在生成的赋值语句的后面留下这些逗号.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
         5. foreach 元素
            关于动态SQL另外一个常用的操作就是需要对一个集合进行遍历,通常发生在构建IN条件语句时.
            1). 例子:
            <select id="selectUsers" resultType="User">
                SELECT * FROM USER
                <where>
                    <if test="idlist != null">
                        <foreach item="item" index="index" collection="idlist" open="(" separator="," close=") ">
                            #{item}
                        </foreach>
                    </if>
                <where/>
            <select/>

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
         6. bind 元素
            1). 例子:
            <select id="selectUsers" resultType="User">
                <bind name="pattern" value="'%'+_parameter.getName() + '%'" />
                SELECT * FROM USER
                <where>
                    <if test="name != null">
                        name LIKE #{pattern}
                    </if>
                <where/>
            <select/>
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        1. trim 语句
======================================================== MyBatis事务管理 =============================================================

    事务基础知识:
    @see  com.jasmine.Java高级.数据库.事务.概念

    三. Mybatis管理事务
        Mybatis管理事务是分为两种方式:
            1. 使用JDBC的事务管理机制,就是利用java.sql.Connection对象完成对事务的提交
            2. 使用MANAGED的事务管理机制,这种机制mybatis自身不会去实现事务管理,而是让程序的容器(JBOSS,WebLogic)来实现对事务的管理.


            Mybatis提供了一个事务接口Transaction,以及两个实现类jdbcTransaction和ManagedTransaction,当spring与Mybatis一起使用时,
        spring提供了一个实现类SpringManagedTransaction.
        1. Transaction 接口:
            提供的抽象方法有获取数据库连接getConnection,提交事务commit,回滚事务rollback和关闭连接close,源码如下:
            @see org.apache.ibatis.transaction.Transaction

        2. JdbcTransaction 实现类:
            Transaction的实现类,通过使用jdbc提供的方式来管理事务,通过Connection提供的事务管理方法来进行事务管理.
            @see org.apache.ibatis.transaction.jdbc.JdbcTransaction

        3. ManagedTransaction 实现类:
            通过容器来进行事务管理,所有它对事务提交和回滚并不会做任何操作
            @see org.apache.ibatis.transaction.managed.ManagedTransaction

        4. SpringManagedTransaction 实现类:
            它其实也是通过使用JDBC来进行事务管理的,当spring的事务管理有效时,不需要操作commit/rollback/close,spring事务管理会自动帮我们完成.
            @see org.mybatis.spring.transaction.SpringManagedTransaction


        事务工厂:
            mybatis的事务是由TransactionFactory创建的,利用典型的简单工厂模式来创建Transaction,如下图:
                                                        TransactionFactory
                                                                |
                            ┌───────────────────────────────────┼───────────────────────────────────┐
                 JdbcTransactionFactory             ManagedTransactionFactory           SpringManagedTransactionFactory
                            |                                   |                                   |
                    创建JdbcTransaction              创建ManagedTransaction                创建SpringManagedTransaction
                            └───────────────────────────────────┼───────────────────────────────────┘
                                                                | 三个对象实现
                                                           Transaction


            TransactionFactory          : 抽象事务工厂生产方法
            JdbcTransactionFactory      : 实现TransactionFactory接口,用于生产JdbcTransaction的工厂类
            ManagedTransactionFactory   : 实现TransactionFactory接口,用于生产ManagedTransaction的工厂类

            Transaction                 : 封装事务管理方法的接口
            JdbcTransaction             : 实现Transaction接口,只是对事务进行了一层包装、实际调用数据库连接Connection的事务管理方法
            ManagedTransaction          : 实现Transaction没有对数据库连接做任何事务处理、交由外部容器管理







========================================================== Mybatis 一级,二级缓存 ====================================================
    参考资料:
    @see 美团公众号
    https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651747419&idx=2&sn=a7c25803179504b7232c0d6777fe4831&chksm=bd12ad168a652400
    95187adf72f82d0c7f2fcff52987e97fa385065eb63ff843e45dc896189e&mpshare=1&scene=23&srcid=0708i2B3zxQQJCepUAUUrZFw#rd
    @see test.J2EE.java.com.jasmine.框架学习.Mybatis.StudentMapperTest

    一. 一级缓存:
        使用方法:
            <?xml version="1.0" encoding="UTF-8" ?>
            <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
            <configuration>
                <settings>
                    //开启一级缓存
                    //SESSION (默认) : 在一个MyBatis会话中执行的所有语句,都会共享这一个缓存.
                    //STATEMENT : 缓存只对当前执行的这一个Statement有效.
                    <setting name="localCacheScope" value="SESSION"/>
                </settings>
            </configuration>

        1. 一级缓存结论:
            1). 同一个数据库会话中,相同的语句只有第一次会查询数据库,后续会使用一级缓存.
            2). 同一个数据库回话,在修改数据后,一级缓存会失效.
            3). 一级缓存只在数据库会话内部共享.

        2. 一级缓存流程图:

                                                                      localCache.getObject(key)
                ┌─────── select ──────┐    ┌──────── query ───────┐    ┌──────────┴──────────┐
                |                     ↓    |                      ↓    |                     ↓
            Client                  SqlSession                   Executor                  Cache                  database
                ↑                     |    ↑                      |    ↑                     |                     ↑   |
                └─── result chache ───┘    └───── result ───┐     |    └────── result ───────┘                     |   |
                                                            |     |                                                |   |
                                                            ↑     ↓                                                |   |
                                                       if result != null                                           |   |
                                                            ↑     |                                                |   |
                                                            ├ yes ┤                                                |   |
                                                            |     no                                               |   |
                                                            |     └──────────────────── dispatch ──────────────────┘   |
                                                            └────────────────────────── return ────────────────────────┘

            1). Client 调用 SqlSession.select 方法
            2). SqlSession 调用 Executor.query 方法
            3). Executor调用query时,从PerpetualCache.getObject取出数据
            4). 如果PerpetualCache取出的数据为空,则查找数据库,否则返回数据
            ───── 缓存取值为空 ───────────────────────────────────────
            5). 为空,Executor的实现类调用doQuery从数据库查询数据
            6). 查询后删除缓存,再将结果插入到缓存中
            7). 返回数据

        3. 源码分析
            1). SqlSession : 对外提供了用户和数据库之间交互需要的所有方法,隐藏了底层的细节.默认实现类是DefaultSqlSession.
                @see org.apache.ibatis.session.SqlSession ---------------------- 接口.
                @see org.apache.ibatis.session.defaults.DefaultSqlSession ------ 默认实现类.
                @see org.apache.ibatis.session.defaults.DefaultSqlSessionFactory 构造DefaultSqlSession


            2). Executor : SqlSession向用户提供操作数据库的方法,但和数据库操作有关的职责都会委托给Executor.
                @see org.apache.ibatis.executor.Executor ----------------------- 接口.
                @see org.apache.ibatis.executor.BaseExecutor ------------------- 一级缓存相关实现.

                1). BaseExecutor : BaseExecutor是一个实现了Executor接口的抽象类,定义若干抽象方法,在执行的时候,把具体的操作委托给子类进行
                                   执行.

                    在一级缓存的介绍中提到对Local Cache的查询和写入是在Executor内部完成的.在阅读BaseExecutor的代码后发现Local Cache是
                    BaseExecutor内部的一个成员变量,如下代码所示.

                    public abstract class BaseExecutor implements Executor {
                        protected ConcurrentLinkedQueue<DeferredLoad> deferredLoads;
                        protected PerpetualCache localCache;
                    }

                    其中 PerpetualCache 下面有介绍


            3). Cache :  MyBatis中的Cache接口,提供了和缓存相关的最基本的操作.
                @see org.apache.ibatis.cache.Cache
                有若干个实现类,使用装饰器模式互相组装,提供丰富的操控缓存的能力,部分实现类如下图所示.


                1). PerpetualCache : 是对Cache接口最基本的实现,其实现非常简单,内部持有HashMap,对一级缓存的操作实则是对HashMap的操作.
                    如下代码所示.
                public class PerpetualCache implements Cache {
                    private String id;
                    private Map<Object, Object> cache = new HashMap<Object, Object>();
                }


            4). 源码执行流程

                (1). 为执行和数据库的交互,首先需要初始化SqlSession,通过DefaultSqlSessionFactory开启SqlSession:
                    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level,
                    boolean autoCommit) {
                        ......
                        ......
                        final Executor executor = configuration.newExecutor(tx, execType);
                        return new DefaultSqlSession(configuration, executor, autoCommit);
                    }

                ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

                (2). 在初始化SqlSesion时,会使用Configuration类创建一个全新的Executor,作为DefaultSqlSession构造函数的参数,创建Executor
                    代码如下所示:

                    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
                        executorType = executorType == null ? defaultExecutorType : executorType;
                        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
                        Executor executor;
                        if (ExecutorType.BATCH == executorType) {
                            executor = new BatchExecutor(this, transaction);
                        } else if (ExecutorType.REUSE == executorType) {
                            executor = new ReuseExecutor(this, transaction);
                        } else {
                            executor = new SimpleExecutor(this, transaction);
                        }
                        // 尤其可以注意这里,如果二级缓存开关开启的话,是使用CahingExecutor装饰BaseExecutor的子类
                        if (cacheEnabled) {
                            executor = new CachingExecutor(executor);
                        }
                        executor = (Executor) interceptorChain.pluginAll(executor);
                        return executor;
                    }

                ────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

                (3). SqlSession创建完毕后,根据Statment的不同类型,会进入SqlSession的不同方法中,如果是Select语句的话,最后会执行到
                    SqlSession的selectList,代码如下所示:

                    @Override
                    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
                        MappedStatement ms = configuration.getMappedStatement(statement);
                        return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
                    }

                ────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

                (4). SqlSession把具体的查询职责委托给了Executor.如果只开启了一级缓存的话,首先会进入BaseExecutor的query方法.代码如下所示:

                    @Override
                    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler)
                    throws SQLException {
                        BoundSql boundSql = ms.getBoundSql(parameter);
                        CacheKey key = this.createCacheKey(ms, parameter, rowBounds, boundSql);//创建缓存的key
                        return this.query(ms, parameter, rowBounds, resultHandler, key, boundSql);
                    }

                ────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

                (5). 在上述代码中,会先根据传入的参数生成CacheKey,进入该方法查看CacheKey是如何生成的,代码如下所示:
                    CacheKey cacheKey = new CacheKey();
                    cacheKey.update(ms.getId());//mapper中标签的id,dao中的方法名
                    cacheKey.update(rowBounds.getOffset());
                    cacheKey.update(rowBounds.getLimit());
                    cacheKey.update(boundSql.getSql());//后面是
                    ......
                    ......
                    cacheKey.update(value);//update了sql中带的参数



                    public class CacheKey implements Cloneable, Serializable {
                        private static final long serialVersionUID = 1146682552656046210L;
                        public static final CacheKey NULL_CACHE_KEY = new NullCacheKey();
                        private static final int DEFAULT_MULTIPLYER = 37;
                        private static final int DEFAULT_HASHCODE = 17;
                        private int multiplier;//用于生成hashcode,初始值是37,做为生成hashcode的基准
                        private int hashcode;//用于生成hashcode,初始值是17,做为生成hashcode的基准,每次调用update方法的时候更新
                        private long checksum;
                        private int count;
                        private List<Object> updateList;

                        public CacheKey() {
                            this.hashcode = 17;
                            this.multiplier = 37;
                            this.count = 0;
                            this.updateList = new ArrayList();
                        }

                        /*
                        更新hashcode
                        17是质子数中一个“不大不小”的存在,如果你使用的是一个如2的较小质数,
                        那么得出的乘积会在一个很小的范围,很容易造成哈希值的冲突.
                        而如果选择一个100以上的质数,得出的哈希值会超出int的最大范围,这两种都不合适.
                        而如果对超过 50,000 个英文单词(由两个不同版本的 Unix 字典合并而成)进行 hash code 运算,
                        并使用常数 31, 33, 37, 39 和 41 作为乘子(cacheKey使用37),每个常数算出的哈希值冲突数都小于7个(国外大神做的测试),
                        那么这几个数就被作为生成hashCode值得备选乘数了
                        * /
                        public void update(Object object) {
                            //
                            int baseHashCode = object == null ? 1 : ArrayUtil.hashCode(object);
                            ++this.count;
                            this.checksum += (long)baseHashCode;
                            baseHashCode *= this.count;

                            //hashcode更新方法
                            //newHashCode = oldHashCode * multiplier(37) + ObjectHashCode
                            this.hashcode = this.multiplier * this.hashcode + baseHashCode;
                            this.updateList.add(object);
                        }
                    }

                ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

                (6). 在上述的代码中,将MappedStatement的Id、sql的offset、Sql的limit、Sql本身以及Sql中的参数传入了CacheKey这个类,最终构成
                    CacheKey.

                    CacheKey : 作为一级缓存HashMap的key存在

                    以下是这个类的内部结构:

                        public class CacheKey implements Cloneable, Serializable {
                            private static final long serialVersionUID = 1146682552656046210L;
                            public static final CacheKey NULL_CACHE_KEY = new NullCacheKey();
                            private static final int DEFAULT_MULTIPLYER = 37; // multiplier 默认值
                            private static final int DEFAULT_HASHCODE = 17;   // hashcode 默认值
                            private int multiplier;                           // 用于生成hashcode,初始值是37,做为生成hashcode的基准
                            // 用于生成hashcode,初始值是17,做为生成hashcode的基准,每次调用update方法的时候更新
                            private int hashcode;
                            // 每次修改hashcode的对象的hashcode值相加,也就是修改元素的hashcode和
                            private long checksum;
                            private int count;                                // 检查次数
                            private List<Object> updateList;                  // 保存每次每次修改CacheKey.hashcode的对象

                            public CacheKey() {
                            this.hashcode = DEFAULT_HASHCODE;
                            this.multiplier = DEFAULT_MULTIPLYER;
                            this.count = 0;
                            this.updateList = new ArrayList();
                        }


                    首先是成员变量和构造函数,有一个初始的hashcode和乘数,同时维护了一个内部的updatelist.在CacheKey的update方法中,会进行一
                    个hashcode和checksum的计算,同时把传入的参数添加进updatelist中.如下代码所示.
                        /*
                        更新hashcode
                        17是质子数中一个“不大不小”的存在,如果你使用的是一个如2的较小质数,
                        那么得出的乘积会在一个很小的范围,很容易造成哈希值的冲突.
                        而如果选择一个100以上的质数,得出的哈希值会超出int的最大范围,这两种都不合适.
                        而如果对超过 50,000 个英文单词(由两个不同版本的 Unix 字典合并而成)进行 hash code 运算,
                        并使用常数 31, 33, 37, 39 和 41 作为乘子(cacheKey使用37),每个常数算出的哈希值冲突数都小于7个(国外大神做的测试),
                        那么这几个数就被作为生成hashCode值得备选乘数了
                         * /
                        public void update(Object object) {
                            //对象的hashcode
                            int baseHashCode = object == null ? 1 : ArrayUtil.hashCode(object);
                            ++this.count;
                            this.checksum += (long)baseHashCode;
                            baseHashCode *= this.count;
                            //hashcode更新方法
                            //newHashCode = oldHashCode * multiplier(37) + ObjectHashCode
                            this.hashcode = this.multiplier * this.hashcode + baseHashCode;
                            this.updateList.add(object);
                        }


                    同时重写了CacheKey的equals方法,代码如下所示:

                        @Override
                        public boolean equals(Object object) {
                            //如果地址相同
                            if (this == object) {
                                return true;
                            //如果不是同一个类
                            } else if (!(object instanceof CacheKey)) {
                                return false;
                            } else {
                                CacheKey cacheKey = (CacheKey)object;//转换类型
                                //hashcode不同
                                if (this.hashcode != cacheKey.hashcode) {
                                    return false;
                                //修改元素的hashcode和
                                } else if (this.checksum != cacheKey.checksum) {
                                    return false;
                                //修改次数不同
                                } else if (this.count != cacheKey.count) {
                                    return false;
                                } else {
                                    //每次修改的Object是否相同
                                    for(int i = 0; i < this.updateList.size(); ++i) {
                                        Object thisObject = this.updateList.get(i);
                                        Object thatObject = cacheKey.updateList.get(i);
                                        if (!ArrayUtil.equals(thisObject, thatObject)) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                    除去hashcode,checksum和count的比较外,只要updatelist中的元素一一对应相等,那么就可以认为是CacheKey相等.只要两条SQL的下
                    列五个值相同,即可以认为是相同的SQL.
                        Statement Id + Offset + Limmit + Sql + Params

                ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

                (7).BaseExecutor的query方法继续往下走,代码如下所示:

                    list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
                    if (list != null) {// 这个主要是处理存储过程用的.
                        handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
                    } else {
                        list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
                    }


                    如果查不到的话,就从数据库查,在queryFromDatabase中,会对localcache进行写入.

                    private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds,
                        ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
                        this.localCache.putObject(key, ExecutionPlaceholder.EXECUTION_PLACEHOLDER);
                        List list;
                        try {
                            list = this.doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
                        } finally {
                            this.localCache.removeObject(key);//删除缓存
                        }
                        this.localCache.putObject(key, list);//插入缓存
                        if (ms.getStatementType() == StatementType.CALLABLE) {
                            this.localOutputParameterCache.putObject(key, parameter);
                        }
                        return list;
                    }


                    在query方法执行的最后,会判断一级缓存级别是否是STATEMENT级别,如果是的话,就清空缓存,这也就是STATEMENT级别的一级缓存无法
                    共享localCache的原因.代码如下所示:

                    if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
                        clearLocalCache();
                    }

                ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
                (8). 在源码分析的最后,我们确认一下,如果是insert/delete/update方法,缓存就会刷新的原因.

                    SqlSession的insert方法和delete方法,都会统一走update的流程,代码如下所示:

                    @Override
                    public int insert(String statement, Object parameter) {
                        return update(statement, parameter);
                    }
                    @Override
                    public int delete(String statement) {
                        return update(statement, null);
                    }

                    update方法也是委托给了Executor执行.BaseExecutor的执行方法如下所示.

                    @Override
                    public int update(MappedStatement ms, Object parameter) throws SQLException {
                        ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());
                        if (closed) {
                            throw new ExecutorException("Executor was closed.");
                        }
                        clearLocalCache();
                        return doUpdate(ms, parameter);
                    }


                    每次执行update前都会清空localCache.


                    至此,一级缓存的工作流程讲解以及源码分析完毕.

                ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

            9). 小结
                (1). MyBatis一级缓存的生命周期和SqlSession一致.
                (2). MyBatis一级缓存内部设计简单,只是一个没有容量限定的HashMap,在缓存的功能性上有所欠缺.
                (2). MyBatis的一级缓存最大范围是SqlSession内部,有多个SqlSession或者分布式的环境下,数据库写操作会引起脏数据,建议设定缓存级别
                     为Statement.

            5). 源码流程图
                @see




    二. 二级缓存:
        使用方法:
            1. 在MyBatis的配置文件中开启二级缓存.
                <?xml version="1.0" encoding="UTF-8" ?>
                <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
                <configuration>
                    <settings>
                        //开启一级缓存
                        //SESSION (默认) : 在一个MyBatis会话中执行的所有语句,都会共享这一个缓存.
                        //STATEMENT : 缓存只对当前执行的这一个Statement有效.
                        <setting name="cacheEnabled" value="true"/>
                    </settings>
                </configuration>

            2.在MyBatis的映射XML中配置cache或者 cache-ref .
                <?xml version="1.0" encoding="UTF-8" ?>
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
                <mapper namespace="mapper.ClassMapper">
                    <cache/>
                </mapper>
                1). cache标签用于声明这个namespace使用二级缓存,并且可以自定义配置:
                    type : cache使用的类型,默认是PerpetualCache,这在一级缓存中提到过.
                    eviction : 定义回收的策略,常见的有FIFO,LRU.
                    flushInterval : 配置一定时间自动刷新缓存,单位是毫秒.
                    size : 最多缓存对象的个数.
                    readOnly : 是否只读,若配置可读写,则需要对应的实体类能够序列化.
                    blocking : 若缓存中找不到对应的key,是否会一直blocking,直到有对应的数据进入缓存.
                2). cache-ref代表引用别的命名空间的Cache配置,两个命名空间的操作使用的是同一个Cache.
                    <cache-ref namespace="mapper.StudentMapper"/>

            在上文中提到的一级缓存中,其最大的共享范围就是一个SqlSession内部,如果多个SqlSession之间需要共享缓存,则需要使用到二级缓存.开启二
        级缓存后,会使用CachingExecutor装饰Executor,进入一级缓存的查询流程前,先在CachingExecutor进行二级缓存的查询,具体的工作流程如下所示.


        1. 二级缓存结论:
            1). 不同的数据库回话但相同的nameSpace,相同的语句只有第一次会查询数据库,后续会使用二级缓存.
            2). 不同的数据库回话但相同的nameSpace,在修改数据后,二级缓存会失效.
            3). 二级缓存可以在不同的namespace中共享,只要引入了其他的命名空间<cache-ref namespace="mapper.StudentMapper"/>,其实使用的就是相
                同的缓存空间.

        2. 二级缓存实例化过程
            首先要说明,mybatis中的缓存使用装饰器模式,根据配置的不同加装不同的装饰,使得缓存具有不同的功能
            具体的装饰链是:
            SynchronizedCache -> LoggingCache -> SerializedCache -> LruCache -> PerpetualCache 实现 Cache
            如果开启二级缓存,会增加一个:TransactionalCache -> SynchronizedCache -> ......

            各个装饰器的功能:
            TransactionalCache : 如果事务提交,对缓存的操作才会生效,如果事务回滚或者不提交事务,则不对缓存产生影响.
            SynchronizedCache  : 同步Cache,实现比较简单,直接使用synchronized修饰方法.
            LoggingCache       : 日志功能,装饰类,用于记录缓存的命中率,如果开启了DEBUG模式,则会输出命中率日志.
            SerializedCache    : 序列化功能,将值序列化后存到缓存中.该功能用于缓存返回一份实例的Copy,用于保存线程安全.
            LruCache           : 采用了Lru算法的Cache实现,移除最近最少使用的key/value.
            PerpetualCache     : 作为为最基础的缓存类,底层实现比较简单,直接使用了HashMap.

            @see org.apache.ibatis.builder.xml.XMLMapperBuilder.cacheElement()

            private void cacheElement(XNode context) throws Exception {
                if (context != null) {
                    // 获取cache标签中的属性
                    String type = context.getStringAttribute("type", "PERPETUAL");
                    Class<? extends Cache> typeClass = this.typeAliasRegistry.resolveAlias(type);
                    String eviction = context.getStringAttribute("eviction", "LRU");
                    Class<? extends Cache> evictionClass = this.typeAliasRegistry.resolveAlias(eviction);
                    Long flushInterval = context.getLongAttribute("flushInterval");
                    Integer size = context.getIntAttribute("size");
                    boolean readWrite = !context.getBooleanAttribute("readOnly", false);
                    boolean blocking = context.getBooleanAttribute("blocking", false);
                    Properties props = context.getChildrenAsProperties();
                    // 创建缓存
                    this.builderAssistant.useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
                }
            }
            这里分别取<cache>中配置的各个属性，关注一下两个默认值：
            1. type表示缓存实现，默认是PERPETUAL，根据typeAliasRegistry中注册的，PERPETUAL实际对应PerpetualCache，这和MyBatis一级缓存是一致的
            2. eviction表示淘汰算法，默认是LRU算法

            @see org.apache.ibatis.builder.MapperBuilderAssistant.useNewCache()
            public Cache useNewCache(Class<? extends Cache> typeClass, Class<? extends Cache> evictionClass, Long flushInterval, Integer size, boolean readWrite, boolean blocking, Properties props) {
                Cache cache =
                    (new CacheBuilder(this.currentNamespace))
                        .implementation((Class)this.valueOrDefault(typeClass, PerpetualCache.class))
                        .addDecorator((Class)this.valueOrDefault(evictionClass, LruCache.class))
                        .clearInterval(flushInterval).size(size)
                        .readWrite(readWrite)
                        .blocking(blocking)
                        .properties(props)
                        .build();//跟踪build方法
                this.configuration.addCache(cache);
                this.currentCache = cache;
                return cache;
            }

            @see org.apache.ibatis.mapping.CacheBuilder.build()
            public Cache build() {
                this.setDefaultImplementations();
                //构建基础的缓存，implementation指的是type配置的值，这里是默认的PerpetualCache。
                Cache cache = this.newBaseCacheInstance(this.implementation, this.id);
                this.setCacheProperties((Cache)cache);
                if (PerpetualCache.class.equals(cache.getClass())) {
                    Iterator var2 = this.decorators.iterator();
                    while(var2.hasNext()) {
                        Class<? extends Cache> decorator = (Class)var2.next();
                        cache = this.newCacheDecoratorInstance(decorator, (Cache)cache);
                        this.setCacheProperties((Cache)cache);
                    }
                    cache = this.setStandardDecorators((Cache)cache);//跟踪此方法
                } else if (!LoggingCache.class.isAssignableFrom(cache.getClass())) {
                    cache = new LoggingCache((Cache)cache);
                }
                return (Cache)cache;
            }


            @see org.apache.ibatis.mapping.CacheBuilder.setStandardDecorators()
            private Cache setStandardDecorators(Cache cache) {
                try {
                    MetaObject metaCache = SystemMetaObject.forObject(cache);
                    if (this.size != null && metaCache.hasSetter("size")) {
                        metaCache.setValue("size", this.size);
                    }

                    //如果配置了flushInterval，那么继续装饰为ScheduledCache，这意味着在调用Cache的getSize、putObject、getObject、
                    //removeObject四个方法的时候都会进行一次时间判断，如果到了指定的清理缓存时间间隔，那么就会将当前缓存清空
                    if (this.clearInterval != null) {
                        cache = new ScheduledCache((Cache)cache);
                        ((ScheduledCache)cache).setClearInterval(this.clearInterval);
                    }

                    //那么继续装饰为SerializedCache，这意味着缓存中所有存储的内存都必须实现Serializable接口
                    if (this.readWrite) {
                        cache = new SerializedCache((Cache)cache);
                    }

                    //在getObject的时候会打印缓存命中率，
                    Cache cache = new LoggingCache((Cache)cache);
                    //将Cache接口中所有的方法都加了Synchronized关键字进行了同步处理
                    cache = new SynchronizedCache(cache);
                    if (this.blocking) {
                        //针对同一个CacheKey，拿数据与放数据、删数据是互斥的，即拿数据的时候必须没有在放数据、删数据
                        cache = new BlockingCache((Cache)cache);
                    }
                    return (Cache)cache;
                } catch (Exception var3) {
                    throw new CacheException("Error building standard cache decorators.  Cause: " + var3, var3);
                }
            }

            1). 如果配置了flushInterval，那么继续装饰为ScheduledCache，这意味着在调用Cache的getSize、putObject、getObject、removeObject四个
                方法的时候都会进行一次时间判断，如果到了指定的清理缓存时间间隔，那么就会将当前缓存清空
            2). 如果readWrite=true，那么继续装饰为SerializedCache，这意味着缓存中所有存储的内存都必须实现Serializable接口
            3). 跟配置无关，将之前装饰好的Cache继续装饰为LoggingCache与SynchronizedCache，前者在getObject的时候会打印缓存命中率，后者将Cache
                接口中所有的方法都加了Synchronized关键字进行了同步处理
            4). 如果blocking=true，那么继续装饰为BlockingCache，这意味着针对同一个CacheKey，拿数据与放数据、删数据是互斥的，即拿数据的时候必须
                没有在放数据、删数据


        3. 源码

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            1).执行query
                public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds,
                    ResultHandler resultHandler,CacheKey key, BoundSql boundSql) throws SQLException {
                    Cache cache = ms.getCache();// 1.会从MappedStatement中获得在配置初始化时赋予的Cache.
                    if (cache != null) {
                        this.flushCacheIfRequired(ms);// 2.然后是判断是否需要刷新缓存
                        if (ms.isUseCache() && resultHandler == null) {
                            this.ensureNoOutParams(ms, parameterObject, boundSql);//处理存储过程
                            List<E> list = (List)this.tcm.getObject(cache, key);// 4.尝试从tcm中获取缓存的列表.
                            if (list == null) {
                                // 5. 如果查询到数据,则调用tcm.putObject方法,往缓存中放入值.
                                list = this.delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
                                this.tcm.putObject(cache, key, list);// 6. 放入待提交缓存中
                            }
                            return list;
                        }
                    }
                    return this.delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
                }

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            2).然后是判断是否需要刷新缓存
                在默认的设置中SELECT语句不会刷新缓存,insert/update/delte会刷新缓存.进入该方法.代码如下所示:
                private void flushCacheIfRequired(MappedStatement ms) {
                    Cache cache = ms.getCache();
                    //一般在构造MappedStatement时创建,默认select不会情况,二insert,update,delete会清空缓存.
                    if (cache != null && ms.isFlushCacheRequired()) {
                        //清空了需要在提交时加入缓存的列表,同时设定提交时清空缓存
                        this.tcm.clear(cache);//TransactionalCacheManager
                    }
                }

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            3). TransactionalCacheManager清空缓存
                TransactionalCacheManager中持有了一个Map,代码如下所示:

                private Map<Cache, TransactionalCache> transactionalCaches = new HashMap<Cache, TransactionalCache>();

                这个Map保存了Cache和用TransactionalCache包装后的Cache的映射关系.

                下面是TransactionalCache:
                TransactionalCache实现了Cache接口,CachingExecutor会默认使用他包装初始生成的Cache,作用是如果事务提交,对缓存的操作才会生
                效,如果事务回滚或者不提交事务,则不对缓存产生影响.

                在TransactionalCache的clear,有以下两句.清空了需要在提交时加入缓存的列表,同时设定提交时清空缓存,代码如下所示:
                public void clear() {
                    this.clearOnCommit = true;//设定,在提及时要清空缓存,也就是说insert,update,delete之后的查询需要重新查询数据库获得数据
                    this.entriesToAddOnCommit.clear();//清空了在提交时需要加入缓存的数据
                }

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            4). 尝试从tcm中获取缓存的列表.
                在getObject方法中,会把获取值的职责一路传递,最终到PerpetualCache.如果没有查到,会把key加入Miss集合,这个主要是为了统计命中率.

                TransactionalCache:
                public Object getObject(Object key) {
                    Object object = this.delegate.getObject(key);//会一直调用到PerpetualCache.getObject来获取缓存
                    if (object == null) {
                        this.entriesMissedInCache.add(key);//统计命中率,未命中放入misscache
                    }
                    return this.clearOnCommit ? null : object;
                }

                this.delegate.getObject会一直调用到
                PerpetualCache:
                public Object getObject(Object key) {
                    return this.cache.get(key);
                }


            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            5). baseExecutor.query : 若缓存中没有值,则查询数据库
            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            6). 放入缓存 : tcm的put方法也不是直接操作缓存,只是在把这次的数据和key放入待提交的Map中.
                最后会调用到TransactionalCache :
                public void putObject(Object key, Object object) {
                    this.entriesToAddOnCommit.put(key, object);
                }

            ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
            7). 最后在commit时才会真正放入缓存中.
                因为我们使用了CachingExecutor,首先会进入CachingExecutor实现的commit方法.
                @Override
                public void commit(boolean required) throws SQLException {
                    delegate.commit(required);
                    tcm.commit();//会把具体commit的职责委托给包装的Executor.主要是看下tcm.commit(),tcm最终又会调用到TrancationalCache.
                }

                会把具体commit的职责委托给包装的Executor.主要是看下tcm.commit(),tcm最终又会调用到TrancationalCache.
                public void commit() {
                    if (clearOnCommit) {
                        delegate.clear();
                    }
                    flushPendingEntries();
                    reset();
                }


                看到这里的clearOnCommit就想起刚才TrancationalCache的clear方法设置的标志位,真正的清理Cache是放到这里来进行的.具体清理的
                职责委托给了包装的Cache类.之后进入flushPendingEntries方法.代码如下所示：
                private void flushPendingEntries() {    for (Map.Entry<Object, Object> entry : entriesToAddOnCommit.entrySet()) {
                    delegate.putObject(entry.getKey(), entry.getValue());
                    }
                    ........
                    ........
                }

                在flushPendingEntries中,将待提交的Map进行循环处理,委托给包装的Cache类,进行putObject的操作.
                后续的查询操作会重复执行这套流程.如果是insert|update|delete的话,会统一进入CachingExecutor的update方法,其中调用了这个函数,
                代码如下所示：
                private void flushCacheIfRequired(MappedStatement ms)

                在二级缓存执行流程后就会进入一级缓存的执行流程,因此不再赘述.

        3. 小结:
            1). MyBatis的二级缓存相对于一级缓存来说,实现了SqlSession之间缓存数据的共享,同时粒度更加的细,能够到namespace级别,通过
                 Cache接口实现类不同的组合,对Cache的可控性也更强.

            2). MyBatis在多表查询时,极大可能会出现脏数据,有设计上的缺陷,安全使用二级缓存的条件比较苛刻.

            3). 在分布式环境下,由于默认的MyBatis Cache实现都是基于本地的,分布式环境下必然会出现读取到脏数据,需要使用集中式缓存将
                 MyBatis的Cache接口实现,有一定的开发成本,直接使用Redis,Memcached等分布式缓存可能成本更低,安全性也更高.































━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
=====================================================================================================================================















































      */
}
