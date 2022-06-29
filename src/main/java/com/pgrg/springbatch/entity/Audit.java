package com.pgrg.springbatch.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("audit")
public class Audit {

    private String createdBy;

    @CreatedDate
    private Timestamp createdDate;

    private String modifiedBy;

    @LastModifiedDate
    private Timestamp modifiedDate;

    @Version
    private Integer version;
}
