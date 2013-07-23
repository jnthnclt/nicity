/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancom.colt.nicity.bytecollections;

/**
 *
 * @author jonathan
 */
public class ExtractPayload extends AExtractor {

    @Override
    public byte[] extract(long _startIndex, int _keySize, int _payloadSize, byte[] _array) {
        byte[] p = new byte[_payloadSize];
        System.arraycopy(_array, (int) _startIndex + 1 + _keySize, p, 0, _payloadSize);
        return p;
    }
    
}
