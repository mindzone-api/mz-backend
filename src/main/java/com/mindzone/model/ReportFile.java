package com.mindzone.model;


import com.mindzone.enums.FileType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "reportFile")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportFile extends File implements Serializable {

    private String reportId;
}
