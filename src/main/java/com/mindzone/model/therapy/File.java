package com.mindzone.model.therapy;

import com.mindzone.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "file")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class File extends AbstractModel implements Serializable {

    private String name;
    private String file;
    private Boolean isMedicalRecord;
}
