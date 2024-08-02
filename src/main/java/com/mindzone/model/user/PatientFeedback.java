package com.mindzone.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientFeedback implements Serializable {

    private Double stars;
    private String comment;

}
