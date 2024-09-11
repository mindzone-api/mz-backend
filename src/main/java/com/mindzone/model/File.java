package com.mindzone.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class File extends AbstractModel{

    private String name;
    private String content;
}
