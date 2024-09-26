package com.mindzone.dto.response.listed;

import com.mindzone.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedAlly implements Serializable {

    private String id;
    private String name;
    private String profilePictureURL;
    private String patientId;
    private Boolean active;
}
