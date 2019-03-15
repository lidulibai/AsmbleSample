package com.umeclub;

import java.util.ArrayList;
import java.util.Arrays;

import asmble.cli.Command;
import asmble.cli.Command.ArgsBuilder.ActualArgBuilder;
import asmble.cli.Invoke;
import asmble.cli.Main;
import asmble.cli.Main.GlobalArgs;
import asmble.util.Logger;

public class InvokeTest {

    public static void main( String[] args )
    {
        Logger.Print logger = new Logger.Print(Logger.Level.WARN);
        ArrayList<String> invokeWast = null;
        if (args.length > 0) {
            invokeWast = new ArrayList<String>();
            invokeWast.add("-res");
            invokeWast.add("-in");
            invokeWast.add("E:\\js_workspace\\node-wasm\\test.wast");
            for (int i = 0; i < args.length; i++) {
                invokeWast.add(args[i]);
            }
        } else {
            invokeWast = new ArrayList<String>(Arrays.asList("-res", "-in", "E:\\js_workspace\\node-wasm\\test.wast", "add", "80", "20"));
        }
        System.out.println(invokeWast);
        Command cmd = null;
        try {
            cmd = new Invoke();
            Command.ArgsBuilder argBuild = new ActualArgBuilder(invokeWast);
            GlobalArgs globals = Main.INSTANCE.globalArgs(argBuild);
            logger = new Logger.Print(globals.getLogLevel());
            cmd.setLogger(logger);
            cmd.runWithArgs(argBuild);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
