package com.example.learning.springbootwebbasics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  
  @GetMapping("/hello")
  public String sayHello() {
    return "Hello from Spring Boot Web Basics!";
  }

  @GetMapping("/greeting")
  public String getGreeting() {
    return "Greetings, Learning World!";
  }
}
