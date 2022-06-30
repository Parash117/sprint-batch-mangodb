package com.pgrg.springbatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.entity.TransactionDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public class RawJsonFileReader {
    ObjectMapper objectMapper;

    public RawJsonFileReader() {
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public List<TransactionDetails> getAccountMaster(String fileName) throws IOException {
        String json = new String(Files.readAllBytes(Path.of(fileName)));
        List<TransactionDetails> object = objectMapper.readValue(json, new TypeReference<List<TransactionDetails>>() {
        });
        return object;
    }

    public Date getCutOffDate(String fileName) throws IOException {
        String json = new String(Files.readAllBytes(Path.of(fileName)));
        List<CutOffDate> cutOffDates = objectMapper.readValue(json, new TypeReference<List<CutOffDate>>() {});
//
//        List<String> emAccounts = cutOffDates.stream().filter(x->
//                DateUtils.isSameDay(x.getDate(), new Date())
//        ).map(x->
//                x.getEmAccountNumber()).distinct().collect(Collectors.toList());

        return null;
    }

}
