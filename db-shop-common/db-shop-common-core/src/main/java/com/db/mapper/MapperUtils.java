package com.db.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;

/**把list转换成dto List*/
public class MapperUtils {
    static MapperFactory mapperFactory;
    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
    }

    public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
        return mapperFactory.getMapperFacade().mapAsList(source, destinationClass);
    }
}
