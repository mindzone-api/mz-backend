package com.mindzone.dto.request;

import com.mindzone.model.therapy.SessionFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientSessionRequest extends SessionRequest {

    private String PatientObservations;
    private List<SessionFile> patientAttatchments;
}
