package com.pgrg.springbatch.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("ods_transaction_fiserv")
public class ODSTransactionMessage {

    private Long _id;

    private String emAccountNumber;

    private Long bonusEarn;

    private String partnerMerchantCategoryCode;

    private String cycleDate;

    private String processedDate;

    private String partnerConfirmationNo;

    private Audit audit = new Audit();

    @Transient
    private List<Bonus> bonusList;


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ODSTransactionMessage)) {
            return false;
        }
        ODSTransactionMessage ods = (ODSTransactionMessage) obj;
        return this._id.equals(ods._id);
    }

}
