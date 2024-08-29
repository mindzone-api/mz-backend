package com.mindzone.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(of = "id")
public abstract class AbstractModel implements Serializable {

    @Id
    private String id;
    private Date createdAt;
    private Date updatedAt;

    public void updateDates() {
        Date now = new Date();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

}
