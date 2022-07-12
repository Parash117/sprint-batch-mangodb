package com.pgrg.springbatch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;


@Data
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
