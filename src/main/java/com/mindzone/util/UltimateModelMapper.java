package com.mindzone.util;

import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public void map(Object source, Object destination) {
        m.map(source, destination);
    }

    public <S, T> List<T> mapToList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(element -> m.map(element, targetClass))
                .collect(Collectors.toList());
    }

}
