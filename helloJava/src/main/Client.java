package main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Client{
    public static void main(String args[]) {

        ServerGUI sendOrder=new ServerGUI("192.168.0.101", "实时操控");//被监控电脑的ip地址
        WriteGUI catchScreen=new WriteGUI(sendOrder);
        catchScreen.changePort(30009);//现在可以修改获取主机屏幕信息要访问的端口号
        new Thread(catchScreen).start();//启动线程
    }
}

/**
 * @author LanXJ @doctime 2010-7-8
 * 访问指定端口的服务，从服务器端读取图像流，生成(刷新)管理面板
 * 默认访问的端口为DEFAULT_PORT=30011 端口，
 * 可以通过changePort来改变访问端口，也可以通过getPort查看当前访问端口
 * 实例化线程类时需要传入一个ServerGUI类型的辅助窗体对象
 */
class WriteGUI extends Thread {
    public static final int DEFAULT_PORT=30011;
    private int port;
    private ServerGUI rec;

    /**
     * @param rec 辅助窗体对象，可通过实例化获得
     */
    public WriteGUI(ServerGUI rec) {
        this.port=WriteGUI.DEFAULT_PORT;
        this.rec = rec;
    }
    public void changePort(int port){
        this.port=port;
    }
    public int getPort(){
        return this.port;
    }
    public void run() {
        while (rec.getBoo()) {
            System.out.println((System.currentTimeMillis()/1000)%24%60);
            Socket socket = null;
            try {
                socket = new Socket(rec.getIP(), this.port);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                ZipInputStream zis = new ZipInputStream(dis);
                Image image = null;
                try {
                    zis.getNextEntry();// 读取下一个 ZIP 文件条目并将流定位到该条目数据的开始处
                    image = ImageIO.read(zis);// 把ZIP流转换为图片
                    rec.jlabel.setIcon(new ImageIcon(image));
                    rec.scroll.setViewportView(rec.jlabel);
                    rec.validate();
                } catch (IOException ioe) {}
                try{
//					dis.close();
                    zis.close();
                }catch (Exception e) {}
                try {
                    TimeUnit.MILLISECONDS.sleep(50);// 接收图片间隔时间
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } catch (IOException ioe) {
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}

/**
 * @author LanXJ @doctime 2010-7-8
 * 访问指定主机的指定端口，向主机发送实例化线程类时传入的操控命令，实现对该主机的操控
 * 默认访问服务端口为DEFAULT_PORT=30012 端口，主机IP为实例化线程类时传入的IP
 * 可以通过changePort和changeIP来修改访问的端口和主机
 * 也可以通过setOperateStr来设置需要发送的操控命令
 * 需要注意的是，修改访问端口或主机必须在线程启动之前修改，否则修改无效
 */
class SendOperate extends Thread {
    public static int DEFAULT_PORT=30012;
    private String ip;
    private int port;// 30012
    private String operateStr;

    public SendOperate(String ip, String operateStr) {
        this.ip = ip;
        this.port = SendOperate.DEFAULT_PORT;
        this.operateStr = operateStr;
    }
    public void setOperateStr(String operateStr){
        this.operateStr=operateStr;
    }
    public void changePort(int port){
        this.port=port;
    }
    public boolean changeIP(String ip){
        if(UtilServer.checkIp(ip)){
            this.ip=ip;
            return true;
        }
        return false;
    }
    public int getPort(){
        return this.port;
    }
    public String getIP(){
        return this.ip;
    }
    public void run() {
        if(this.operateStr==null||this.operateStr.equals("")){
            return;
        }
//		if(this.operateStr.trim().startsWith("mouseMoved")){
//			return;
//		}
        try {
            Socket socket = new Socket(this.ip, this.port);
            OutputStream os = socket.getOutputStream();
            os.write((this.operateStr).getBytes());
            os.flush();
            socket.close();
            System.out.println("INFO: 【SendOperate】ip=" + this.ip + ",port=" + this.port + ",operateStr=" + this.operateStr + ".");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/**
 * @author LanXJ @doctime 2010-7-8
 * 服务工具类
 */
class UtilServer{
    public static boolean checkIp(String ip){
        if(ip==null)return false;
        String []dps=ip.split("\\.");
        if(dps.length!=4&&dps.length!=6)return false;
        boolean isIp=true;
        for (int i = 0; i < dps.length; i++) {
            try {
                int dp=Integer.parseInt(dps[i]);
                if(dp>255||dp< 0){
                    throw new RuntimeException("error IP");
                }
            } catch (Exception e) {
                isIp=false;
                break;
            }
        }
        return isIp;
    }
}
/**
 * @author LanXJ @doctime 2010-7-8
 * serverManage的辅助窗体，内部事件封装了sendOperate的实现
 */
class ServerGUI extends JFrame {
    private static final long serialVersionUID = 2273190419221320707L;
    JLabel jlabel;
    JScrollPane scroll;
    private String ip;
    private int port;
    private boolean boo;
    public boolean getBoo(){
        return this.boo;
    }
    public int getPort(){
        return this.port;
    }
    public void changePort(int port){
        this.port=port;
    }
    public String getIP(){
        return this.ip;
    }
    public boolean changeIP(String ip){
        if(UtilServer.checkIp(ip)){
            this.setTitle(this.getTitle().replace(this.ip, ip));
            this.ip=ip;
            return true;
        }
        return false;
    }

    protected ServerGUI(String IP, String sub) {
        this.boo = true;
        this.ip = IP;
        this.port=SendOperate.DEFAULT_PORT;
        this.setTitle("远程监控--IP:" + IP + "--主题:" + sub);
        this.jlabel = new JLabel();
        this.scroll = new JScrollPane();
        this.scroll.add(this.jlabel);
        scroll.addMouseListener(new MouseAdapter() {
			/*public void mouseClicked(MouseEvent e) {// getMousePosition()
				super.mouseClicked(e);
				//由于加上单击事件后，鼠标按下并快速抬起 就设计到按下、抬起、单击 三个事件，将单击变为了双击
				//所以不实现单击监听
				int x = (int) e.getX() + (int) ServerGUI.this.scroll.getHorizontalScrollBar().getValue();
				int y = (int) e.getY() + (int) ServerGUI.this.scroll.getVerticalScrollBar().getValue();
//				int type = e.getModifiers();//e.BUTTON1_MASK 或 e.BUTTON2_MASK 或 e.BUTTON3_MASK
				String operateStr ="mouseClicked,"+ x + "," + y + "," + e.getModifiers();

				SendOperate sender=new SendOperate(ServerGUI.this.ip, (operateStr));
				sender.changeIP(ServerGUI.this.ip);//同步ip
				sender.changePort(ServerGUI.this.port);//同步port
				sender.start();
			}*/

            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int x = (int) e.getX() + (int) ServerGUI.this.scroll.getHorizontalScrollBar().getValue();
                int y = (int) e.getY() + (int) ServerGUI.this.scroll.getVerticalScrollBar().getValue();
//				int type = e.getModifiers();//e.BUTTON1_MASK 或 e.BUTTON2_MASK 或 e.BUTTON3_MASK
                String operateStr ="mousePressed,"+ x + "," + y + "," + e.getModifiers();

                SendOperate sender=new SendOperate(ServerGUI.this.ip, (operateStr));
                sender.changeIP(ServerGUI.this.ip);//同步ip
                sender.changePort(ServerGUI.this.port);//同步port
                sender.start();
            }
            @SuppressWarnings("static-access")
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int x = (int) e.getX() + (int) ServerGUI.this.scroll.getHorizontalScrollBar().getValue();
                int y = (int) e.getY() + (int) ServerGUI.this.scroll.getVerticalScrollBar().getValue();
//				int type = e.getModifiers();//e.BUTTON1_MASK 或 e.BUTTON2_MASK 或 e.BUTTON3_MASK
                String operateStr ="mouseReleased,"+ x + "," + y + "," + e.getModifiers();

                SendOperate sender=new SendOperate(ServerGUI.this.ip, (operateStr));
                sender.changeIP(ServerGUI.this.ip);//同步ip
                sender.changePort(ServerGUI.this.port);//同步port
                sender.start();
            }
        });
        scroll.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = (int) e.getX() + (int) ServerGUI.this.scroll.getHorizontalScrollBar().getValue();
                int y = (int) e.getY() + (int) ServerGUI.this.scroll.getVerticalScrollBar().getValue();
                String operateStr ="mouseDragged,"+ x + "," + y + "," + e.getModifiers();

                SendOperate sender=new SendOperate(ServerGUI.this.ip, operateStr);
                sender.changeIP(ServerGUI.this.ip);//同步ip
                sender.changePort(ServerGUI.this.port);//同步port
                sender.start();
            }
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int x = (int) e.getX() + (int) ServerGUI.this.scroll.getHorizontalScrollBar().getValue();
                int y = (int) e.getY() + (int) ServerGUI.this.scroll.getVerticalScrollBar().getValue();
                String operateStr ="mouseMoved,"+ x + "," + y;

                SendOperate sender=new SendOperate(ServerGUI.this.ip, (operateStr));
                sender.changeIP(ServerGUI.this.ip);//同步ip
                sender.changePort(ServerGUI.this.port);//同步port
                sender.start();
            }
        });
        this.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                String operateStr ="keyPress,"+ e.getKeyCode();

                SendOperate sender=new SendOperate(ServerGUI.this.ip, (operateStr));
                sender.changeIP(ServerGUI.this.ip);//同步ip
                sender.changePort(ServerGUI.this.port);//同步port
                sender.start();
            }
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String operateStr ="keyReleas,"+ e.getKeyCode();

                SendOperate sender=new SendOperate(ServerGUI.this.ip, (operateStr));
                sender.changeIP(ServerGUI.this.ip);//同步ip
                sender.changePort(ServerGUI.this.port);//同步port
                sender.start();
            }
            public void keyTyped(KeyEvent e) {
//				super.keyTyped(e);
            }
        });

        //删除动作监控
        //scroll.addMouseListener(null);
        //scroll.addMouseMotionListener(null);
        //scroll.addKeyListener(null);


        this.add(scroll);

        this.setAlwaysOnTop(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(100, 75, (int) screenSize.getWidth() - 200, (int) screenSize.getHeight() - 150);
        // this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);// 关闭窗体不做任何事
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                boo = false;
                ServerGUI.this.dispose();
                System.out.println("窗体关闭");
                System.gc();
            }
        });
        this.setVisible(true);
        this.validate();

    }

}
