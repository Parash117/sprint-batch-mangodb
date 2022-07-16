package com.pgrg.springbatch;


import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;

public class Test1 {

    @Test
    public void printThisOnceTest(){
        Date date = new Date(Instant.now().toEpochMilli());

        System.out.println("asdasd");
    }

}
