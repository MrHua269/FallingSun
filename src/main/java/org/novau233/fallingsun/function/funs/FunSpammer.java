package org.novau233.fallingsun.function.funs;

import net.minecraft.util.text.TextComponentString;
import org.novau233.fallingsun.Util;
import org.novau233.fallingsun.function.Function;
import java.io.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class FunSpammer implements Function {
    private Mode mode = Mode.File;
    private final AtomicBoolean enable = new AtomicBoolean(false);
    private Timer timer = new Timer();
    private String content;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (enable.get()) {
                if (mc.getConnection() == null) {
                    enable.set(false);
                }
                switch (mode) {
                    case File:
                        mc.player.sendChatMessage(content);
                        break;
                    case Random:
                        mc.player.sendChatMessage(Util.getRandomString(new Random().nextInt(30), new Random().nextInt(255)));
                        break;
                }
            }
        }
    };

    @Override
    public void onClientTick() {}

    @Override
    public void onCommandExecute(String[] arg) {
        this.task.cancel();
        this.timer.purge();
        this.timer.cancel();
        this.timer = null;
        this.task = new TimerTask() {
            @Override
            public void run() {
                if (enable.get()) {
                    switch (mode) {
                        case File:
                            mc.player.sendChatMessage(content);
                            break;
                        case Random:
                            mc.player.sendChatMessage(Util.getRandomString(new Random().nextInt(30), new Random().nextInt(255)));
                            break;
                    }
                }
            }
        };
        this.timer = new Timer();
        if (!this.enable.get()) {
            if (arg.length != 2) {
                mc.player.sendMessage(new TextComponentString("Eror args.The right args is : #spammer <File,Random> <Time>"));
                return;
            }
            this.enable.set(true);
            File file = new File("spammer.txt");
            try {
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    this.content = new String(buffer);
                } else {
                    file.createNewFile();
                    FileWriter writer = new FileWriter(file);
                    writer.write("Fuck servers");
                    this.content = "Fuck servers";
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mode = Mode.valueOf(arg[0]);
            this.timer.scheduleAtFixedRate(this.task, 0L, (long) (Float.parseFloat(arg[1]) * 1000));
            mc.player.sendMessage(new TextComponentString("Spammer enabled!"));
        } else {
            this.enable.set(false);
            mc.player.sendMessage(new TextComponentString("Spammer disabled!"));
        }
    }

    @Override
    public String getHead() {
        return "spammer";
    }

    @Override
    public boolean enabled() {
        return this.enable.get();
    }

    @Override
    public void onDisable() {
        this.enable.set(false);
    }

    private enum Mode {
        File,
        Random
    }
}
