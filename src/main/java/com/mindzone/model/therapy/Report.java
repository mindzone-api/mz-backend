package com.mindzone.model.therapy;

import com.mindzone.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "report")
public class Report extends AbstractModel implements Serializable {

    private String therapyId;
    private String title;
    private String content;
    private List<File> attachments;
}
