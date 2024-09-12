package com.mindzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionnaireStatisticsRequest {

    private Date from;
    private  Date until;
}
