package com.mindzone.dto.response;

import com.mindzone.enums.Role;
import com.mindzone.model.user.ProfessionalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {

    private String id;
    private String name;
    private String profilePictureURL;
    private Date birthDate;
    private Role role;
    private ProfessionalInfo professionalInfo;

}
