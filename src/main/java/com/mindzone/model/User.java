package com.mindzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends AbstractModel implements Serializable {

    private String name;
    private int age;

}