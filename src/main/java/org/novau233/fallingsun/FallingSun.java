package org.novau233.fallingsun;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import org.novau233.fallingsun.function.Function;
import org.novau233.fallingsun.function.FunctionManager;
import org.novau233.fallingsun.mixins.FeildCache;

@Mod(
        modid = FallingSun.MOD_ID,
        name = FallingSun.MOD_NAME,
        version = FallingSun.VERSION
)
public class FallingSun {

    public static final String MOD_ID = "fallingsun";
    public static final String MOD_NAME = "FallingSun";
    public static final String VERSION = "1.11.0";


    @Mod.Instance(MOD_ID)
    public static FallingSun INSTANCE;


    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ObjectRegistryHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("FallingSun v1.11.0 dev by Novau233");
        FunctionManager.init();
        AsyncCommandHandler.COMMAND_EXECUTOR.execute(()->{
            LogManager.getLogger().info("Getting proxies...");
            FeildCache.initProxies();
        });
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        @SubscribeEvent
        public static void onChatSent(Event event1) {
            if (event1 instanceof ClientChatEvent){
                ClientChatEvent event = (ClientChatEvent) event1;
                String content = event.getMessage();
                if (content.startsWith("@")){
                    event.setCanceled(true);
                    AsyncCommandHandler.handleCommand(content);
                }
                return;
            }
            if (event1 instanceof TickEvent.ClientTickEvent){
                for (Function f : FunctionManager.functions){
                    f.onClientTick();
                }
            }
        }
    }

}
