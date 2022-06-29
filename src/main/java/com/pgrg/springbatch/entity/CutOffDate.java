package com.pgrg.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("cycleSchedule")
public class CutOffDate {
    private String _id;
    private String cycleCode;
    private String month;
    private String processingDate;
    private String year;
}
