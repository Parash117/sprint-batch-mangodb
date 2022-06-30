package com.pgrg.springbatch.controller;

import com.pgrg.springbatch.service.startjob.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/trigger-job")
public class JobTriggerController {

    @Autowired
    private JobService jobService;

    @GetMapping(path = "/choice") // Start batch process path For Choice
    public ResponseEntity<String> startBatchChoice() throws ParseException {
        jobService.startJobForChoice();
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }

    @GetMapping(path = "/fiserv") // Start batch process path for Fiserv
    public ResponseEntity<String> startBatchFiserv() throws ParseException {
        jobService.startJobForAccountIdentifier();
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }

}
