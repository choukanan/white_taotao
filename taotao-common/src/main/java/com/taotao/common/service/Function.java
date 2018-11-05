package com.taotao.common.service;

public interface Function<E,T>{
    
    public T callback(E e);
}
