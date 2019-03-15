package com.umeclub;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.umeclub.util.AsmbleUtil;
import com.umeclub.util.CheckUtil;
import com.umeclub.util.CommonUtil;


/**
 * 执行合约的入口
 *
 */
public class App
{
    public static void main( String[] args )
    {
        if (args.length > 0) {
            // 智能合约完整路径
            String fileName = args[0];
            if (CheckUtil.isWastOrWasmFile(fileName)) {
                try {
                    // 根据智能合约的文件名转为对应的类名
                    String className = CommonUtil.getNameByWastFile(fileName);
                    String fileFullName = System.getProperty("user.dir") + "/target/classes/com/umeclub/"+className+".class";
                    String fullClassName = "com.umeclub." + className;

                    CommonUtil.importBasicLib(fileName);
                    // 把webassemble格式的智能合约编译成class文件
                    AsmbleUtil.compile(fileName, className, fileFullName);
                    ClassLoader parentLoader = App.class.getClassLoader();
                    MyClassLoader classLoader = new MyClassLoader(parentLoader, fullClassName, fileFullName);
                    Class<?> contractClass = classLoader.loadClass(fullClassName);

                    // 编译之后的两种构造函数：
                    //      1. (int, MethodHandle...)
                    //      2. (ByteBuffer, MethodHandle...)
                    Constructor[] constructors = contractClass.getDeclaredConstructors();
                    Constructor cons = constructors[1];
                    String methodName = args[1];
                    if (args.length > 1) {
                        Class[] methodParams = new Class[args.length - 2];
                        String[] passArgs = new String[methodParams.length];
                        for (int j = 2; j < args.length; j++) {
                            methodParams[j - 2] = int.class;
                            passArgs[j - 2] = args[j];
                        }
                        new Executor(cons).run(contractClass.getMethod(methodName, methodParams), passArgs);
                    } else {
                        new Executor(cons).run(contractClass.getMethod(methodName));
                    }
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
