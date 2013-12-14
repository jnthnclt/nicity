package com.jonathancom.colt.nicity.bytecollections;

/*
 * #%L
 * nicity-io
 * %%
 * Copyright (C) 2013 Jonathan Colt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.jonathancolt.nicity.core.comparator.AValueComparator;
import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.UMath;
import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.core.memory.struct.Tuple2;
import java.util.Arrays;

import static com.jonathancom.colt.nicity.bytecollections.ByteArrayFixedSizeSet.*;

//!! NOT FINISHED this is a starting point impl
/**
 * This is a skip list which is backed by a byte[] via a SByteArrayFixedSizeSet
 * This collection wastes quite a bit of space infavor of page in and out speed.
 * May make sense to use a compression strategy when sending over the wire.
 * Each entry you sadd cost a fixed amount of space. This can be calculate by the following:
 * entrySize = entrySize+(1+(entrySize*maxColumHeight);
 * maxColumHeight = ? where 2 ^ ? is > maxCount
 *
 * The key composed of all BYTE.MIN_VALUE is reserved as the head of the list.
 * 
 */
public class ByteArraySkipListSet {

    private static final int cColumKeySize = 4;// stores the int index of the key it points to
    private byte[] set;
    private ByteArrayComparator valueComparator;
    private byte[] headKey;
    byte maxHeight;
    int iStart;
    // new

    public ByteArraySkipListSet(int _maxCount, int _keySize, int _payloadSize, ByteArrayComparator _valueComparator) {
        _maxCount += 2;
        valueComparator = _valueComparator;
        maxHeight = (byte) UMath.powerFit(_maxCount);
        // column structure byte height+ cColumKeySize*maxHeight+actualPayload
        set = sallocate(_maxCount, _keySize, slpayloadSize(maxHeight) + _payloadSize);
        headKey = new byte[_keySize];
        byte[] headPayload = new byte[_payloadSize];
        Arrays.fill(headKey, Byte.MIN_VALUE);
        Arrays.fill(headPayload, Byte.MIN_VALUE);
        byte[] newColumn = newColumn(headPayload, maxHeight, (byte) maxHeight);
        iStart = sadd(set, (byte) 1, headKey, newColumn);
        toSysOut();
        //System.exit(0);
        //stoSysOut(set);
    }
    // exisiting

    public ByteArraySkipListSet(byte[] _set, AValueComparator<byte[]> _valueComparator) {
        set = _set;
        maxHeight = (byte) UMath.powerFit(sgetCapacity(set));
        headKey = new byte[sgetKeySize(set)];
        Arrays.fill(headKey, Byte.MIN_VALUE);
    }

    public byte[] getBytes() {
        return set;
    }

    static public int slcost(int _maxKeys, int _keySize, int _payloadSize) {
        byte maxHeight = (byte) UMath.powerFit(_maxKeys);
        return scost(_maxKeys, _keySize, slpayloadSize(maxHeight) + _payloadSize);
    }

    static final int slpayloadSize(byte maxHeight) {
        return 1 + (cColumKeySize * maxHeight);
    }

    synchronized public long getCount() {
        return sgetCount(set) - 1; // -1 because of head
    }

    synchronized public void add(byte[] _key, byte[] _payload) {

        int index = sgetIndex(set, _key);
        if (index != -1) { // aready exists so just update payload
            ssetPayloadAtIndex(set, index, startOfPayload(maxHeight), _payload, 0, _payload.length);
            return;
        }
        int skeySize = sgetKeySize(set);
        int spayloadSize = sgetPayloadSize(set);
        // create a new colum for a new key
        byte[] newColumn = newColumn(_payload, maxHeight, (byte) -1);
        int insertsIndex = sadd(set, (byte) 1, _key, newColumn);


        int level = maxHeight - 1;
        int ilevel = columnLength(set, insertsIndex, maxHeight);
        int atIndex = iStart;
        while (level > -1) {
            int nextIndex = rcolumnNext(set, atIndex, level);
            if (nextIndex == -1) {
                if (level < ilevel) {
                    wcolumnNext(set, atIndex, level, insertsIndex);
                }
                level--;
            } else {
                int compare = valueComparator.compare(set, sstartOfKey(nextIndex, skeySize, spayloadSize), set, sstartOfKey(insertsIndex, skeySize, spayloadSize), skeySize);
                if (compare == 0) {
                    throw new RuntimeException("should be impossible");
                } else if (compare < 0) { // keep looking forward
                    atIndex = nextIndex;
                } else { // insert
                    if (level < ilevel) {
                        wcolumnNext(set, insertsIndex, level, nextIndex);
                        wcolumnNext(set, atIndex, level, insertsIndex);
                    }
                    level--;
                }
            }
        }
    }

