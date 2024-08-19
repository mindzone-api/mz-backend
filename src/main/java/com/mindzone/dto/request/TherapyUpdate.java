package com.mindzone.dto.request;


import lombok.*;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyUpdate extends TherapyToValidate {
    private String url;
    private Date nextSession;
}
