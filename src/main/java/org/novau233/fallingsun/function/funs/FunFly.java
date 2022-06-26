package org.novau233.fallingsun.function.funs;

import net.minecraft.entity.MoverType;
import net.minecraft.util.text.TextComponentString;
import org.novau233.fallingsun.function.Function;

import java.util.concurrent.atomic.AtomicBoolean;

public class FunFly implements Function {
    private final AtomicBoolean enable = new AtomicBoolean(false);

    @Override
    public void onClientTick() {
        if (mc.player!=null){
            if (enable.get()){
                mc.player.capabilities.allowFlying = true;
                mc.player.move(MoverType.SELF,0,-0.02,0);
            }else{
                mc.player.capabilities.isFlying = false;
                mc.player.capabilities.allowFlying = false;
            }
        }
    }

    @Override
    public void onCommandExecute(String[] arg) {
        if (!enable.get()){
            enable.set(true);
            mc.player.sendMessage(new TextComponentString("Flight enabled!"));
        }else {
            enable.set(false);
            mc.player.sendMessage(new TextComponentString("Flight disabled!"));
        }
    }

    @Override
    public String getHead() {
        return "flight";
    }

    @Override
    public boolean enabled() {
        return enable.get();
    }

    @Override
    public void onDisable() {
        this.enable.set(false);
    }
}
