package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.processor.AccountIdentifierFiservProcessor;
import com.pgrg.springbatch.reader.subreaders.AccountIdentifierReader;
import com.pgrg.springbatch.writer.CoBrandCycleWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AccountIdentifierStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private AccountIdentifierReader accountIdentifierReader;
    @Autowired
    @Qualifier("acc-id-processor")
    private AccountIdentifierFiservProcessor accountIdentifierFiservProcessor;
    @Autowired
    @Qualifier("cobrand-fiserv-writer")
    private CoBrandCycleWriter coBrandCycleWriter;

    public Step stepForAccId(String cycleCode) {
        return stepBuilderFactory.get("stepForAccountId")
                .<AccountMaster, ODSTransactionMessage>chunk(20)
                .reader(accountIdentifierReader.reader(cycleCode))
                .processor(accountIdentifierFiservProcessor)
                .writer(coBrandCycleWriter)
                .build();
    }
}
