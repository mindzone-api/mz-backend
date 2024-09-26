package com.mindzone.dto.response.listed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListedPrescription implements Serializable {

    private Date createdAt;
    private String medicationName;
    private String file;
    private Date until;
}
