package com.umeclub.util;

import java.util.ArrayList;
import java.util.Arrays;

import asmble.cli.Command;
import asmble.cli.Command.ArgsBuilder.ActualArgBuilder;
import asmble.cli.Compile;
import asmble.cli.Main;
import asmble.cli.Main.GlobalArgs;
import asmble.util.Logger;

public class AsmbleUtil {
    public static void compile(String fileName, String className, String filePath) {
        Logger.Print logger = new Logger.Print(Logger.Level.WARN);
        ArrayList<String> invokeWast = null;
        Command cmd = null;
        try {
            cmd = new Compile();
            invokeWast = new ArrayList<String>(Arrays.asList(fileName, "com.umeclub."+className, "-out", filePath));
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
