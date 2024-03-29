package com.example.demo;

import com.example.demo.config.OSSConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTests {

    @Autowired
    private OSSConfig ossConfig;

    @Test
    public void test() {
        System.out.println(ossConfig.getAccessKeyId());
    }
}
