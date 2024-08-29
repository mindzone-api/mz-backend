package com.mindzone.dto.response;

import com.mindzone.model.therapy.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientSessionResponse extends SessionResponse {

    private String PatientObservations;
    private List<File> patientAttatchments;
}
