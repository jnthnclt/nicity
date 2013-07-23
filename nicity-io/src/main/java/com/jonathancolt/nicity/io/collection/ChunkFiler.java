package com.jonathancolt.nicity.io.collection;


import com.jonathancolt.nicity.io.Filer;
import com.jonathancolt.nicity.io.SubFiler;
import com.jonathancolt.nicity.io.SubFilers;
import com.jonathancolt.nicity.io.UFile;
import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.IOut;
import java.io.File;

public class ChunkFiler {
    private static final long cMagicNumber = Long.MAX_VALUE;
    private static final int cMinPower = 8;
   
    private long lengthOfFile = 8+8+(8*(64-cMinPower));
    private long referenceNumber = 0;
    
    private SubFiler filer;

    /*
    New Call Sequence
    ChunkFiler chunks = ChunkFiler();
    chunks.setup(100);
    open(_filer);
     */
    
    public ChunkFiler() {}

    /*
     * file header format
     * lengthOfFile
     * referenceNumber
     * free 2^8
     * free 2^9
     * thru
     * free 2^64
     */
    public void setup(long _referenceNumber) {
        lengthOfFile = 8+8+(8*(64-cMinPower));
        referenceNumber = _referenceNumber;
    }
    
    public long bytesNeeded() {
        return Long.MAX_VALUE;
    }
    
    public void open(SubFiler _filer) throws Exception {
        filer = _filer;
        synchronized (filer.lock()) {
            UIO.writeLong(filer, lengthOfFile, "lengthOfFile");
            UIO.writeLong(filer, referenceNumber, "referenceNumber");
            for(int i=cMinPower;i<65;i++) {
                UIO.writeLong(filer, -1, "free");
            }
            filer.flush();
        }
    }
    
    /*
    Exsisting Call Sequence
    ChunkFiler chunks = ChunkFiler(_filer);
    open();
     */
    
    public ChunkFiler(SubFiler _filer) throws Exception {
        filer = _filer;
    }
    
    public void open() throws Exception {
        synchronized (filer.lock()) {
            filer.seek(0);
            lengthOfFile = UIO.readLong(filer, "lengthOfFile");
            referenceNumber = UIO.readLong(filer, "referenceNumber");
        }
    }
    
    public long getReferenceNumber() {
        return referenceNumber;
    }

    public void allChunks(IOut _,ICallback<Long,Long>  _chunks) throws Exception {
        synchronized (filer.lock()) {
            filer.seek(8+8+(8*(64-cMinPower)));
            long size = filer.getSize();
            while(filer.getFilePointer() < size) {
                long chunkFP =  filer.getFilePointer();
                long magicNumber = UIO.readLong(filer, "magicNumber");
                if (magicNumber != cMagicNumber) {
                    throw new Exception("Invalid chunkFP " + chunkFP);
                }
                long chunkPower = UIO.readLong(filer, "chunkPower");
                UIO.readLong(filer, "chunkNexFreeChunkFP");
                long chunkLength = UIO.readLong(filer, "chunkLength");
                long fp = filer.getFilePointer();
                if (chunkLength > 0) {
                    long more = _chunks.callback(chunkFP);
                    if (more != chunkFP) break;
                }
                filer.seek(fp+UIO.chunkLength(chunkPower));
            }
        }
    }

    public long newChunk(long _chunkLength) throws Exception {
        long resultFP = 0;
        synchronized (filer.lock()) {
            long chunkPower = UIO.chunkPower(_chunkLength,cMinPower);
            resultFP = resuseChunk(chunkPower);
            if (resultFP == -1) {
                long chunkLength = UIO.chunkLength(chunkPower);
                chunkLength += 8; // add magicNumber
                chunkLength += 8; // add chunkPower
                chunkLength += 8; // add next free chunk of equal size
                chunkLength += 8; // add bytesLength
                long newChunkFP = lengthOfFile;
                if (newChunkFP + chunkLength > filer.endOfFP()) {
                    //!! to do over flow allocated chunk request reallocation
                    throw new RuntimeException("need larger allocation for ChunkFile" + this);
                }
                synchronized (filer.lock()) {
                    filer.seek(newChunkFP + chunkLength - 1);// last byte in chunk
                    filer.write(0);// cause file backed ChunkFiler to grow file on disk
                    filer.seek(newChunkFP);
                    UIO.writeLong(filer, cMagicNumber, "magicNumber");
                    UIO.writeLong(filer, chunkPower, "chunkPower");
                    UIO.writeLong(filer, -1, "chunkNexFreeChunkFP");
                    UIO.writeLong(filer, _chunkLength, "chunkLength");
                    lengthOfFile += chunkLength;
                    filer.seek(0);
                    UIO.writeLong(filer, lengthOfFile, "lengthOfFile");
                    filer.flush();
                }
                return newChunkFP;
            }
        }
        return resultFP;
    }
    private long resuseChunk(long _chunkPower) throws Exception {
        synchronized (filer.lock()) {
            filer.seek(freeSeek(_chunkPower));
            long reuseFP = UIO.readLong(filer, "free");
            if (reuseFP == -1) return reuseFP;
            long nextFree = readNextFree(reuseFP);
            filer.seek(freeSeek(_chunkPower));
            UIO.writeLong(filer,nextFree,"free");
            return reuseFP;
        }
    }



    
    public SubFiler getFiler(long _chunkFP) throws Exception {
        long chunkPower = 0;
        long nextFreeChunkFP = 0;
        long length = 0;
        long fp = 0;
        synchronized (filer.lock()) {
            filer.seek(_chunkFP);
            long magicNumber = UIO.readLong(filer, "magicNumber");
            if (magicNumber != cMagicNumber) {
                throw new Exception("Invalid chunkFP " + _chunkFP);
            }
            chunkPower = UIO.readLong(filer, "chunkPower");
            nextFreeChunkFP = UIO.readLong(filer, "chunkNexFreeChunkFP");
            length = UIO.readLong(filer, "chunkLength");
            fp = filer.getFilePointer();
        }

        try {
            return filer.get(fp,fp + UIO.chunkLength(chunkPower),length);
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("_chunkFP=" + _chunkFP);
            System.out.println("nextFree=" + nextFreeChunkFP);
            System.out.println("fp=" + fp);
            System.out.println("length=" + length);
            System.out.println("chunkPower=" + chunkPower);
            throw x;
        }
    }


