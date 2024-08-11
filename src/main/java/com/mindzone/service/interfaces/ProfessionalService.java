package com.mindzone.service.interfaces;

import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedPatient;
import com.mindzone.model.user.User;
import com.mindzone.dto.response.listed.ListedAlly;
import com.mindzone.model.user.WeekDaySchedule;

import java.util.List;

public interface ProfessionalService {

    List<ListedPatient> getMyPatients(User user);

    List<ListedAlly> getMyAllies(User user);

    UserResponse updateAvailability(User user, List<WeekDaySchedule> schedule);
}
