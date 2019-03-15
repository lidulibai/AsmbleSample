package com.umeclub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyClassLoader extends ClassLoader{

    private String className;
    private String fileFullName;

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public MyClassLoader(ClassLoader parent, String fullClassName, String fileFullName) {
        super(parent);
        this.className = fullClassName;
        this.fileFullName = fileFullName;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        if(!className.equals(name))
                return super.loadClass(name);

        try {
            // String url = "file:E:/maven_space/AsmbleSample/target/" +
                            // "classes/com/umeclub/Token.class";
            String url = "file:" + fileFullName;
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass(className, classData, 0, classData.length);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
