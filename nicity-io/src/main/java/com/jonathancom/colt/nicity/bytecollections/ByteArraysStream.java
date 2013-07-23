package com.jonathancom.colt.nicity.bytecollections;

interface ByteArraysStream<B> {
    /**
     * end of stream demarcated by null;
     * return null to stop stream;
     * 
     * @param object
     * @return
     * @throws Exception 
     */
    public B stream(B bytes) throws Exception;
}
