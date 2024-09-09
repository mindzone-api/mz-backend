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
@Builder
public class ReportFile extends AbstractModel implements Serializable {

    private String name;
    private String reportId;
    private String content;
}
