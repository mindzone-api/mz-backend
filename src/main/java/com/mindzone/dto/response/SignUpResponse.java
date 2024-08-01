package com.mindzone.dto.response;

import com.mindzone.enums.Role;
import com.mindzone.model.user.ProfessionalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpResponse {
    private String email;
    private Role role;
}
