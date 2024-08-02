package com.mindzone.model.user;

import com.mindzone.exception.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

import static com.mindzone.exception.ExceptionMessages.INVALID_TIME_RANGE;

@NoArgsConstructor
@Data
public class TimeRange implements Serializable {

    private LocalTime startsAt;
    private LocalTime endsAt;

    public TimeRange(LocalTime startsAt, LocalTime endsAt) {
        if (endsAt.isBefore(startsAt)) {
            throw new ApiRequestException(INVALID_TIME_RANGE);
        }
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

}
