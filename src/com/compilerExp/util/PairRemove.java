/*
 * @(#) PairRemove.java 2019/04/15
 */
package com.compilerExp.util;

public class PairRemove<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<PairRemove<K, V>> {
    K key;
    V value;

    public PairRemove(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(PairRemove<K, V> an) {
        int firstResult = key.compareTo(an.key);
        if (firstResult == 0) {
            return value.compareTo(an.value);
        } else {
            return firstResult;
        }
    }

    @Override
    public boolean equals(Object obj) {
        PairRemove<K, V> an = null;
        try {
            an = (PairRemove<K, V>) obj;
        } catch (Exception e) {
            return false;
        }
        return an.key == this.key && an.value == this.value;
    }

    @Override
    public int hashCode() {
        return (key.hashCode() + value.hashCode()) % Integer.MAX_VALUE;
    }
}
