package com.mindzone.dto.request;

import com.mindzone.enums.TherapyModality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyRequest {

    private String patientId;
    private String professionalId;
    private TherapyModality therapyModality;
}
