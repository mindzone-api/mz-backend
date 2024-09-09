package com.mindzone.service.impl;

import com.mindzone.model.ReportFile;
import com.mindzone.repository.ReportFileRepository;
import com.mindzone.service.interfaces.ReportFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportFileServiceImpl implements ReportFileService {

    private ReportFileRepository reportFileRepository;
    @Override
    public void save(ReportFile model) {
        model.updateDates();
        reportFileRepository.save(model);
    }
}