    public void remove(long _chunkFP) throws Exception {
        synchronized (filer.lock()) {
            filer.seek(_chunkFP);
            long magicNumber = UIO.readLong(filer, "magicNumber");
            if (magicNumber != cMagicNumber) {
                throw new Exception("Invalid chunkFP " + _chunkFP);
            }
            long chunkPower = UIO.readLong(filer, "chunkPower");
            UIO.readLong(filer, "chunkNexFreeChunkFP");
            UIO.writeLong(filer, -1, "chunkLength");
            long chunkLength = UIO.chunkLength(chunkPower); // bytes
            // fill with zeros
            while (chunkLength >= zerosMax.length) {
                filer.write(zerosMax);
                chunkLength -= zerosMax.length;
            }
            while (chunkLength >= zerosMin.length) {
                filer.write(zerosMin);
                chunkLength -= zerosMin.length;
            }
            filer.flush();
            // save as free chunk
            filer.seek(freeSeek(chunkPower));
            long freeFP = UIO.readLong(filer, "free");
            if (freeFP == -1) {
                filer.seek(freeSeek(chunkPower));
                UIO.writeLong(filer, _chunkFP, "free");
                filer.flush();
            }
            else{
                long nextFree = readNextFree(freeFP);
                filer.seek(freeSeek(chunkPower));
                UIO.writeLong(filer, _chunkFP, "free");
                writeNextFree(_chunkFP, nextFree);
                filer.flush();
            }
        }
    }

    private static final byte[] zerosMin = new byte[(int) Math.pow(2, cMinPower)]; // never too big
    private static final byte[] zerosMax = new byte[(int) Math.pow(2, 16)]; // 65536 max used until min needed


    
    

    final private long freeSeek(long _chunkPower) {
        return 8+8+((_chunkPower-cMinPower)*8);
    }
    final private long readNextFree(long _chunkFP) throws Exception {
        synchronized (filer.lock()) {
            filer.seek(_chunkFP);
            UIO.readLong(filer, "magicNumber");
            UIO.readLong(filer, "chunkPower");
            return UIO.readLong(filer, "chunkNexFreeChunkFP");
        }
    }
    final private void writeNextFree(long _chunkFP,long _nextFreeFP) throws Exception {
        synchronized (filer.lock()) {
            filer.seek(_chunkFP);
            UIO.readLong(filer, "magicNumber");
            UIO.readLong(filer, "chunkPower");
            UIO.writeLong(filer,_nextFreeFP,"chunkNexFreeChunkFP");
        }
    }
    
   
    
    
    
    
    public static String name(String _chunkName) {
        String fileName = _chunkName + ".chunk";
        UFile.ensureDirectory(new File(fileName));
        return fileName;
    }
    
    public static ChunkFiler factory(String _chunkName) throws Exception {
        if (new File(name(_chunkName)).exists()) {
            return openInstance(_chunkName);
        }
        return newInstance(_chunkName);
    }
    
    public static ChunkFiler newInstance(String _chunkName) throws Exception {
        Filer chunkFiler = Filer.open(name(_chunkName), "rw");
        SubFilers chunkSegmenter = new SubFilers(name(_chunkName),chunkFiler);
        SubFiler chunkSegment = chunkSegmenter.get(0, Long.MAX_VALUE, 0);
        ChunkFiler chunks = new ChunkFiler();
        chunks.open(chunkSegment);
        return chunks;
    }
    
    public static ChunkFiler openInstance(String _chunkName) throws Exception {
        Filer chunkFiler = Filer.open(name(_chunkName), "rw");
        SubFilers chunkSegmenter = new SubFilers(name(_chunkName),chunkFiler);
        SubFiler chunkSegment = chunkSegmenter.get(0, Long.MAX_VALUE, 0);
        ChunkFiler chunks = new ChunkFiler(chunkSegment);
        chunks.open();
        return chunks;
    }
}

