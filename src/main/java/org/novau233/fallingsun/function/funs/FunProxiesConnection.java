package org.novau233.fallingsun.function.funs;

import net.minecraft.util.text.TextComponentString;
import org.novau233.fallingsun.AsyncCommandHandler;
import org.novau233.fallingsun.function.Function;
import org.novau233.fallingsun.mixins.FeildCache;

public class FunProxiesConnection implements Function {

    @Override
    public void onClientTick() {}

    @Override
    public void onCommandExecute(String[] arg) {
        if (FeildCache.enableProxyConnection.get()){
            mc.player.sendMessage(new TextComponentString("Proxied connection enabled!Current proxies:"+FeildCache.proxies.size()));
            FeildCache.enableProxyConnection.set(false);
            if (FeildCache.proxies.size() - 1 <= FeildCache.currentIndex.get()){
                AsyncCommandHandler.COMMAND_EXECUTOR.execute(FeildCache::initProxies);
            }
        }else {
            mc.player.sendMessage(new TextComponentString("Proxied connection disabled!"));
            FeildCache.enableProxyConnection.set(true);
        }
    }

    @Override
    public String getHead() {
        return "proxyconnection";
    }

    @Override
    public boolean enabled() {
        return FeildCache.enableProxyConnection.get();
    }

    @Override
    public void onDisable() {
        FeildCache.enableProxyConnection.set(false);
    }
}
