package com.finance.manager;

public interface Mapper<T, U> {
    U mapToModel(T entity);

    T mapToEntity(U model);
}

