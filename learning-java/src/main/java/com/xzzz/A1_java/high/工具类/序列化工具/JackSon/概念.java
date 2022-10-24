package com.xzzz.A1_java.high.工具类.序列化工具.JackSon;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     对于某些需要忽略的敏感字段(如password),jackson可以过滤字段

     1. 自定义视图来过滤字段
         如
         public class User implements Serializable {
            public interface UserSimpleView{}                        //定义视图
            public interface UserDetailView extends UserSimpleView{} //定义视图

            @JsonView(UserSimpleView.class) //定义字段所属视图
            private String userName;//用户名
            @JsonView(UserDetailView.class) //定义字段所属视图
            private String passWord;//密码
         }

         @RestController
         public class UserController{

             @GetMapping("/user")
             @JsonView(User.UserSimpleView.class) //该方法查询结果序列化时会自动过滤掉未被UserSimpleView注解的字段
             public User getById(@RequestParam("userId")Integer userId){
             return userService.getById(userId);
             }
         }

     2. 用@JsonIgnore注解
        被该注解的字段在所有的序列化过程中都会被忽略

     */
}
