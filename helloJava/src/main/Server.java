package main;

import java.awt.AWTException;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

/**
 *使用Java截屏工具，不停的截取当前屏幕图片，图片不需保存直接以流的形式发送的监控端电脑上，并显示出来
 *控制端的鼠标和键盘的操作再发送的被控端并且执行从而实现屏幕监控
 *可以达到用一台电脑完全控制另外一台电脑
 */

public class Server{
    public static void main(String args[]) {
        SendScreenImg sender=new SendScreenImg();
        sender.changeServerPort(30009);//此处可以修改服务端口
        new Thread(sender).start();//打开图像传输服务
        OperateWindow operate=new OperateWindow();
//		operate.changeServerPort(30010);//此处可以修改服务端口
        new Thread(operate).start();//打开主机操控服务

        //***** 当然 服务器端的端口修改是随时都可以操作的 它实际上是关闭以前的端口 再开启一个新端口 *****//
    }
}

/**
 * @author LanXJ @doctime 2010-7-8
 * 开启一个设定端口的服务，该服务用于向客户端传送主机的屏幕信息，实现客户端对服务器端主机的监控
 * 实例化线程类后默认打开DEFAULT_SERVER_PORT=30011 端口实现监听
 * 可以通过changeServerPort改变监听端口，也可以通过getServerPort来查询当前监听端口
 */
class SendScreenImg implements Runnable{

    public static final int DEFAULT_SERVER_PORT=30011;
    private int serverPort;
    private Robot robot;
    private ServerSocket serverSocket;
    private Rectangle rect;
    private Dimension screen;
    private BufferedImage img;
    private Socket socket;
    private ZipOutputStream zip;

    public SendScreenImg() {
        this.serverPort=SendScreenImg.DEFAULT_SERVER_PORT;

        try {
            serverSocket = new ServerSocket(this.serverPort);
            serverSocket.setSoTimeout(86400000);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        rect = new Rectangle(screen);

    }
    public void changeServerPort(int serverPort){
        if(this.serverPort==serverPort)return;
        this.serverPort=serverPort;
        try {
            this.serverSocket.close();
        } catch (Exception e) {}
        try {
            serverSocket = new ServerSocket(this.serverPort);
            serverSocket.setSoTimeout(86400000);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public int getServerPort(){
        return this.serverPort;
    }

    public void run() {
        while (true) {
            try {
                System.out.println("等待接收截屏信息");
                socket = serverSocket.accept();
                zip = new ZipOutputStream(new DataOutputStream(socket.getOutputStream()));
                zip.setLevel(9);//为后续的 DEFLATED 条目设置压缩级别 压缩级别 (0-9)
                try {
                    img = robot.createScreenCapture(rect);
                    zip.putNextEntry(new ZipEntry("test.jpg"));
                    ImageIO.write(img, "jpg", zip);
                    if(zip!=null)zip.close();
                    System.out.println("被控端：connect");
                } catch (IOException ioe) {
                    System.out.println("被控端：disconnect");
                }
            } catch (IOException ioe) {
                System.out.println("错误1");
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}

/**
 * @author LanXJ @doctime 2010-7-8
 * 开启一个设定端口的服务，该服务用于接受客户端传来的操作字符串，实现对服务器端主机的操控
 * 实例化线程类后默认打开DEFAULT_SERVER_PORT=30012 端口实现监听
 * 可以通过changeServerPort改变监听端口，也可以通过getServerPort来查询当前监听端口
 */
class OperateWindow implements Runnable {
    public static final int DEFAULT_SERVER_PORT=30012;
    private int serverPort;
    private ServerSocket serverSocket;
    private Robot robot;
    public OperateWindow() {
        this.serverPort=OperateWindow.DEFAULT_SERVER_PORT;
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            this.serverSocket.setSoTimeout(86400000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    public void changeServerPort(int serverPort){
        if(this.serverPort==serverPort)return;
        this.serverPort=serverPort;
        try {
            this.serverSocket.close();
        } catch (Exception e) {}
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            this.serverSocket.setSoTimeout(86400000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getServerPort(){
        return this.serverPort;
    }

    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                //读取操作信息:120,200,InputEvent.BUTTON1_DOWN_MASK 全部是int类型
                InputStream is = socket.getInputStream();
                int r;
                String info = "";
                while ((r = is.read()) != -1) {
                    info += "" + (char) r;
                }
                System.out.println(info);
                is.close();
                if (info != null) {
                    String s[] = info.trim().split(",");
                    if ("mouseClicked".equals(s[0].trim())) {//operateStr Model: mouseClicked,x,y,type
                        //由于加上单击事件后，鼠标按下并快速抬起 就设计到按下、抬起、单击 三个事件，将单击变为了双击 不合乎规范  所以 服务端并没有实现单击事件的监听，这里保留 不坐修改
                        int type = Integer.parseInt(s[s.length - 1].trim());
                        if (s.length == 4) {
                            int x = Integer.parseInt(s[1].trim());
                            int y = Integer.parseInt(s[2].trim());
                            robot.mouseMove(x, y);
                            robot.mousePress(type);
                            robot.mouseRelease(type);
                            System.out.println("ClientINFO:MOUSE move to "+x+","+y+" AND execute TYPE IS click "+type);
                        }
                    }else if("mousePressed".equals(s[0].trim())){//operateStr Model: mousePressed,x,y,type
                        int type = Integer.parseInt(s[s.length - 1].trim());
                        if (s.length == 4) {
                            int x = Integer.parseInt(s[1].trim());
                            int y = Integer.parseInt(s[2].trim());
                            robot.mouseMove(x, y);
                            robot.mousePress(type);
                            System.out.println("ClientINFO:MOUSE move to "+x+","+y+" AND execute TYPE IS press "+type);
                        }
                    }else if("mouseReleased".equals(s[0].trim())){//operateStr Model: mouseReleased,x,y,type
                        int type = Integer.parseInt(s[s.length - 1].trim());
                        if (s.length == 4) {
                            int x = Integer.parseInt(s[1].trim());
                            int y = Integer.parseInt(s[2].trim());
                            robot.mouseMove(x, y);
                            robot.mouseRelease(type);
                            System.out.println("ClientINFO:MOUSE move to "+x+","+y+" AND execute TYPE IS release  "+type);
                        }
                    }else if("mouseDragged".equals(s[0].trim())){//operateStr Model: mouseDragged,x,y,type
                        if (s.length == 4) {
                            int x = Integer.parseInt(s[1].trim());
                            int y = Integer.parseInt(s[2].trim());
                            robot.mouseMove(x, y);
                            System.out.println("ClientINFO:MOUSE move to "+x+","+y );
                        }
                    }else if("mouseMoved".equals(s[0].trim())){
                        if (s.length == 3) {
                            int x = Integer.parseInt(s[1].trim());
                            int y = Integer.parseInt(s[2].trim());
                            robot.mouseMove(x, y);
                            System.out.println("ClientINFO:MOUSE move to "+x+","+y);
                        }
                    }else if("keyPress".equals(s[0].trim())){
                        if(s.length==2){
                            int keycode=Integer.parseInt(s[1]);
                            robot.keyPress(keycode);
                        }
                    }else if("keyRelease".equals(s[0].trim())){
                        if(s.length==2){
                            int keycode=Integer.parseInt(s[1]);
                            robot.keyRelease(keycode);
                        }
                    }else if("keyTyped".equals(s[0].trim())){
                        if(s.length==2){
                            int keycode=Integer.parseInt(s[1]);
                            robot.keyPress(keycode);
                            robot.keyRelease(keycode);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("error1");
            }
        }
    }
}