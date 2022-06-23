package com.pgrg.springbatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.entity.RawData;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RawJsonFileReader {
    ObjectMapper objectMapper;

    public RawJsonFileReader() {
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public List<RawData> getAccountMaster(String fileName) throws IOException {
        String json = new String(Files.readAllBytes(Path.of(fileName)));
        List<RawData> object = objectMapper.readValue(json, new TypeReference<List<RawData>>() {
        });
        return object;
    }

    public Date getCutOffDate(String fileName) throws IOException {
        String json = new String(Files.readAllBytes(Path.of(fileName)));
        List<CutOffDate> cutOffDates = objectMapper.readValue(json, new TypeReference<List<CutOffDate>>() {});

        List<String> emAccounts = cutOffDates.stream().filter(x->
                DateUtils.isSameDay(x.getDate(), new Date())
        ).map(x->
                x.getEmAccountNumber()).distinct().collect(Collectors.toList());

        return null;
    }

}
