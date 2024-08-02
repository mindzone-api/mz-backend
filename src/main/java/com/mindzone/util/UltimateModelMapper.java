package com.mindzone.util;

import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
@AllArgsConstructor
public class UltimateModelMapper {

    private ModelMapper m;

    public <E, T> E map(T source, Class<E> typeDestination) {
        E model = null;
        if (source != null && typeDestination != null) {
            model = m.map(source, typeDestination);
        }
        return model;
    }

    public <E, T> List<E> mapToList(T sourceList, Class<E> listedTypeDestination) {
        Type listType = TypeToken.getParameterized(List.class, listedTypeDestination).getType();
        return m.map(sourceList, listType);
    }

}
