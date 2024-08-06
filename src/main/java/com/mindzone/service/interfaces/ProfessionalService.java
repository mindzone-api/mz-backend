package com.mindzone.service.interfaces;

import com.mindzone.dto.response.ListedPatient;
import com.mindzone.model.user.User;

import java.util.List;

public interface ProfessionalService {

    List<ListedPatient> getMyPatients(User user);

}
