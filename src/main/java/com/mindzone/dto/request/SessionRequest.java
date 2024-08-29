package com.mindzone.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mindzone.model.therapy.File;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProfessionalSessionRequest.class, name = "PROFESSIONAL"),
        @JsonSubTypes.Type(value = PatientSessionRequest.class, name = "PATIENT")
})
public abstract class SessionRequest {

    private List<File> sharedAttatchments;

}