    synchronized public void remove(byte[] _key) {
        if (_key == null || _key.length == 0) {
            throw new RuntimeException("null not supported");
        }
        int removeIndex = sgetIndex(set, _key);
        if (removeIndex == -1) { // doesn't exists so return
            return;
        }
        int skeySize = sgetKeySize(set);
        int spayloadSize = sgetPayloadSize(set);

        int level = maxHeight - 1;
        int atIndex = iStart;
        while (level > -1) {
            int nextIndex = rcolumnNext(set, atIndex, level);
            if (nextIndex == -1) {
                level--;
            } else {
                int compare = valueComparator.compare(set, sstartOfKey(nextIndex, skeySize, spayloadSize), set, sstartOfKey(removeIndex, skeySize, spayloadSize), skeySize);
                if (compare == 0) {
                    while (level > -1) {
                        int removesNextIndex = rcolumnNext(set, removeIndex, level);
                        wcolumnNext(set, atIndex, level, removesNextIndex);
                        //byte[] removesNext = remove.columnKey(level);
                        //atIndex.columnKey(level, removesNext);
                        level--;
                    }
                    //atIndex.save();

                } else if (compare < 0) {
                    atIndex = nextIndex;
                } else {
                    level--;
                }
            }
        }
        sremove(set, _key);
    }

    synchronized public byte[] getExisting(byte[] _key) {
        if (_key == null || _key.length == 0) {
            throw new RuntimeException("null not supported");
        }
        int index = sgetIndex(set, _key);
        if (index == -1) {
            return null;
        } else {
            return getColumnPayload(set, index, maxHeight);
        }
    }

    synchronized public byte[] getAfterExisting(byte[] key) {
        if (key == null || key.length == 0) {
            throw new RuntimeException("null not supported");
        }
        int index = sgetIndex(set, key);
        if (index == -1) {
            return null;
        }
        int nextIndex = rcolumnNext(set, index, 0);
        return sgetKeyAtIndex(set, nextIndex);
    }

    public void getInOrder(byte[] from, byte[] to, int _max, ByteArraysStream<BytesArrayKeyValue> _get) throws Exception {

        int at = -1;
        if (from != null && from.length > 0) {
            int index = sgetIndex(set, from);
            if (index == -1) {
                _get.stream(null);// done cause from doesn't exist
                return;
            }

        } else {
            at = iStart;
        }

        done:
        while (at != -1) {
            byte[] key = sgetKeyAtIndex(set, at);
            byte[] payload = getColumnPayload(set, at, maxHeight);
            BytesArrayKeyValue sent = new BytesArrayKeyValue(key, payload);
            if (_get.stream(sent) != sent) {
                break;
            }
            if (to != null) {
                for (int i = 0; i < to.length; i++) {
                    if (key[i] != to[i]) {
                        break done;
                    }
                }
            }
            at = rcolumnNext(set, at, 0);
            if (_max > -1) {
                _max--;
                if (_max < 0) {
                    break;

                }
            }
        }
        _get.stream(null);//EOS
    }
    static URandom.Seed seed = new URandom.Seed(System.currentTimeMillis());

