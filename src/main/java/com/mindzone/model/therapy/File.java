package com.mindzone.model.therapy;

import com.mindzone.enums.FileType;
import com.mindzone.model.AbstractModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "file")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class File extends AbstractModel implements Serializable {

    private String name;
    private String sessionId;
    private FileType fileType;
    private String content;
    private Boolean isMedicalRecord;
}
