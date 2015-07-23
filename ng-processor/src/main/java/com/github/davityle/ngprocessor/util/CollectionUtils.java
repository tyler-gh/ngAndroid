package com.github.davityle.ngprocessor.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

public class CollectionUtils {

    @Inject
    public CollectionUtils() {
    }

    public <T> Collection<T> filter(final Iterable<T> it, final Function<T, Boolean> f) {
        final ArrayList<T> arrayList = new ArrayList<>();
        it.forEach(new Consumer<T>() {
            @Override
            public void accept(T t) {
                if(f.apply(t)) {
                    arrayList.add(t);
                }
            }
        });
        return arrayList;
    }

    public <T, R> Map<T, R> toMap(Collection<Tuple<T, R>> collection) {
        final Map<T, R> map = new HashMap<>();
        collection.forEach(new Consumer<Tuple<T, R>>() {
            @Override
            public void accept(Tuple<T, R> trTuple) {
                map.put(trTuple.getFirst(), trTuple.getSecond());
            }
        });
        return map;
    }

    public <T, R> Collection<R> flatMap(final Iterable<T> it, final Function<T, Collection<R>> f) {
        final ArrayList<R> arrayList = new ArrayList<>();
        it.forEach(new Consumer<T>() {
            @Override
            public void accept(T t) {
                arrayList.addAll(f.apply(t));
            }
        });
        return arrayList;
    }

    public <T, R> Collection<R> flatMapOpt(final Iterable<T> it, final Function<T, Option<R>> f) {
        final ArrayList<R> arrayList = new ArrayList<>();
        it.forEach(new Consumer<T>() {
            @Override
            public void accept(T t) {
                Option<R> opt = f.apply(t);
                if(opt.isPresent()) {
                    arrayList.add(opt.get());
                }
            }
        });
        return arrayList;
    }

    public <T, R> Collection<R> map(final Iterable<T> it, final Function<T, R> f) {
        final ArrayList<R> arrayList = new ArrayList<>();
        it.forEach(new Consumer<T>() {
            @Override
            public void accept(T t) {
                arrayList.add(f.apply(t));
            }
        });
        return arrayList;
    }

    public interface Function<T, R> {
        R apply(T t);
    }
}
