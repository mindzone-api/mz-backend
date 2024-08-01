package com.mindzone.dto.request;

import com.mindzone.model.user.ProfessionalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpRequest {
    private String name;
    private String email;
    private String profilePictureURL;
    private Date birthDate;
    private ProfessionalInfo professionalInfo;
}
