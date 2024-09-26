package com.mindzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HomeworkChecklistItem implements Serializable {

    private Date ceckedAt;
    private String name;
    private Boolean isDone;
}
