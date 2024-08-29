package com.mindzone.dto.response;

import com.mindzone.model.therapy.File;
import com.mindzone.model.therapy.Homework;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionalSessionResponse extends SessionResponse{

    private String professionalObservations;
    private List<File> professionalAttatchments;
    private Homework currentHomeworkState;
    private Double patientMood;
}
