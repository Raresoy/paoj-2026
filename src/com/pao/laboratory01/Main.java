package com.pao.laboratory01;

import com.pao.laboratory01.model.Cat;
import com.pao.laboratory01.model.Dog;

public class Main {
    public static void main(String[] args) {
        Dog dog1 = new Dog("Rex", "maro");
        Dog dog2 = new Dog("Luna", "alb");

        System.out.println(dog1);
        System.out.println(dog2);

        System.out.println("Numele primului caine: " + dog1.getName());
        dog1.setColor("negru");
        System.out.println("Dupa modificare " + dog1);

        Cat cat1 = new Cat("Miti", "portocaliu");
        System.out.println(cat1);

        Dog[] dogs = {dog1, dog2, new Dog("Azorel", "cenusiu")};

        for(Dog d : dogs) {
            System.out.println(d);
        }
    }
}