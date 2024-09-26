package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionnaireStatisticsResponse implements Serializable {

    private Double avgMoodBetweenDates;
    private Double overallAvgMood;

}
