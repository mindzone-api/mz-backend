package com.mindzone.model;

import com.mindzone.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "prescription")
public class Prescription extends AbstractModel implements Serializable {

    private String therapyId;
    private String file;
    private String medicationName;
    private String quantity;
    private String howOften;
    private Date until;
}
