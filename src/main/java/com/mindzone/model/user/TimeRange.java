package com.mindzone.model.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeRange implements Serializable {

    private Integer startsAt; // minutes since midnight
    private Integer endsAt; // minutes since midnight

    public TimeRange(TimeRange tr) {
        this.startsAt = tr.getStartsAt();
        this.endsAt = tr.getEndsAt();
    }
}
