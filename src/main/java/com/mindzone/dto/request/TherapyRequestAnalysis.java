package com.mindzone.dto.request;

import com.mindzone.enums.TherapyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TherapyRequestAnalysis {

    private TherapyStatus status;
    private String denialJustification;
}
