package org.novau233.fallingsun;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Util {
    public static String getRandomString(int minLength,int maxLength) {
        String str = "abcdefghijklmno!':|<>?,./pqrstuvwxyzA@#$%^&*()_+-=[]{};BCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~`";
        Random random = new Random();
        int length=random.nextInt(maxLength) % (maxLength-minLength+1)+minLength;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length()-1);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static class TickTimer{
        private long ticked = 0;
        private ConcurrentHashMap<Runnable, List<Object>> tasks = new ConcurrentHashMap<>();
        public void onTick(){
            tasks.forEach((task,options)->{
                if ((Long)options.get(1)%ticked==0){
                    try {
                        task.run();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if ((Boolean) options.get(0)){
                        tasks.remove(task);
                    }
                }
            });
            ++this.ticked;
        }
        public void scheduleTask(Runnable task,boolean once,long time){
            this.tasks.put(task,Arrays.asList(new Object[]{once, time}));
        }
        public void removeTask(Runnable task){
            this.tasks.remove(task);
        }
    }
}
