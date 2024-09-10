package com.mindzone.service.impl;

import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.ReportFile;
import com.mindzone.repository.ReportFileRepository;
import com.mindzone.service.interfaces.ReportFileService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.mindzone.util.FileUtil.contains;
import static com.mindzone.exception.ExceptionMessages.REPORT_FILE_NOT_FOUND;
import static com.mindzone.util.FileUtil.hasUpdated;

@Service
@AllArgsConstructor
public class ReportFileServiceImpl implements ReportFileService {

    private ReportFileRepository reportFileRepository;
    private UltimateModelMapper m;
    @Override
    public void save(ReportFile model) {
        model.updateDates();
        reportFileRepository.save(model);
    }

    @Override
    public ReportFile getById(String id) {
        return reportFileRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(REPORT_FILE_NOT_FOUND));
    }

    @Override
    public List<ReportFile> getAll(String reportId) {
        return reportFileRepository.findAllByReportId(reportId);
    }

    @Override
    public List<ReportFile> updateReportFiles(String reportId, List<ReportFile> newReportFiles) {
        List<ReportFile> databaseFiles = reportFileRepository.findAllByReportId(reportId);

        // 1- Delete all files ocurrences that are on mongo and not on the updated list
        for (ReportFile databaseFile : databaseFiles) {

            if  (!contains(newReportFiles, databaseFile)) {
                reportFileRepository.delete(databaseFile);
            }
        }

        // 2- Update existing files with new data and update the updatedAt attribute
        for (ReportFile reportFile : newReportFiles) {
            if (contains(databaseFiles, reportFile)) {
                ReportFile fileToUpdate = getById(reportFile.getId());
                if (hasUpdated(fileToUpdate, reportFile)) {
                    fileToUpdate.setName(reportFile.getName());
                    fileToUpdate.setContent(reportFile.getContent());
                    save(fileToUpdate);
                    m.map(fileToUpdate, reportFile);
                }
            } else {
                reportFile.setReportId(reportId);
                save(reportFile);
            }
        }
        return newReportFiles;
    }



}
