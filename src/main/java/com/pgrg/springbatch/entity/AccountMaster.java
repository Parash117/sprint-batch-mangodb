package com.pgrg.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("accountMaster")
public class AccountMaster {
    private String _id;
    private String accountIdentifier;
    private String accountId;
    private Long cycleCode99;
    private Long productCode;
}
