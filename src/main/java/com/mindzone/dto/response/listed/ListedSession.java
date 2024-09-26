package com.mindzone.dto.response.listed;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@NoArgsConstructor
@Data
public class ListedSession implements Serializable {
    private Date date;
}
