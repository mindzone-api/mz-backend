package com.mindzone.dto.response;

import com.mindzone.enums.Profession;
import com.mindzone.enums.TherapyModality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListedProfessional implements Serializable {

    private String name;
    private String profilePictureURL;
    private Profession profession;
    private List<TherapyModality> therapyModalities;
    private BigDecimal pricePerSession;
}
