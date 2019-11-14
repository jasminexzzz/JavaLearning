package com.jasmine.learingsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequestMapping("/test")
public class LearingSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearingSpringbootApplication.class, args);
	}

	@GetMapping("/hi")
	public String hi(@RequestParam("name") String name){
	    return "你好 : " + name;
    }

}
