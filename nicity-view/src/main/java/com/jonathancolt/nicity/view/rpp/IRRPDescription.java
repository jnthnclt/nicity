/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.view.rpp;

/**
 *
 * @author jonathan
 */
public interface IRRPDescription<K,V> {
    public K keys();
    public V value(K key);
}
