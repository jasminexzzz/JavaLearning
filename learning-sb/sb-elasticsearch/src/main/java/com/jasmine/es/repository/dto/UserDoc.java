//package com.jasmine.es.repository.dto;
//
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * @author wangyf
// * @since 0.0.1
// */
//@Data
//@Document(indexName = "user_index")
//public class UserDoc implements Serializable {
//    @Id
//    private String userId;
//    @Field
//    private String name;
//    @Field
//    private String address;
//    @Field
//    private UserRoleDTO role;
//    @Field
//    private List<PermDTO> perms;
//}
