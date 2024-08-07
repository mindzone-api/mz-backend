package com.mindzone.dto.request;

import com.mindzone.enums.Gender;
import com.mindzone.enums.State;
import com.mindzone.model.user.ProfessionalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    private String name;
    private String email;
    private String profilePictureURL;
    private Date birthDate;
    private Gender gender;
    private String address;
    private String neighborhood;
    private String city;
    private State state;
    private ProfessionalInfo professionalInfo;
}
