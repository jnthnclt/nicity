package com.jonathancom.colt.nicity.bytecollections;

import com.jonathancolt.nicity.core.lang.UString;

/**
 * this is a key+payload set that is backed buy a byte array. It is a fixed size set.
 * It will not grow or shrink. You need to be aware and expect that your system will
 * cause the set to throw EOverCapacity exceptions. The goal is to create a collection
 * which will page to and from disk or net as fast as possible. Nothing is synchronized
 * to make it thread safe you need to synchronize on the byte[].
 * 
 */
public class ByteArrayFixedSizeSet {
    // header 16bytes for id, 4 for count, 4 for max count,4 for keysize, 4 for payloadsize

    private static final int cVersionSize = 1;
    private static final int cFamilySize = 1;
    private static final int cIdSize = 16;
    private static final int cCountSize = 4;
    private static final int cMaxCountSize = 4;
    private static final int cMaxCapacitySize = 4;
    private static final int cKeySizeSize = 4;
    private static final int cPayloadSize = 4;
    private static final int cHeaderSize = cVersionSize + cFamilySize + cIdSize + cCountSize + cMaxCountSize + cMaxCapacitySize + cKeySizeSize + cPayloadSize;
    private static final int cVersionOffset = 0;
    private static final int cFamilyOffset = cVersionSize;
    private static final int cIdOffset = cVersionSize + cFamilySize;
    private static final int cCountOffset = cVersionSize + cFamilySize + cIdSize;
    private static final int cMaxCountOffset = cVersionSize + cFamilySize + cIdSize + cCountSize;
    private static final int cCapacityOffset = cVersionSize + cFamilySize + cIdSize + cCountSize + cMaxCountSize;
    private static final int cKeySizeOffset = cVersionSize + cFamilySize + cIdSize + cCountSize + cMaxCountSize + cMaxCapacitySize;
    private static final int cPayloadOffset = cVersionSize + cFamilySize + cIdSize + cCountSize + cMaxCountSize + cMaxCapacitySize + cKeySizeSize;
    private static final double cSetDensity = 0.6d;
    private static final byte cSkip = -1;
    private static final byte cNull = 0;

    public static int scost(int _maxKeys, int _keySize, int _payloadSize) {
        int maxCapacity = (int) (_maxKeys + (_maxKeys - (_maxKeys * cSetDensity)));
        // 1+ for head of entry status byte. 0 and -1 reserved
        int entrySize = 1 + _keySize + _payloadSize;
        return cHeaderSize + (entrySize * maxCapacity);
    }

    public static long sabsoluteMaxCount(int _keySize, int _payloadSize) {
        // 1+ for head of entry status byte. 0 and -1 reserved
        int entrySize = 1 + _keySize + _payloadSize;
        long maxCount = (Integer.MAX_VALUE - cHeaderSize) / entrySize;
        return (long) (maxCount * cSetDensity);
    }

    public static byte[] sallocate(int _maxCount, int _keySize, int _payloadSize) {
        int maxCapacity = (int) (_maxCount + (_maxCount - (_maxCount * cSetDensity)));

        byte[] array = new byte[scost(_maxCount, _keySize, _payloadSize)];
        ssetVersion(array, (byte) 0);
        ssetFamily(array, (byte) 0);
        setCount(array, 0);
        setMaxCount(array, _maxCount);
        setCapacity(array, maxCapacity);// good to use prime
        setKeySize(array, _keySize);
        setPayloadSize(array, _payloadSize);
        // header numKeys,maxKeys,keySize,payloadSize
        return array;
    }

    public static byte sgetVersion(byte[] array) {
        return array[cVersionOffset];
    }

    public static void ssetVersion(byte[] array, byte _version) {
        array[cVersionOffset] = _version;
    }

    public static byte sgetFamily(byte[] array) {
        return array[cFamilyOffset];
    }

    public static void ssetFamily(byte[] array, byte _version) {
        array[cFamilyOffset] = _version;
    }

