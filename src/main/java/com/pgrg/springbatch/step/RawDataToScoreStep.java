package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionRaw;
import com.pgrg.springbatch.entity.RawData;
import com.pgrg.springbatch.processor.RawDataProcessor;
import com.pgrg.springbatch.reader.RawDataReader;
import com.pgrg.springbatch.writer.RawToScoreWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RawDataToScoreStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private RawDataReader rawDataReader;
    @Autowired
    @Qualifier("raw-data-processor")
    private RawDataProcessor rawDataProcessor;
    @Autowired
    @Qualifier("raw-to-score-writer")
    private RawToScoreWriter rawToScoreWriter;

    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .<RawData, ODSTransactionMessage>chunk(20)
                .reader(rawDataReader.reader())
                .processor(rawDataProcessor)
                .writer(rawToScoreWriter)
                .build();
    }

}
