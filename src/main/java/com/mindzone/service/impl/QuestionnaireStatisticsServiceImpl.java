package com.mindzone.service.impl;

import com.mindzone.dto.request.QuestionnaireStatisticsRequest;
import com.mindzone.dto.response.QuestionnaireStatisticsResponse;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.Questionnaire;
import com.mindzone.model.user.User;
import com.mindzone.repository.QuestionnaireRepository;
import com.mindzone.service.interfaces.QuestionnaireService;
import com.mindzone.service.interfaces.QuestionnaireStatisticsService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.mindzone.exception.ExceptionMessages.QUESTIONNAIRE_NOT_FOUND;

@Service
@AllArgsConstructor
public class QuestionnaireStatisticsServiceImpl implements QuestionnaireStatisticsService {

    private QuestionnaireService q;
    private UserService userService;
    private QuestionnaireRepository questionnaireRepository;


    @Override
    @Cacheable("questionnaire")
    public QuestionnaireStatisticsResponse getStatistics(User professional, String userId, QuestionnaireStatisticsRequest request) {
        User patient = userService.getById(userId);
        q.canAccessQuestionnaire(professional, patient);

        QuestionnaireStatisticsResponse response = new QuestionnaireStatisticsResponse(0.0, 0.0);
        response.setOverallAvgMood(buildOverallAvgMood(request.getUntil()));
        response.setAvgMoodBetweenDates(buildAvgMoodBetweenDates(request.getFrom(), request.getUntil()));
        return response;
    }

    private Double buildAvgMoodBetweenDates(Date from, Date until) {
        Double overallMood = 0.0;
        List<Questionnaire> questionnaires = questionnaireRepository.findAllByCreatedAtBetween(from, until);
        if (questionnaires.isEmpty()) {
            return overallMood;
        }
        for (Questionnaire q : questionnaires) {
            overallMood += q.getMood();
        }

        return overallMood / questionnaires.size();
    }


    private Double buildOverallAvgMood(Date until) {
        Questionnaire oldestQuestionnaire = questionnaireRepository.findOldest().orElseThrow(() -> new ApiRequestException(QUESTIONNAIRE_NOT_FOUND));
        return buildAvgMoodBetweenDates(oldestQuestionnaire.getCreatedAt(), until);
    }
}
