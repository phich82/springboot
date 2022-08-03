package com.example.demo.quickstart.other_packages;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.example.demo.quickstart.Outfit;

// Define a bean name as 'bikini'
@Component("bikini")
// @Primary: prefer/priority to get this class when there are many implementation classes
// @Primary
public class Bikini implements Outfit {
    @Override
    public void wear() {
        System.out.println("I'm wearing the bikini...");
    }
}
