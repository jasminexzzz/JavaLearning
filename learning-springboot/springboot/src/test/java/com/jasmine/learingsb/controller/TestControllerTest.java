package com.jasmine.learingsb.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTest {

    @MockBean
    private TestController testController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHi() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                .get("/test/hi")
                .param("name","333")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
            );

        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testHi1(){
        System.out.println(testController.hi("1"));
    }

    @Test
    public void testHi2(){
        System.out.println(testController.hi("2"));
    }
}
