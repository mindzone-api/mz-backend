package com.mindzone.dto.response.listed;

import com.mindzone.enums.TherapyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListedTherapy {

    private String id;
    private String patientId;
    private String professionalId;
    private TherapyStatus therapyStatus;
    private boolean active;
}
