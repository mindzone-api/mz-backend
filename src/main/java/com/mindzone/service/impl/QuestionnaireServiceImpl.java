package com.mindzone.service.impl;

import com.mindzone.dto.request.QuestionnaireRequest;
import com.mindzone.dto.response.QuestionnaireResponse;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.Questionnaire;
import com.mindzone.model.user.User;
import com.mindzone.repository.QuestionnaireRepository;
import com.mindzone.service.interfaces.QuestionnaireService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.mindzone.exception.ExceptionMessages.QUESTIONNAIRE_ALREADY_EXISTS;
import static com.mindzone.exception.ExceptionMessages.QUESTIONNAIRE_NOT_FOUND;

@Service
@AllArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private QuestionnaireRepository questionnaireRepository;
    private UltimateModelMapper m;

    @Override
    public void save(Questionnaire entity) {
        entity.updateDates();
        questionnaireRepository.save(entity);
    }
    @Override
    public Questionnaire getById(String id) {
        return questionnaireRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(QUESTIONNAIRE_NOT_FOUND));
    }

    @Override
    public void canAccessQuestionnaire(User user, Questionnaire questionnaire) {

    }

    @Override
    public void canManageQuestionnaire(User user, Questionnaire questionnaire) {

    }

    @Override
    public QuestionnaireResponse create(User patient, QuestionnaireRequest request) {
        if (questionnaireRepository.findByDate(new Date()).isPresent()) {
            throw new ApiRequestException(QUESTIONNAIRE_ALREADY_EXISTS);
        }

        Questionnaire questionnaire = m.map(request, Questionnaire.class);
        questionnaire.setPatientId(patient.getId());
        save(questionnaire);
        return m.map(questionnaire, QuestionnaireResponse.class);
    }
}
