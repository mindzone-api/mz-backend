package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionnaireStatisticsResponse {

    private Double avgMoodBetweenDates;
    private Double overallAvgMood;

}
