package com.mindzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends GenericModel{

    private String name;
    private int age;

}