    public static byte[] sgetId(byte[] array) {
        byte[] id = new byte[cIdSize];
        System.arraycopy(id, cIdOffset, id, 0, cIdSize);
        return id;
    }

    public static void ssetId(byte[] array, byte[] id) {
        System.arraycopy(id, 0, array, cIdOffset, cIdSize);
    }

    public static long sgetCount(byte[] array) {
        return getInt(array, cCountOffset);
    }

    private static void setCount(byte[] array, long v) {
        setInt(array, cCountOffset, (int) v);
    }

    public static int sgetMaxCount(byte[] array) {
        return getInt(array, cMaxCountOffset);
    }

    private static void setMaxCount(byte[] array, int v) {
        setInt(array, cMaxCountOffset, v);
    }

    public static int sgetCapacity(byte[] array) {
        return getInt(array, cCapacityOffset);
    }

    private static void setCapacity(byte[] array, int v) {
        setInt(array, cCapacityOffset, v);
    }

    public static int sgetKeySize(byte[] array) {
        return getInt(array, cKeySizeOffset);
    }

    private static void setKeySize(byte[] array, int v) {
        setInt(array, cKeySizeOffset, v);
    }

    public static int sgetPayloadSize(byte[] array) {
        return getInt(array, cPayloadOffset);
    }

    private static void setPayloadSize(byte[] array, int v) {
        setInt(array, cPayloadOffset, v);
    }

    /**
     *
     * @param array
     * @param _mode
     * @param key
     * @param _payload
     * @return Returns the index the entry was inserted at or -1
     */
    public static int sadd(byte[] array, byte _mode, byte[] key, byte[] _payload) {
        int capacity = sgetCapacity(array);
        if (sgetCount(array) >= sgetMaxCount(array)) {
            throw new EOverCapacity();
        }
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        for (long i = hash(key, 0, key.length) % (capacity - 1), j = 0, k = capacity; // stack vars for efficiency
                j < k; // max search for available slot
                i = (++i) % k, j++) {					// wraps around table

            long ai = index(i, keySize, payloadSize);
            if (array[(int) ai] == cNull || array[(int) ai] == cSkip) {
                array[(int) ai] = _mode;
                System.arraycopy(key, 0, array, (int) (ai + 1), keySize);
                System.arraycopy(_payload, 0, array, (int) (ai + 1 + keySize), payloadSize);
                setCount(array, sgetCount(array) + 1);
                return (int) i;
            }
            if (equals(array, ai, keySize, key)) {
                array[(int) ai] = _mode;
                //byte[] oldPayload = payload(array,ai,keySize,payloadSize);
                System.arraycopy(_payload, 0, array, (int) (ai + 1 + keySize), payloadSize);
                return (int) i;
            }
        }
        return -1;
    }

    public static boolean scontains(byte[] array, byte[] _key) {
        return sget(array, _key) != null;
    }

    public static byte[] sgetKeyAtIndex(byte[] array, int i) {
        if (i < 0 || i >= sgetCapacity(array)) {
            throw new RuntimeException("Requested index (" + i + ") is out of bounds (0->" + (sgetCapacity(array) - 1) + ")");
        }
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        long ai = index(i, keySize, payloadSize);
        if (array[(int) ai] == cSkip) {
            return null;
        }
        if (array[(int) ai] == cNull) {
            return null;
        }
        return key(array, ai, keySize);
    }

    public static byte[] sgetPayloadAtIndex(byte[] array, int i) {
        if (i < 0 || i >= sgetCapacity(array)) {
            throw new RuntimeException("Requested index (" + i + ") is out of bounds (0->" + (sgetCapacity(array) - 1) + ")");
        }
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        long ai = index(i, keySize, payloadSize);
        if (array[(int) ai] == cSkip) {
            return null;
        }
        if (array[(int) ai] == cNull) {
            return null;
        }
        return payload(array, ai, keySize, payloadSize);
    }

    public static int sstartOfKey(int setIndex, int keySize, int payloadSize) {
        long ai = index(setIndex, keySize, payloadSize);
        return (int) (ai + 1);
    }

