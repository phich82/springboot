package com.example.demo.quickstart;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// Dependency Injection
// Using @Component as mark it as a java bean
// @Component("dress"): define a bean name as 'dress'
@Component("dress")
// Specify to initiate an instance
// Default: singleton
// singleton: all instances point to an address on memory
// @Scope("singleton")
// prototype: Every when initiating, it will initiate an different instance (different addresses on memory)
// @Scope("prototype")
// @Primary: prefer/priority to get this class when there are many implementation classes
// @Primary
public class Dress implements Outfit {
    @Override
    public void wear() {
        System.out.println("I'm wearing the dress...");
    }
}
