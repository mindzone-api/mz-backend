package com.mindzone.dto.request;

import com.mindzone.model.ReportFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportRequest {

    private String therapyId;
    private String title;
    private String content;
    private List<ReportFile> attachments;
}
