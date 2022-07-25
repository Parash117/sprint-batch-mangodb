package com.pgrg.springbatch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("cycleSchedule")
public class CutOffDate {

    private String _id;

    private String cycleCode;

    private String month;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date processingDate;

    private String year;

}
