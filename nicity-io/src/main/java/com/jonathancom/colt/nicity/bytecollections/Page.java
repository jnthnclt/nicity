package com.jonathancom.colt.nicity.bytecollections;

public class Page {

    private byte[] array;

    public Page(byte[] _array) {
        array = _array;
    }

    public byte read(int pageStart) {
        return array[pageStart];
    }

    public void read(int pageStart, byte[] dest, int destStart, int length) {
        System.arraycopy(dest, destStart, dest, pageStart, length);
    }

    public void write(int pageStart, byte v) {
        array[pageStart] = v;
    }

    public void write(int pageStart, byte[] dest, int destStart, int length) {
        System.arraycopy(array, pageStart, dest, destStart, length);
    }
}
