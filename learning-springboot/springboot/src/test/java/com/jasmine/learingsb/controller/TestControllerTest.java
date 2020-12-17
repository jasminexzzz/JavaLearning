package com.jasmine.learingsb.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHi() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
            MockMvcRequestBuilders
            .get("/test/hi")
            .param("name","111")
        )
        .andExpect(status().isOk())
        .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());


        MvcResult mvcResult1 = mockMvc.perform(
            MockMvcRequestBuilders
            .get("/test/hi")
            .param("name","222")
        )
        .andExpect(status().isOk())
        .andReturn();
        System.out.println(mvcResult1.getResponse().getContentAsString());

    }

}
