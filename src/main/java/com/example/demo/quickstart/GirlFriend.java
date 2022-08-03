package com.example.demo.quickstart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GirlFriend {
    // @Autowired: Auto resolve DI
    // @Autowired
    // @Qualifier: Specify a 'bikini' bean (Bikini class) for Outfit
    // @Qualifier("dress")
    public Outfit outfit;

    public GirlFriend() {
        // this.outfit = new Dress();
    }

    @Autowired
    public GirlFriend(@Qualifier("dress") Outfit outfit) {
    // public GirlFriend(Outfit outfit) {
        this.outfit = outfit;
    }
}
