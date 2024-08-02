package com.mindzone.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;


@Data
public abstract class AbstractModel implements Serializable {

    @Id
    private String id;
    private Date createdAt;
    private Date updatedAt;

}
