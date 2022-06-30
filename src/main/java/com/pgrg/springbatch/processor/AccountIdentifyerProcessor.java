package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.AccountIdentifiers;
import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("acc-id-processor")
public class AccountIdentifyerProcessor implements ItemProcessor<AccountMaster, AccountIdentifiers> {

    @Override
    public AccountIdentifiers process(AccountMaster item) throws Exception {

        return AccountIdentifiers.builder()
                ._id(item.get_id())
                .accountIdentifiers(item.getAccountIdentifier())
                .cycleCode(item.getCycleCode99())
                .build();
    }
}
