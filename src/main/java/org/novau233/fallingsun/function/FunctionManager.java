package org.novau233.fallingsun.function;

import org.novau233.fallingsun.function.funs.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionManager {
    public static final List<Function> functions = new ArrayList<>();
    public static void init(){
        functions.add(new FunCrash());
        functions.add(new FunNoFall());
        functions.add(new FunSpammer());
        functions.add(new FunFly());
        functions.add(new FunMotdAttack());
    }
}
