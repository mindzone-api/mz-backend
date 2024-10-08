package com.mindzone.util;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
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

    public <S, T> List<T> listMap(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(element -> m.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public <S, T> Page<T> pageMap(Page<S> sourcePage, Class<T> targetClass) {
        Pageable pageable = PageRequest.of(
                sourcePage.getNumber(),
                sourcePage.getSize(),
                sourcePage.getSort()
        );

        return new PageImpl<>(listMap(sourcePage.getContent(), targetClass), pageable, sourcePage.getTotalElements());
    }

}
