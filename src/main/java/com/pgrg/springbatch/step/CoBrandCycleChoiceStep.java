package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.processor.CoBrandCycleChoiceProcessor;
import com.pgrg.springbatch.processor.CoBrandCycleProcessor;
import com.pgrg.springbatch.reader.CoBrandCycleChoiceReader;
import com.pgrg.springbatch.reader.CoBrandCycleReader;
import com.pgrg.springbatch.writer.CoBrandCycleChoiceWriter;
import com.pgrg.springbatch.writer.CoBrandCycleWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CoBrandCycleChoiceStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private CoBrandCycleChoiceReader coBrandCycleChoiceReader;
    @Autowired
    @Qualifier("coBrand-cycle-processor")
    private CoBrandCycleChoiceProcessor coBrandCycleChoiceProcessor;
    @Autowired
    @Qualifier("cobrand-choice-writer")
    private CoBrandCycleChoiceWriter coBrandCycleChoiceWriter;

    public Step stepOneForChoice() {
        return stepBuilderFactory.get("stepOneForChoice")
                .<CoBrandAccountMaster, ODSTransactionMessage>chunk(20)
                .reader(coBrandCycleChoiceReader.reader())
                .processor(coBrandCycleChoiceProcessor)
                .writer(coBrandCycleChoiceWriter)
                .build();
    }

}
