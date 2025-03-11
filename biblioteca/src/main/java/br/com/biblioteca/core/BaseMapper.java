package br.com.biblioteca.core;


import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import org.mapstruct.BeanMapping;

public interface BaseMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    default List<D> toDto(Collection<E> entity) {
        return entity.stream().map(this::toDto).toList();
    }

    default List<E> toEntity(Collection<D> dto) {
        return dto.stream().map(this::toEntity).toList();
    }

    default Page<D> toDto(Page<E> entity) {
        return entity.map(this::toDto);
    }

    default Page<E> toEntity(Page<D> dto) {
        return dto.map(this::toEntity);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeNonNull(D dto, @MappingTarget E entity);

}
