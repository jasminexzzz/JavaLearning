package com.jasmine.项目.电商项目;

/**
 * @author : jasmineXz
 */
public class A_购物车 {
    /**
     加入购物车
        判断用户是否登录
        未登陆 :
            1. 获取cookie中的购物车
            2. 没有则创建购物车对象
            3. 将商品加入购物车
            4. 保存购物车到cookie中
            5. 将cookie放入浏览器
        已登录 :
            1. 将cookie中的购物车取出
            2. 没有购物车先创建
            3. 将购物车传入后台
            4. 比较购物车和redis中购物车商品
            5. 未添加进redis的添加进去
            6. 取出redis购物车内容
            7. 清空cookie
            8. 返回前台

     */
}
