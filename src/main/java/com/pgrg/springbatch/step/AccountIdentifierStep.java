package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.AccountIdentifiers;
import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.processor.AccountIdentifyerProcessor;
import com.pgrg.springbatch.processor.CoBrandCycleChoiceProcessor;
import com.pgrg.springbatch.reader.CoBrandCycleChoiceReader;
import com.pgrg.springbatch.reader.subreaders.AccountIdentifierReader;
import com.pgrg.springbatch.writer.AccountIdentifierWriter;
import com.pgrg.springbatch.writer.CoBrandCycleChoiceWriter;
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
    private AccountIdentifyerProcessor accountIdentifyerProcessor;
    @Autowired
    @Qualifier("acc-id-writer")
    private AccountIdentifierWriter accountIdentifierWriter;

    public Step stepForAccId(String cycleCode) {
        return stepBuilderFactory.get("stepForAccountId")
                .<AccountMaster, AccountIdentifiers>chunk(20)
                .reader(accountIdentifierReader.reader(cycleCode))
                .processor(accountIdentifyerProcessor)
                .writer(accountIdentifierWriter)
                .build();
    }
}
