package com.mindzone.dto.response.listed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedPatient {

    private String id;
    private String name;
    private String profilePictureURL;
    private Boolean active;
}
