package com.mindzone.dto.response.listed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedReportResponse implements Serializable {

    private Date createdAt;
    private String title;
}
