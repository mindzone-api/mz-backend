package com.mindzone.model.therapy;

import com.mindzone.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "homework")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Homework extends AbstractModel implements Serializable {

    private String therapyId;
    private Date dueDate;
    private List<HomeworkChecklistItem> checklist;
    private String observations;
}
