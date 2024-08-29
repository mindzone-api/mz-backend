package com.mindzone.dto.request;

import com.mindzone.model.therapy.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionalSessionRequest extends SessionRequest{

    private String professionalObservations;
    private List<File> professionalAttatchments;
    private Double patientMood;
}
