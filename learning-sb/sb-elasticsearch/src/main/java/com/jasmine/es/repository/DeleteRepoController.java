//package com.jasmine.es.repository;
//
//import com.jasmine.es.repository.dto.UserDoc;
//import com.jasmine.es.repository.service.UserMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author wangyf
// * @since 0.0.1
// */
//@RestController
//@RequestMapping("/es/repo")
//public class DeleteRepoController {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @DeleteMapping(value = "/all")
//    public Object delAll() {
//        userMapper.deleteAll();
//        return "全部删除成功";
//    }
//
//    @DeleteMapping(value = "/byid")
//    public Object delById(@RequestBody UserDoc user) {
//        userMapper.deleteById(user.getUserId());
//        return String.format("根据id [%s] 删除成功",user.getUserId());
//    }
//}
