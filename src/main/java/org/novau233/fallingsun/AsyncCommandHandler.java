package org.novau233.fallingsun;

import org.apache.logging.log4j.LogManager;
import org.novau233.fallingsun.function.Function;
import org.novau233.fallingsun.function.FunctionManager;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncCommandHandler {
    public static final Executor COMMAND_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static void handleCommand(String command){
        COMMAND_EXECUTOR.execute(()->{
            LogManager.getLogger().info("Got a command task.Command:" + command);
            String[] argsWithHead = command.split(" ");
            String[] args = new String[argsWithHead.length-1];
            System.arraycopy(argsWithHead, 1, args, 0, argsWithHead.length - 1);
            LogManager.getLogger().info("Command args:"+ Arrays.toString(args));
            for (Function f : FunctionManager.functions){
                if (("@"+f.getHead()).equals(argsWithHead[0].toLowerCase())){
                    f.onCommandExecute(args);
                }
            }
        });
    }
}
