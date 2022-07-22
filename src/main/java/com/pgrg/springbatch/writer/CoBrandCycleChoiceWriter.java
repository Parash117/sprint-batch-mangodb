package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.Audit;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Qualifier("cobrand-choice-writer")
public class CoBrandCycleChoiceWriter implements ItemWriter<ODSTransactionMessageForChoice> {

    @Autowired
    private BaseRepo<ODSTransactionMessageForChoice> baseRepo2;


    @Override
    public void write(List<? extends ODSTransactionMessageForChoice> items) throws Exception {
        List<ODSTransactionMessageForChoice> odsItemWriterList = (List<ODSTransactionMessageForChoice>) items;
        List<ODSTransactionMessageForChoice> odsTransactionMessageList = new ArrayList<>();
        odsItemWriterList.parallelStream().forEach(x -> {
            x.getBonusList().parallelStream().forEach( y ->
                    odsTransactionMessageList.add(
                            ODSTransactionMessageForChoice.builder()
                            .emAccountNumber(x.getEmAccountNumber())
                            .cycleDate(x.getCycleDate())
                            .partnerMerchantCategoryCode(y.getPartnerMerchantCategoryCode())
                            .bonusEarn(y.getBonusScore())
                            .processedDate(x.getProcessedDate())
                            .audit(new Audit())
                            .build()
                    )
            );
        });

        baseRepo2.bulkInsert(odsTransactionMessageList, ODSTransactionMessageForChoice.class);
    }
}
