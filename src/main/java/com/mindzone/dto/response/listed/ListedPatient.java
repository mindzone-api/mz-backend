package com.mindzone.dto.response.listed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedPatient implements Serializable {

    private String id;
    private String name;
    private String profilePictureURL;
    private Boolean active;
}
