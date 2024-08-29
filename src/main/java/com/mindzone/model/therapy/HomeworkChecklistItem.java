package com.mindzone.model.therapy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HomeworkChecklistItem {

    private Date ceckedAt;
    private String name;
    private Boolean isDone;
}
