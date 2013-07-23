/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancom.colt.nicity.bytecollections;

/**
 *
 * @author jonathan
 */
public abstract class AExtractor {

    public abstract byte[] extract(long _startIndex, int _keySize, int _payloadSize, byte[] _array);
    
}