    public static int sstartOfPayload(int setIndex, int keySize, int payloadSize) {
        long ai = index(setIndex, keySize, payloadSize);
        return (int) (ai + 1 + keySize);
    }

    public static void ssetPayloadAtIndex(byte[] array, int i, int _destOffset, byte[] payload, int _poffset, int _plength) {
        if (i < 0 || i >= sgetCapacity(array)) {
            throw new RuntimeException("Requested index (" + i + ") is out of bounds (0->" + (sgetCapacity(array) - 1) + ")");
        }
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        long ai = index(i, keySize, payloadSize);
        if (array[(int) ai] == cSkip) {
            return;
        }
        if (array[(int) ai] == cNull) {
            return;
        }
        System.arraycopy(payload, _poffset, array, _destOffset + (int) (ai + 1 + keySize), _plength);
    }

    //  Getting
    // this only works because the set will not rehash.
    public static int sgetIndex(byte[] array, byte[] key) {
        if (key == null || key.length == 0) {
            return -1;
        }
        int capacity = sgetCapacity(array);
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        for (long i = hash(key, 0, key.length) % (capacity - 1), j = 0, k = capacity; // stack vars for efficiency
                j < k; // max search for key
                i = (++i) % k, j++) {					// wraps around table

            long ai = index(i, keySize, payloadSize);
            if (array[(int) ai] == cSkip) {
                continue;
            }
            if (array[(int) ai] == cNull) {
                return -1;
            }
            if (equals(array, ai, keySize, key)) {
                return (int) i;
            }
        }
        return -1;
    }

    //  Getting
    public static byte[] sget(byte[] array, byte[] key) {
        if (key == null || key.length == 0) {
            return null;
        }
        int capacity = sgetCapacity(array);
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        for (long i = hash(key, 0, key.length) % (capacity - 1), j = 0, k = capacity; // stack vars for efficiency
                j < k; // max search for key
                i = (++i) % k, j++) {					// wraps around table

            long ai = index(i, keySize, payloadSize);
            if (array[(int) ai] == cSkip) {
                continue;
            }
            if (array[(int) ai] == cNull) {
                return null;
            }
            if (equals(array, ai, keySize, key)) {
                return payload(array, ai, keySize, payloadSize);
            }
        }
        return null;
    }

    public static byte[] sget(byte[] array, byte[] key, int[] index) {
        if (key == null || key.length == 0) {
            return null;
        }
        int capacity = sgetCapacity(array);
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        for (long i = hash(key, 0, key.length) % (capacity - 1), j = 0, k = capacity; // stack vars for efficiency
                j < k; // max search for key
                i = (++i) % k, j++) {					// wraps around table

            long ai = index(i, keySize, payloadSize);
            if (array[(int) ai] == cSkip) {
                continue;
            }
            if (array[(int) ai] == cNull) {
                index[0] = (int) -1;
                return null;
            }
            if (equals(array, ai, keySize, key)) {
                index[0] = (int) i;
                return payload(array, ai, keySize, payloadSize);
            }
        }
        return null;
    }

    public static byte[] sremove(byte[] array, byte[] key) {
        if (key == null || key.length == 0) {
            return null;
        }
        int capacity = sgetCapacity(array);
        int keySize = sgetKeySize(array);
        int payloadSize = sgetPayloadSize(array);
        for (long i = hash(key, 0, key.length) % (capacity - 1), j = 0, k = capacity; // stack vars for efficiency
                j < k; // max search for key
                i = (++i) % k, j++) {					// wraps around table

            long ai = index(i, keySize, payloadSize);
            if (array[(int) ai] == cSkip) {
                continue;
            }
            if (array[(int) ai] == cNull) {
                return null;
            }
            if (equals(array, ai, keySize, key)) {
                byte[] removedPayload = payload(array, ai, keySize, payloadSize);
                long next = (i + 1) % k;
                if (array[(int) index(next, keySize, payloadSize)] == cNull) {
                    for (long z = i; z >= 0; z--) {
                        if (array[(int) index(z, keySize, payloadSize)] != cSkip) {
                            break;
                        }
                        array[(int) index(z, keySize, payloadSize)] = cNull;
                    }
                    array[(int) index(i, keySize, payloadSize)] = cNull;
                } else {
                    array[(int) index(i, keySize, payloadSize)] = cSkip;
                }
                setCount(array, sgetCount(array) - 1);
                return removedPayload;
            }
        }
        return null;
    }

