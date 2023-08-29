package com.example.tempboard;

import com.example.tempboard.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileMockTest {

    @Autowired
    HelloService helloService;

    @Test
    public void testCreate() {

    }
}
