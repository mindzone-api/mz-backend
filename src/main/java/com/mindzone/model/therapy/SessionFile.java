package com.mindzone.model.therapy;

import com.mindzone.enums.FileType;
import com.mindzone.model.File;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "sessionFile")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SessionFile extends File implements Serializable {

    private String sessionId;
    private FileType fileType;
    private Boolean isMedicalRecord;
}
