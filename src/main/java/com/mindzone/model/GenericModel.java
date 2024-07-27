package com.mindzone.model;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public abstract class GenericModel {

    @Id
    private String id;

}
