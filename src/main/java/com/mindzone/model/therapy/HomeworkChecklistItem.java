package com.mindzone.model.therapy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HomeworkChecklistItem {

    private String name;
    private Boolean isDone;
}
