package org.novau233.fallingsun.function.funs;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.text.TextComponentString;
import org.novau233.fallingsun.function.Function;

public class FunNoFall implements Function {
    private boolean enable = false;

    @Override
    public void onClientTick() {
        if (this.enable){
            if (mc.player!=null){
                if (Minecraft.getMinecraft().player.fallDistance > 2){
                    if (Minecraft.getMinecraft().getConnection()!=null){
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer(true));
                    }
                }
            }
        }
    }

    @Override
    public void onCommandExecute(String[] arg) {
        if (!enable){
            this.enable = true;
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Nofall enabled!"));
        }else {
            this.enable = false;
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Nofall disabled!"));
        }
    }

    @Override
    public String getHead() {
        return "nofall";
    }

    @Override
    public boolean enabled() {
        return this.enable;
    }

    @Override
    public void onDisable() {
        this.enable = false;
    }
}
