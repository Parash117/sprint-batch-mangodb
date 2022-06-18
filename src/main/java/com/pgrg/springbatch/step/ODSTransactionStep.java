package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.processor.ODSTransactionProcessor;
import com.pgrg.springbatch.reader.ODSTransactionReader;
import com.pgrg.springbatch.writer.ODSItemWriter;
import com.pgrg.springbatch.writer.OSDTranscationWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ODSTransactionStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ODSTransactionReader odsTransactionReader;
    @Autowired
    private ODSTransactionProcessor odsTransactionProcessor;
    @Autowired
    private OSDTranscationWriter osdTranscationWriter;
    @Autowired
    private ODSItemWriter odsItemWriter;

    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .<ODSTransactionMessage, ODSTransactionMessage>chunk(20)
                .reader(odsTransactionReader.reader())
                .processor(odsTransactionProcessor)
                .writer(odsItemWriter)
                .build();
    }
}
