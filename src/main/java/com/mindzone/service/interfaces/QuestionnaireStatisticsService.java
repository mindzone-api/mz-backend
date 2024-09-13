package com.mindzone.service.interfaces;

import com.mindzone.dto.request.QuestionnaireStatisticsRequest;
import com.mindzone.dto.response.QuestionnaireStatisticsResponse;
import com.mindzone.model.user.User;

public interface QuestionnaireStatisticsService {
    QuestionnaireStatisticsResponse getStatistics(User professional, String userId, QuestionnaireStatisticsRequest request);
}
