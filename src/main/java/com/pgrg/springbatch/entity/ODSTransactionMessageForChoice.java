package com.pgrg.springbatch.entity;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("ods_transaction_choice")
public class ODSTransactionMessageForChoice {

    private Long _id;

    private String emAccountNumber;

    private Long fdrProductType;

    private Long bonusEarn;

    private String partnerMerchantCategoryCode;

    private String cycleDate;

    private String processedDate;

    private String partnerConfirmationNo;
    @Builder.Default
    private Audit audit = new Audit();

    @Transient
    private List<Bonus> bonusList;
}
