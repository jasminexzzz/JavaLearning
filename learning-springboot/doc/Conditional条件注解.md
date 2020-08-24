# SpringBoot Conditional 条件注解

---

而在SpringBoot中的作用就是：当这个注解指定的条件成立的时候，才可以给容器中添加组件，自动配置里面的内容才会生效。




|@Conditional扩展注解|作用（判断当前条件是否满足）|
|---|---|
|@ConditionalOnJava	             | 系统的java版本是否符合要求 |
|@ConditionalOnBean		         | 容器中是否存在指定的Bean|
|@ConditionalOnMissingBean	     | 容器中不存在指定的类|
|@ConditionalOnExpression	     | 满足SpEL表达式指定规范|
|@ConditionalOnClass		     | 在系统中有指定的对应的类|
|@ConditionalOnMissingClass	     | 在系统中没有指定对应的类|
|@ConditionalOnSingleCandidate   | 容器中是否指定一个单实例的Bean，或者找个是一个首选的Bean|
|@ConditionalOnProperty		     | 系统中指定的对应的属性是否有对应的值|
|@ConditionalOnResource		     | 类路径下是否存在指定的资源文件|
|@ConditionalOnWebApplication	 | 当前是Web环境|
|@ConditionalOnNotWebApplication | 当前不是Web环境|
|@ConditionalOnJndi		         | JNDI存在指定项|
