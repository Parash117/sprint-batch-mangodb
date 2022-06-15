package com.pgrg.springbatch.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ODSTransactionMessage {
    private Long uid;

    private Long id;

    private String crn;

    private Double totalPointsEarned;

    private String bonusCode;

    private String cycleDate;

    private String processedDate;

    private String destinationSystem;

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
}
