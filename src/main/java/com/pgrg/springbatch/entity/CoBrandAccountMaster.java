package com.pgrg.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoBrandAccountMaster {
    private Long id;

    private String emAccountNumber;

    private BigDecimal transactionAmount;

    private Long merchantID;

    private List<Bonus> bonus;

    private String transactionPostedDate;

    private String transactionDate;

    private String cycledForFiserv;

    private String cycledForChoice;
}

