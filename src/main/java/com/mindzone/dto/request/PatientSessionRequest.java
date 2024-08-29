package com.mindzone.dto.request;

import com.mindzone.model.therapy.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientSessionRequest extends SessionRequest {

    private String PatientObservations;
    private List<File> patientAttatchments;
}
