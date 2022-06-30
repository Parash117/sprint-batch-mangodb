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
@Document("accountMasterIdentifiers")
public class AccountIdentifiers {

    private String _id;

    private String accountIdentifiers;

    private Long cycleCode;

    private String cycleDate;

}
