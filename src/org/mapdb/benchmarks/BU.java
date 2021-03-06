package org.mapdb.benchmarks;


import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BU {

    public final static String TMPDIR = System.getProperty("java.io.tmpdir");
    private final static String chars = "0123456789abcdefghijklmnopqrstuvwxyz !@#$%^&*()_+=-{}[]:\",./<>?|\\";

    public static byte[] randomByte(int size, long seed) {
        byte[] b = new byte[size];
        Random r = new Random(seed);
        r.nextBytes(b);
        return b;
    }

    public static byte[] randomByte(int size) {
        byte[] b = new byte[size];
        Random r = new Random();
        r.nextBytes(b);
        return b;
    }

    public static String randomString(int size, long seed) {
        StringBuilder b = new StringBuilder(size);
        Random r = new Random(seed);
        for(int i=0;i<size;i++){
            b.append(chars.charAt(r.nextInt(chars.length())));
        }
        return b.toString();
    }

    public static String randomString(int size) {
        StringBuilder b = new StringBuilder(size);
        Random r = new Random();
        for(int i=0;i<size;i++){
            b.append(chars.charAt(r.nextInt(chars.length())));
        }
        return b.toString();
    }



    public static void mkdir(String dir){
        new File(dir).mkdirs();
    }

    public static long randomLong(long maxVal) {
        return Math.abs(new Random().nextLong()%maxVal);
    }

    public static void shutdown(ExecutorService t){
        t.shutdown();
        try {
            t.awaitTermination(9999, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    static private AtomicLong printCounter = new AtomicLong();
    static private volatile long printTime;
    static private String printDesc;

    static public void printStart(String desc){
        printDesc = desc;
        printTime = System.currentTimeMillis();
        printCounter.set(0);
    }

    static public void printEnd(){
        long t = System.currentTimeMillis()-printTime;
        if(t==0)
            t=1;
        long c = printCounter.get();
        System.out.printf("%30s  @  " +
                //"%,d per second" +
                "%d" +
                "\n",printDesc, (1000L * c)/t);

    }

    static public void printIncrement(){
        printCounter.incrementAndGet();
    }

    static boolean printIncrementTime(long time){
        printCounter.incrementAndGet();
        return printTime + time > System.currentTimeMillis();
    }


    static public void gc(){
        for(int i=0;i<10;i++){
            System.gc();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }

    }


    static public Iterator<Long> reverseLongIterator(final long start, final long end){

        return new Iterator<Long>() {

            long counter = end;

            @Override
            public boolean hasNext() {
                return counter>=start;
            }

            @Override
            public Long next() {
                long ret = counter--;
                if(ret<start)
                    throw new NoSuchElementException();
                return ret;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}
