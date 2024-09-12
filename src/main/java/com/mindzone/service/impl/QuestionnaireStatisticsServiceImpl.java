package com.mindzone.service.impl;

import com.mindzone.dto.request.QuestionnaireStatisticsRequest;
import com.mindzone.dto.response.QuestionnaireStatisticsResponse;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.QuestionnaireService;
import com.mindzone.service.interfaces.QuestionnaireStatisticsService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionnaireStatisticsServiceImpl implements QuestionnaireStatisticsService {

    private QuestionnaireService q;
    private UserService userService;


    @Override
    public QuestionnaireStatisticsResponse getStatistics(User professional, String userId, QuestionnaireStatisticsRequest request) {
        User patient = userService.getById(userId);
        q.canAccessQuestionnaire(professional, patient);

        QuestionnaireStatisticsResponse response = new QuestionnaireStatisticsResponse();
        response.setOverallAvgMood(buildOverallAvgMood(request));
        response.setMedianMoodBetweenDates(buildMedianMoodBetweenDates(request));
        response.setAvgMoodBetweenDates(buildAvgMoodBetweenDates(request));
        response.setOverallMedianMood(buildOverallMedianMood(request));
        return response;
    }

    private Double buildOverallMedianMood(QuestionnaireStatisticsRequest request) {
        return null;
    }

    private Double buildAvgMoodBetweenDates(QuestionnaireStatisticsRequest request) {
        return null;
    }

    private Double buildMedianMoodBetweenDates(QuestionnaireStatisticsRequest request) {
        return null;
    }

    private Double buildOverallAvgMood(QuestionnaireStatisticsRequest request) {
        return null;
    }
}
