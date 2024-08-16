package com.mindzone.dto.request;


import lombok.*;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyUpdate extends TherapyToValidate {
    private String url;
}
