package com.umeclub;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 结合智能合约与底层链的辅助执行类
 */
public class Executor<T> {
    protected static final int PAGE_SIZE = 65536;

    public OutputStream out = System.out;

    protected final ByteBuffer mem;

    protected final List<Integer> strPtrs = new ArrayList<>();

    protected final List<String> exactlyResult = new ArrayList<>();

    protected final T instance;

    protected Integer exitCode;

    private int offsetPtr = 8192;

    public Executor(Constructor m)
            throws InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this(16, m);
    }

    public Executor(int maxMemPages, Constructor m) throws InstantiationException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassCastException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType oneType = MethodType.methodType(int.class);
        MethodType twoType = MethodType.methodType(int.class, int.class);
        MethodType threeType = MethodType.methodType(int.class, int.class, int.class);
        // MethodType fourType = MethodType.methodType(int.class, int.class, int.class, int.class);
        MethodType fiveType = MethodType.methodType(int.class, int.class, int.class, int.class, int.class);
        mem = ByteBuffer.allocateDirect(maxMemPages * PAGE_SIZE);
        instance = (T) m.newInstance(mem, lookup.bind(this, "print", twoType),
                lookup.bind(this, "printNum", twoType),
                lookup.bind(this, "println", twoType),
                lookup.bind(this, "printlnNum", twoType),
                lookup.bind(this, "allowed", threeType),
                lookup.bind(this, "getAddress", oneType),
                lookup.bind(this, "getMyBalance", oneType),
                lookup.bind(this, "getOtherBalance", twoType),
                lookup.bind(this, "initToken", fiveType),
                lookup.bind(this, "setAllowed", threeType),
                lookup.bind(this, "setBalance", twoType),
                lookup.bind(this, "setOtherBalance", threeType)
                );
    }

    public Integer run(Method run) throws IllegalAccessException, InvocationTargetException {
        run.invoke(instance);
        return exitCode;
    }

    public Integer run(Method run, String[] args) throws IllegalAccessException, InvocationTargetException {
        int offset = 4096;
        for (String arg : args) {
            strPtrs.add(offset);
            if (isNumeric(arg)) {
                exactlyResult.add(arg);
            } else {
                exactlyResult.add(String.valueOf(offset));
            }
            offset += newString(arg, offset);
        }
        for (int strPtr : strPtrs) {
            mem.putLong(offset, strPtr);
            offset += 8;
        }
        // Run and return exit code
        run.invoke(instance, exactlyResult.stream().map(r -> Integer.parseInt(r)).toArray());
        return exitCode;
    }

    // Returns size, aligned to 8
    protected int newString(String str, int ptr) {
        byte[] bytes = (str + '\0').getBytes(StandardCharsets.UTF_8);
        putBytes(ptr, bytes);
        return bytes.length + (8 - (bytes.length % 8));
    }

    protected byte[] getBytes(int offset, byte[] bytes) {
        ByteBuffer buf = mem.duplicate();
        buf.position(offset);
        buf.get(bytes);
        return bytes;
    }

    protected void putBytes(int offset, byte[] bytes) {
        ByteBuffer buf = mem.duplicate();
        buf.position(offset);
        buf.put(bytes);
    }

    protected int printlnNum(int sp) {
        try {
            out.write((String.valueOf(sp) + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

    protected int printNum(int sp) {
        try {
            out.write((String.valueOf(sp)).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

    protected int println(int sp) {
        try {
            String result = getParam(sp);
            out.write((result + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

    protected int print(int sp) {
        try {
            String result = getParam(sp);
            out.write(result.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

    protected int allowed(int a, int b) {
        System.out.println("====== allowed ======");
        String owner = getParam(a);
        String spender = getParam(b);
        int result = Account.allowed(owner, spender);
        return result;
    }

    protected int getAddress() {
        System.out.println("====== getAddress ======");
        String myAddress = "Satoshi";
        return changeToPtr(myAddress);
    }

    protected int getMyBalance() {
        System.out.println("====== getMyBalance ======");
        System.out.println("My Balance is : 4300");
        return 4300;
    }

    protected int getOtherBalance(int a) {
        System.out.println(getParam(a) + "'s Balance is: 100");
        return 100;
    }

    protected int initToken(int a, int b, int c, int d) {
        String tokenName = getParam(a);
        int num = b;
        String symbol = getParam(c);
        int precision = d;
        RC20Coin token = new RC20Coin(tokenName, num, symbol, precision);
        return 1;
    }

    protected int setAllowed(int a, int b) {
        System.out.println("====== setAllowed ======");
        return 1;
    }

    protected int setBalance(int a) {
        System.out.println("====== setBalance ======");
        Account.setBalance(a);
        return 1;
    }

    protected int setOtherBalance(int a, int b) {
        System.out.println("====== setOtherBalance ======");
        System.out.println(b);
        Account.setOtherBalance(getParam(a), b);
        return 1;
    }

    protected String getParam(int a) {
        System.out.println("====== getParam ======");
        if (!strPtrs.contains(a) /*&& !exactlyResult.contains(String.valueOf(a))*/) {
            return new String(getBytes(a, new byte[PAGE_SIZE - a])).trim();
        }/* else if(exactlyResult.contains(String.valueOf(a))) {
            return String.valueOf(a);
        } */else if(strPtrs.contains(a) && strPtrs.indexOf(a) == strPtrs.size() - 1) {
            return new String(getBytes(a, new byte[PAGE_SIZE - a])).trim();
        } else {
            return new String(getBytes(a, new byte[strPtrs.get(strPtrs.indexOf(a) + 1) - a])).trim();
        }
    }

    protected int changeToPtr(String param) {
        int preOffsetPtr = offsetPtr;
        strPtrs.add(offsetPtr);
        exactlyResult.add(String.valueOf(offsetPtr));
        offsetPtr += newString(param, offsetPtr);
        mem.position(preOffsetPtr);
        mem.put((param + '\0').getBytes(StandardCharsets.UTF_8));
        // mem.putLong(preOffsetPtr, preOffsetPtr);
        return preOffsetPtr;
    }

    protected boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
