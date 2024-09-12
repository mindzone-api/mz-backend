package com.mindzone.service.interfaces;

import com.mindzone.dto.request.TherapyUpdate;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;

import java.util.List;

public interface TherapyService {

    void save(Therapy therapy);
    Therapy getById(String id);
    void canAccess(User user, Therapy therapy);
    void canManage(User user, Therapy therapy);

    void isApproved(Therapy therapy);

    void isActive(Therapy therapy);

    TherapyResponse get(User user, String id);

    boolean hasActiveTherapy(User professional, User patient);

    List<ListedTherapy> getAll(User user);
    TherapyResponse update(User professional, String id, TherapyUpdate therapyUpdate);

    TherapyResponse cancel(User user, String therapyId);
}
