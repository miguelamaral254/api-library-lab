package br.com.biblioteca.core;

public interface GenericDataQuery {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
