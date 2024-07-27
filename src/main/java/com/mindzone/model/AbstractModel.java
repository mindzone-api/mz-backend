package com.mindzone.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Data
public abstract class AbstractModel implements Serializable {

    @Id
    private String id;

}
