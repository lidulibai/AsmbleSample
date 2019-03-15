package com.umeclub;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.junit.Test;

import asmble.cli.Command;
import asmble.cli.Command.ArgsBuilder.ActualArgBuilder;
import asmble.cli.Main;
import asmble.cli.Main.GlobalArgs;
import asmble.cli.Translate;
import asmble.util.Logger;


/**
 * Unit test for simple App.
 */
public class AppTest
{
    // @Test
    // public void testDB() {
        // File file = new File("dbfile");
        // try {
            // DB db = Iq80DBFactory.factory.open(new File(file, "db"), new Options().createIfMissing(true));
            // db.put("hello".getBytes(), "world".getBytes());
            // // System.out.println(new String(db.get("hello".getBytes())));
            // assertEquals("world", new String(db.get("hello".getBytes())));
        // } catch(Exception e){
            // e.printStackTrace();
        // }
    // }

    @Test
    public void testAsmbleTraslate() {
        File file = new File("dbfile");
        Logger.Print logger = new Logger.Print(Logger.Level.WARN);
        ArrayList<String> invokeWast = new ArrayList<String>();
        invokeWast.add("C:\\Users\\Administrator\\Downloads\\asmble\\bin\\out.wast");
        invokeWast.add("C:\\Users\\Administrator\\Downloads\\asmble\\bin\\tmp.wasm");
        Command cmd = null;
        DB db = null;
        ByteArrayOutputStream bos = null;
        try {
            cmd = new Translate();
            Command.ArgsBuilder argBuild = new ActualArgBuilder(invokeWast);
            GlobalArgs globals = Main.INSTANCE.globalArgs(argBuild);
            logger = new Logger.Print(globals.getLogLevel());
            cmd.setLogger(logger);
            cmd.runWithArgs(argBuild);
            db = Iq80DBFactory.factory.open(new File(file, "db"), new Options().createIfMissing(true));
            File binFile = new File("C:\\Users\\Administrator\\Downloads\\asmble\\bin\\tmp.wasm");
            FileInputStream fis = new FileInputStream(binFile);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            db.put("hello".getBytes(), bos.toByteArray());
            System.out.print(new String(db.get("hello".getBytes())));
            db.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
