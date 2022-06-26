package org.novau233.fallingsun.function;

import net.minecraft.client.Minecraft;

public interface Function {
    final Minecraft mc = Minecraft.getMinecraft();
    public void onClientTick();
    public void onCommandExecute(String[] arg);
    public String getHead();
    public boolean enabled();
    public void onDisable();
}