    static final private byte[] newColumn(byte[] _payload, int _maxHeight, byte _height) {
        if (_height <= 0) {
            byte newH = 1;
            while (URandom.rand(seed, 1d) > 0.5d) {// could pick a rand number bewteen 1 and 32 instead
                if (newH + 1 >= _maxHeight) {
                    break;
                }
                newH++;
            }
            _height = newH;
        }
        assert _height > 0 : _height < 32;
        byte[] column = new byte[1 + (_maxHeight * cColumKeySize) + _payload.length];
        column[0] = _height;
        for (int i = 0; i < _maxHeight; i++) {
            setColumKey(column, i, UIO.intBytes(-1));// fill with nulls ie -1
        }
        System.arraycopy(_payload, 0, column, 1 + (_maxHeight * cColumKeySize), _payload.length);
        return column;
    }

    static final private void setColumKey(byte[] _column, int _h, byte[] _key) {
        System.arraycopy(_key, 0, _column, 1 + (_h * cColumKeySize), cColumKeySize);
    }

    static final private byte columnLength(byte[] set, int setIndex, int maxHeight) {
        int keySize = sgetKeySize(set);
        int payloadSize = sgetPayloadSize(set);
        return set[sstartOfPayload(setIndex, keySize, payloadSize)];
    }

    static final private int rcolumnNext(byte[] set, int setIndex, int level) {
        int keySize = sgetKeySize(set);
        int payloadSize = sgetPayloadSize(set);
        int offset = (int) (sstartOfPayload(setIndex, keySize, payloadSize) + 1 + (level * cColumKeySize));
        int v = 0;
        v |= (set[offset + 0] & 0xFF);
        v <<= 8;
        v |= (set[offset + 1] & 0xFF);
        v <<= 8;
        v |= (set[offset + 2] & 0xFF);
        v <<= 8;
        v |= (set[offset + 3] & 0xFF);
        return v;
    }

    static final private void wcolumnNext(byte[] set, int setIndex, int level, int v) {
        int keySize = sgetKeySize(set);
        int payloadSize = sgetPayloadSize(set);
        int offset = (int) (sstartOfPayload(setIndex, keySize, payloadSize) + 1 + (level * cColumKeySize));
        set[offset + 0] = (byte) (v >>> 24);
        set[offset + 1] = (byte) (v >>> 16);
        set[offset + 2] = (byte) (v >>> 8);
        set[offset + 3] = (byte) v;
    }

    static final private int startOfPayload(int maxHeight) {
        return 1 + (cColumKeySize * maxHeight);
    }

    static final private byte[] getColumnPayload(byte[] set, int setIndex, int maxHeight) {
        int keySize = sgetKeySize(set);
        int payloadSize = sgetPayloadSize(set);
        int startOfPayload = sstartOfPayload(setIndex, keySize, payloadSize);
        int size = payloadSize - startOfPayload(maxHeight);
        byte[] payload = new byte[size];
        System.arraycopy(set, startOfPayload + 1 + (maxHeight * cColumKeySize), payload, 0, size);
        return payload;
    }

    // debugging aids
    public void toSysOut() {
        int atIndex = iStart;
        while (atIndex != -1) {
            toSysOut(atIndex);
            atIndex = rcolumnNext(set, atIndex, 0);
        }
    }

    private void toSysOut(int index) {
        byte[] key = sgetKeyAtIndex(set, index);
        System.out.print("\t" + new String(key) + " - ");
        int l = columnLength(set, index, maxHeight);
        System.out.print("Lenght=" + l);
        for (int i = 0; i < l; i++) {
            if (i != 0) {
                System.out.print(",");
            }
            int ni = rcolumnNext(set, index, i);
            System.out.print("nextI=" + ni);
            /*
            if (ni == -1) {
            System.out.print(i+"=NULL("+ni+")");
            
            } else {
            byte[] nkey = sgetKeyAtIndex(set, ni);
            System.out.print(i+"="+new String(nkey)+"("+ni+")");
            
            }*/
        }
        System.out.println();
    }
}
