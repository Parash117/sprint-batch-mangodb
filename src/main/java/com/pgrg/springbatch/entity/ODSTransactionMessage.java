package com.pgrg.springbatch.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("ods_transaction_fiserv")
public class ODSTransactionMessage {
    private Long uid;

    private Long id;

    private String crn;

    private BigDecimal totalPointsEarned;

    private String bonusCode;

    private String cycleDate;

    private String processedDate;

    private String destinationSystem;

    private String cycledForFiserv;

    private String cycledForChoice;

    private Audit audit = new Audit();

    @Override
    public String toString() {
        return "ODSTransactionMessage{" +
                "id=" + id +
                ", crn='" + crn + '\'' +
                ", totalPointsEarned=" + totalPointsEarned +
                ", bonusCode='" + bonusCode + '\'' +
                ", cycleDate='" + cycleDate + '\'' +
                ", processedDate='" + processedDate + '\'' +
                ", destinationSystem='" + destinationSystem + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ODSTransactionMessage)) {
            return false;
        }
        ODSTransactionMessage ods = (ODSTransactionMessage) obj;
        return this.crn.equals(ods.crn);
    }

}
