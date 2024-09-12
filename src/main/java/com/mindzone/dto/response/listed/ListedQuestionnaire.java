package com.mindzone.dto.response.listed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedQuestionnaire {

    private Date createdAt;
    private Double mood;
}
