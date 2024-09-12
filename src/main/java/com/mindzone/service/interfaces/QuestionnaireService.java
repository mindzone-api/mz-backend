package com.mindzone.service.interfaces;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.QuestionnaireRequest;
import com.mindzone.dto.response.QuestionnaireResponse;
import com.mindzone.dto.response.listed.ListedQuestionnaire;
import com.mindzone.model.Questionnaire;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;

public interface QuestionnaireService {

    void save(Questionnaire entity);

    Questionnaire getById(String id);

    void canAccessQuestionnaire(User user, Questionnaire questionnaire);

    void canAccessQuestionnaire(User userAsking, User userToAccess);

    void canManageQuestionnaire(User user, Questionnaire questionnaire);

    QuestionnaireResponse create(User patient, QuestionnaireRequest request);

    QuestionnaireResponse get(User user, String questionnaireId);

    Page<ListedQuestionnaire> getAll(User user, String userId, MzPageRequest pageRequest);
}
