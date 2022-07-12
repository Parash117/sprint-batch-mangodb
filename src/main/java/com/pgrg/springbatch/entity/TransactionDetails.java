package com.pgrg.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("transactionDetails")
public class TransactionDetails {
    private Object _id;

    private String emAccountNumber;

    private Long transactionAmount;

    private Long merchantID;

    private List<Bonus> bonus;

    private String transactionPostedDate;

    private String transactionDate;

    private String cycledForFiserv;

    private String cycledForPartner;
}

