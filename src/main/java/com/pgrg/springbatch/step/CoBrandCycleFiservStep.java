package com.pgrg.springbatch.step;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import com.pgrg.springbatch.processor.CoBrandCycleProcessor;
import com.pgrg.springbatch.reader.CoBrandCycleReader;
import com.pgrg.springbatch.writer.CoBrandCycleWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CoBrandCycleFiservStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private CoBrandCycleReader coBrandCycleReader;
    @Autowired
    @Qualifier("raw-data-processor")
    private CoBrandCycleProcessor coBrandCycleProcessor;
    @Autowired
    @Qualifier("cobrand-fiserv-writer")
    private CoBrandCycleWriter coBrandCycleWriter;

    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .<CoBrandAccountMaster, ODSTransactionMessage>chunk(20)
                .reader(coBrandCycleReader.reader())
                .processor(coBrandCycleProcessor)
                .writer(coBrandCycleWriter)
                .build();
    }

}
