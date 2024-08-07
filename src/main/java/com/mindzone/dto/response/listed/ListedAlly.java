package com.mindzone.dto.response.listed;

import com.mindzone.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedAlly {

    private String name;
    private String profilePictureURL;
    private String patientId;
    private Boolean active;
}
