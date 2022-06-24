package org.novau233.fallingsun;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeildCache {
    public static AtomicBoolean enableProxyConnection = new AtomicBoolean(false);
    public static List<Proxy> proxies = new CopyOnWriteArrayList<>();
    public static AtomicInteger currentIndex = new AtomicInteger(0);

    public static void initProxies(){
        LogManager.getLogger().info("Updating proxies");
        getProxysFromFile();
        getProxysFromAPI("http://www.89ip.cn/tqdl.html?api=1&num=9999");
    }

    public static void getProxysFromFile() {
        try {
            File file=new File("http.txt");
            if(!file.exists()) return;
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString=reader.readLine())!=null) {
                String[] _p = tempString.split(":");
                Proxy p1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(_p[0], Integer.parseInt(_p[1])));
                proxies.add(p1);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getProxysFromAPI(String url) {
        String ips = sendGet(url);
        Matcher matcher = matches(ips, "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:\\d{1,5}");
        while (matcher.find()) {
            String ip = matcher.group();
            if (!proxies.contains(ip)) {
                String[] _p = ip.split(":");
                Proxy p1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(_p[0], Integer.parseInt(_p[1])));
                proxies.add(p1);
            }
        }
    }

    public static Matcher matches(String str,String regex) {
        Pattern mPattern=Pattern.compile(regex);
        Matcher mMatcher=mPattern.matcher(str);
        return mMatcher;
    }

    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
