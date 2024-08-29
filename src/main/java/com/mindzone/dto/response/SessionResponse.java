package com.mindzone.dto.response;

import com.mindzone.model.therapy.File;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public abstract class SessionResponse {

    private String therapyId;
    private Date date;
    private List<File> sharedAttatchments;

}
