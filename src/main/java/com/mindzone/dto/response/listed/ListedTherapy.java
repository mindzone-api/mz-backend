package com.mindzone.dto.response.listed;

import com.mindzone.enums.TherapyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListedTherapy implements Serializable {

    private String id;
    private String patientId;
    private String professionalId;
    private TherapyStatus therapyStatus;
    private boolean active;
}
