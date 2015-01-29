package com.ngandroid.lib.utils;

/**
 * Created by tyler on 1/28/15.
 */
public class Tuple<T,K> {


    private T first;
    private K second;

    public Tuple(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public K getSecond() {
        return second;
    }

    public void setSecond(K second) {
        this.second = second;
    }

    public static <T,K> Tuple<T,K> of(T t, K k){
        return new Tuple<>(t,k);
    }
}
