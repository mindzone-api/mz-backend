package com.mindzone.dto.response;

import com.mindzone.model.therapy.SessionFile;
import com.mindzone.model.Homework;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionalSessionResponse extends SessionResponse{

    private String professionalObservations;
    private List<SessionFile> professionalAttatchments;
    private Homework currentHomeworkState;
    private Double patientMood;
}
