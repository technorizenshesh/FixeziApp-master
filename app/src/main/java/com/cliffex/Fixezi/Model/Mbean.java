package com.cliffex.Fixezi.Model;

/**
 * Created by technorizen8 on 4/4/16.
 */
public class Mbean<K,V> {


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    private K key;
    private V value;
    public Mbean(K key, V value)
    {
        this.key=key;
        this.value=value;

    }
}
