package com.pgrg.springbatch.service.startjob;

import java.text.ParseException;

public interface JobService {

    void startJobForFiserv() throws ParseException;

    void startJobForChoice() throws ParseException;

    void startJobForAccountIdentifier() throws ParseException;
}
