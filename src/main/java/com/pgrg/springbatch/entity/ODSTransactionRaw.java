package com.pgrg.springbatch.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ODSTransactionRaw {

    private Long id;

    private String crn;

    private Double transactionAmount;

    private Double pointsEarned;

    private Integer merchantID;

    private String bonusCode;

    private String transactionPostedDate;

    private String transactionDate;

    private String cycleDate;

    private String cycledForFiserv;

    private String cycledForChoice;

}
