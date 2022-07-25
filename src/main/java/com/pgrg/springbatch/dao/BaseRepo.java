package com.pgrg.springbatch.dao;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;


import java.util.List;

public interface BaseRepo<T> {
    Integer bulkInsert(List<T> items, Class type);
//    Pair<Integer, Integer> bulkUpsert(List<T> items, Class type, Function<T, Pair<Query, Update>> function);
//    Integer deleteAll(Class type);
//    Integer bulkUpdate(List<T> items, Class type,Function<T, Pair<Query, Update>> function);
}
