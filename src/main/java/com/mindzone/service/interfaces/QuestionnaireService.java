package com.mindzone.service.interfaces;

import com.mindzone.dto.request.QuestionnaireRequest;
import com.mindzone.dto.response.QuestionnaireResponse;
import com.mindzone.model.Questionnaire;
import com.mindzone.model.user.User;

public interface QuestionnaireService {

    void save(Questionnaire entity);

    Questionnaire getById(String id);

    void canAccessQuestionnaire(User user, Questionnaire questionnaire);

    void canManageQuestionnaire(User user, Questionnaire questionnaire);

    QuestionnaireResponse create(User patient, QuestionnaireRequest request);

    QuestionnaireResponse get(User user, String questionnaireId);
}
