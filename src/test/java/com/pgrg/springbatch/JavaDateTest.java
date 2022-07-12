package com.pgrg.springbatch;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JavaDateTest {

    @Test
    public void dateTest(){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

        Date yesterdayDate = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        System.out.println(yesterdayDate.toString());
    }

}
