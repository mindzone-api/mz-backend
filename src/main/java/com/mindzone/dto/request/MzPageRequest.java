package com.mindzone.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class MzPageRequest {
    private Integer page;
    private Integer size;
}
