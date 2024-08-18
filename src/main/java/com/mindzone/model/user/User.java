package com.mindzone.model.user;

import com.mindzone.enums.Gender;
import com.mindzone.enums.Role;
import com.mindzone.enums.State;
import com.mindzone.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends AbstractModel implements Serializable {

    private String name;
    private String email;
    private String profilePictureURL;
    private Date birthDate;
    private Role role;
    private Gender gender;
    private String address;
    private String neighborhood;
    private String city;
    private State state;
    private ProfessionalInfo professionalInfo;
    private String stripeId;
    private String subscriptionId;

}