    // IBackcall
    public static void sget(byte[] array, AExtractor _extractor, ByteArraysStream<byte[]> _callback) {
        try {
            long c = sgetCapacity(array);
            long count = sgetCount(array);
            int keySize = sgetKeySize(array);
            int payloadSize = sgetPayloadSize(array);
            for (int i = 0; i < c; i++) {
                long ai = index(i, keySize, payloadSize);
                if (array[(int) ai] == cNull) {
                    continue;
                }
                if (array[(int) ai] == cSkip) {
                    continue;
                }
                count--;
                byte[] v = _extractor.extract(ai, keySize, payloadSize, array);
                byte[] back = _callback.stream(v);
                if (back != v) {
                    break;
                }
                if (count < 0) {
                    break;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void stoSysOut(byte[] array) {
        try {
            long c = sgetCapacity(array);
            int keySize = sgetKeySize(array);
            int payloadSize = sgetPayloadSize(array);
            System.out.println("\t#): count " + sgetCount(array));
            for (int i = 0; i < c; i++) {
                long ai = index(i, keySize, payloadSize);
                if (array[(int) ai] == cNull) {
                    System.out.println("\t" + i + "): null");
                    continue;
                }
                if (array[(int) ai] == cSkip) {
                    System.out.println("\t" + i + "): skip");
                    continue;
                }
                System.out.println("\t" + i + "): K=" + UString.toString(cKeys.extract(ai, keySize, payloadSize, array), ",") + " V=" + UString.toString(cPayload.extract(ai, keySize, payloadSize, array), ","));
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private static void setInt(byte[] array, int offset, int v) {
        array[offset + 0] = (byte) (v >>> 24);
        array[offset + 1] = (byte) (v >>> 16);
        array[offset + 2] = (byte) (v >>> 8);
        array[offset + 3] = (byte) v;
    }

    private static int getInt(byte[] array, int offset) {
        int v = 0;
        v |= (array[offset + 0] & 0xFF);
        v <<= 8;
        v |= (array[offset + 1] & 0xFF);
        v <<= 8;
        v |= (array[offset + 2] & 0xFF);
        v <<= 8;
        v |= (array[offset + 3] & 0xFF);
        return v;
    }

    private static boolean equals(byte[] array, long arrayIndex, int keySize, byte[] b) {
        for (int i = 0; i < keySize; i++) {
            if (array[(int) (arrayIndex + 1 + i)] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static long index(long _arrayIndex, int keySize, int payloadSize) {
        return cHeaderSize + (1 + keySize + payloadSize) * _arrayIndex;
    }

    private static byte[] payload(byte[] array, long byteIndex, int keySize, int payloadSize) {
        byte[] p = new byte[payloadSize];
        System.arraycopy(array, (int) (byteIndex + 1 + keySize), p, 0, payloadSize);
        return p;
    }

    private static byte[] key(byte[] array, long _byteIndex, int keySize) {
        byte[] k = new byte[keySize];
        System.arraycopy(array, (int) (_byteIndex + 1), k, 0, keySize);
        return k;
    }

    public static long hash(byte[] _key, int _start, int _length) {
        long hash = 0;
        long randMult = 0x5DEECE66DL;
        long randAdd = 0xBL;
        long randMask = (1L << 48) - 1;
        long seed = _length;
        for (int i = 0; i < _length; i++) {
            long x = (seed * randMult + randAdd) & randMask;
            seed = x;
            hash += (_key[_start + i] + 128) * x;
        }
        return hash;
    }
    static AExtractor cKeys = new ExtractKey();
    static AExtractor cPayload = new ExtractPayload();
}
