package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrescriptionResponse {

    private String file;
    private String medicationName;
    private String quantity;
    private String howOften;
    private Date until;

}
