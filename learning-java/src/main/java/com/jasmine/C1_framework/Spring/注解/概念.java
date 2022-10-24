package com.jasmine.C1_framework.Spring.注解;

public class 概念 {
    /**
     常用注解
     一. Bean配置的相关注解

        @scope bean作用域
            1. singleton : 单例模式
            2. prototype : 每次请求会创建一个新的对象
            3. request   : Web项目中给每一个Http request新建一个Bean的实例
            4. session   : Web项目中给每一个Http session新建一个Bean的实例
            5. global session : portal项目中给每一个global http session新建一个Bean的实例


        @PostConstruct
            被注解的方法会成为初始化方法 Init-method
            执行步骤可查看{@link com.jasmine.C1_framework.Spring.spring生命周期.jpg}


        @PreDestory
            被注解的方法会成为销毁方法 Destory-method
            执行步骤可查看{@link com.jasmine.C1_framework.Spring.spring生命周期.jpg}


======================================================================================================================================
    二. bean的注册
        @Component
            意思该类为一个bean类,被注解的类会被自动注册到Ioc容器

        @Named
            Named和Spring的Component功能相同。
            Named可以有值，如果没有值生成的Bean名称默认和类名相同。

        @Configuration
            意思该类为配置bean的集合
            @see com.jasmine.bean.ConfigurationAnnotationTest


        @Value : @Value("${wx_appid}")
            使用@Value, 可以从properties文件中取值配置值.
            即使给变量赋了初值也会以配置文件的值为准.
            用法 :
                用在被@Component注解的类中,且保存至的配置文件必须已被spring加载
                加载方法 :
                <!-- 配置数据库相关参数properties的属性：${url} -->
                <context:property-placeholder location="classpath:jdbc.properties"/>
            @see com.jasmine.bean.BeanTest


        @Primary
            自动装配若是出现多个bean的实例,则被注解的为首选类,否则将抛出异常
            org.springframework.beans.factory.BeanCreationException: Could not autowire field
            org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type


        @Lazy(true)
            用于指定该Bean是否取消预初始化,用于注解类,延迟初始化.


        @Singleton
            只要在类上加上这个注解，就可以实现一个单例类，不需要自己手动编写单例实现类。


======================================================================================================================================

    三. 自动装配,自动注入
        @Autowired
            先按byType查找Bean,如果发现找到多个bean,再按照byName方式比对,如果还有多个,则报出异常.
            也可以手动指定按byName方注入 @Qualifier("beanName")
            如果要允许null 值,可以设置它的required属性为false,如：@Autowired(required=false)

        @Resource
            先按byName查找Bean,如果找不到bean,再按照byType方式比对,如果找到多个,则报出异常.
            值的注意的是,spring不允许beanName重复,所以无论按byName还是byType如果找到多个,则抛异常.
            该注解是属于javax.annotation.Resource下的,不与spring耦合.


======================================================================================================================================
     四. 其他注解
        @Async

        @ResponseStatus

     五. 请求返回
        @RequestMapping
            处理映射请求.
            value    : 指定请求的实际地址
                value的uri值为以下三类：
                1). 可以指定为普通的具体值；如@RequestMapping(value ="/testValid")
                2). 可以指定为含有某变量的一类值;如@RequestMapping(value="/{day}")
                3). 可以指定为含正则表达式的一类值;如@RequestMapping(value="/{textualPart:[a-z-]+}.{numericPart:[\\d]+}")可以匹配
                    ../chenyuan122912请求。
            method   : 指定请求的method类型， GET、POST、PUT、DELETE等；
            consumes : 指定处理请求的提交内容类型（Content-Type） @RequestMapping(value = "/test", consumes="application/json")处理
                       application/json内容类型
            produces : 指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
            params   : 指定request中必须包含某些参数值是，才让该方法处理。
                        @RequestMapping(value = "/test", method = RequestMethod.GET, params="name=wyf")
                        仅处理请求中包含了名为"name"，值为"wyf"的请求.
            headers  : 指定request中必须包含某些指定的header值，才能让该方法处理请求。

        @RequestBody
            前后端分离中除查询外请求全部以Ajax的方式来做.
            Ajax的请求Content-Type必需是application/json
            需要jackson-databind.jar
            <mvc:annotation-driven/>要配置或直接配置bean
            XXXController.jar在post方式接收数据
            @see com.jasmine.controller.GameController.updGameAndPlatValid

        @RequestParam
            前端参数通过name=value的格式填写

        @PathVariable
            参数通过name/value的格式填写


        @RestController
            @RestController = @Controller + @ResponseBody。

















































     */
}
