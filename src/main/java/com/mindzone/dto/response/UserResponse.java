package com.mindzone.dto.response;

import com.mindzone.enums.Role;
import com.mindzone.enums.State;
import com.mindzone.model.user.ProfessionalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse implements Serializable {

    private String id;
    private String name;
    private String profilePictureURL;
    private Date birthDate;
    private Role role;
    private ProfessionalInfo professionalInfo;
    private String neighborhood;
    private String city;
    private State state;

}
