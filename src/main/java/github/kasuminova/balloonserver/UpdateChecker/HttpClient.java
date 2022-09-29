package github.kasuminova.balloonserver.UpdateChecker;

import github.kasuminova.balloonserver.BalloonServer;
import github.kasuminova.balloonserver.GUI.LayoutManager.VFlowLayout;
import github.kasuminova.balloonserver.Utils.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

public class HttpClient {
    /**
     * 从指定 URL 中获取 String 字符串
     * @param urlStr 链接
     * @return 返回的字符串
     */
    public static String getStringWithURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            String SUBMIT_METHOD_GET = "GET";

            HttpURLConnection connection;
            InputStream is = null;
            BufferedReader br = null;
            String result = null;  // 返回结果字符串
            // 通过远程 url 连接对象打开一个连接，强转成 httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：GET
            connection.setRequestMethod(SUBMIT_METHOD_GET);
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(5000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(10000);
            // 发送请求
            connection.connect();
            // 通过connection连接，请求成功后获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sb = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                result = sb.toString();
            }
            // 释放资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();  // 关闭远程连接
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }
    }

    public static void downloadFileWithURL(URL url, File file) throws IOException {
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Could Not Delete File " + file.getPath());
            }
        }
        if (!file.createNewFile()){
            throw new IOException("Could Not Create File " + file.getPath());
        }

        JFrame downloadingFrame = new JFrame("下载更新中");
        downloadingFrame.setIconImage(BalloonServer.ICON.getImage());
        downloadingFrame.setSize(new Dimension(350,145));
        downloadingFrame.setResizable(false);

        JPanel mainPanel = new JPanel(new VFlowLayout(0,10,5,true,false));
        mainPanel.add(new JLabel("进度："));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(progressBar.getMaximum());
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("连接中...");

        mainPanel.add(progressBar);
        mainPanel.add(new JLabel("在此期间你可以继续使用程序。"));
        JButton cancel = new JButton("取消下载");
        mainPanel.add(cancel);

        downloadingFrame.add(mainPanel);
        downloadingFrame.setLocationRelativeTo(null);
        downloadingFrame.setVisible(true);

        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileChannel foc = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        //[0] 为已完成进度，[1] 为速度缓存进度
        final long[] total = {0,1};
        //定时更新查询器
        Timer timer = new Timer(250, e -> {
            progressBar.setString(String.format("已完成: %s - 速度: %s/s",
                    FileUtil.formatFileSizeToStr(total[0]),
                    FileUtil.formatFileSizeToStr((total[0] - total[1]) * 4)
            ));
            total[1] = total[0];
        });
        timer.start();
        Thread currentThread = Thread.currentThread();
        cancel.addActionListener(e -> {
            //中断线程后再完成其他停止操作
            currentThread.interrupt();

            timer.stop();
            progressBar.setString("停止中...");
            try {
                bis.close();
                foc.close();
            } catch (IOException ignored) {}
            downloadingFrame.dispose();
        });

        int count;
        while((count = bis.read(byteBuffer.array(),0,4096)) != -1)
        {
            foc.write(byteBuffer, total[0]);
            total[0] += count;
            byteBuffer.flip();
            byteBuffer.clear();
        }
        bis.close();
        foc.close();

        //关闭定时器和窗口
        timer.stop();
        downloadingFrame.dispose();
    }
}