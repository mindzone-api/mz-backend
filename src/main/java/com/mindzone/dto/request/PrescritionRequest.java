package com.mindzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescritionRequest {

    private String therapyId;
    private String file;
    private String medicationName;
    private String quantity;
    private String howOften;
    private Date until;
}
