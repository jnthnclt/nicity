/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancom.colt.nicity.bytecollections;

/**
 *
 * @author jonathan
 */
public class ExtractKey extends AExtractor {

    @Override
    public byte[] extract(long _startIndex, int _keySize, int _payloadSize, byte[] _array) {
        byte[] k = new byte[_keySize];
        System.arraycopy(_array, (int) _startIndex + 1, k, 0, _keySize);
        return k;
    }
    
}
