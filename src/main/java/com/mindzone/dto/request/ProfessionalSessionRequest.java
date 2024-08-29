package com.mindzone.dto.request;

import com.mindzone.model.therapy.SessionFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionalSessionRequest extends SessionRequest{

    private String professionalObservations;
    private List<SessionFile> professionalAttatchments;
    private Double patientMood;
}
