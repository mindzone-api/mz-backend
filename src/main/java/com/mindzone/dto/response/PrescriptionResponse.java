package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrescriptionResponse implements Serializable {

    private String therapyId;
    private String file;
    private String medicationName;
    private String quantity;
    private String howOften;
    private Date until;

}
