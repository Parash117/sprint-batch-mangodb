package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import com.pgrg.springbatch.processor.CoBrandCycleChoiceProcessor;
import com.pgrg.springbatch.reader.subreaders.AccountIdentifierReader;
import com.pgrg.springbatch.writer.CoBrandCycleChoiceWriter;
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
    private AccountIdentifierReader accountIdentifierReader;
    @Autowired
    @Qualifier("coBrand-cycle-processor")
    private CoBrandCycleChoiceProcessor coBrandCycleChoiceProcessor;
    @Autowired
    @Qualifier("cobrand-choice-writer")
    private CoBrandCycleChoiceWriter coBrandCycleChoiceWriter;

    public Step stepOneForChoice(String cycleCode) {
        return stepBuilderFactory.get("stepOneForChoice")
                .<AccountMaster, ODSTransactionMessageForChoice>chunk(500)
                .reader(accountIdentifierReader.reader(cycleCode))
                .processor(coBrandCycleChoiceProcessor)
                .writer(coBrandCycleChoiceWriter)
                .build();
    }

}
