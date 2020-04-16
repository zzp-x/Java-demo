进程之间的相互通信用的就是套接字

![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416221829435-1995597640.png)

![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416221915575-433015230.png)


```
Socket编程
	1.进程间通信
	2.API
		ServerSocket  服务器套接字
		Socket        套接字
	3.协议
		TCP   transfer control protocal 传输控制协议，面向连接，基于流
		UDP   user datagram protocal 用户数据报协议，无连接，无顺序，不安全
		IP    Internet protocal
		FTP   file transfer protocal
		SMTP  simple

UDP
	1.user datagram protcal，用户数据报协议
	2.DatagramSocket
		发送数据报包(Datagram Packet),也可以接收包。
		数据报套接字
```
###Java socket 聊天室 例子
server 服务器端，处理数据连接，并转发消息
```
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

 * 聊天室服务端
public class Server {

	 * 运行在服务端的ServerSocket主要负责：
	 * 1. 向系统申请服务端口客户端就是通过这个端口与之建立连接的
	 * 2. 监听申请的服务端口，当一个客户端通过该端口尝试建立连接时，ServerSocket会在
	 * 服务端创建一个Socket与客户端建立连接。

	private ServerSocket server;

	 * 保存所有客户端输出流的集合
	private Map<String,PrintWriter> allOut;
	
	 * 用来初始化服务端
	public Server() throws IOException{
		 * 初始化的同时申请服务端口 
		server = new ServerSocket(8088);		
		allOut = new HashMap<String,PrintWriter>();
	}

	 * 入口方法
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("服务端启动失败");
		}
	}

	 * 将给定的输出流添加到共享集合
	private synchronized void addOut(String nickName,PrintWriter out) {
		allOut.put(nickName, out);
	}
	 * 将给定的输出流从共享集合中删除
	private synchronized void removeOut(String nickName) {
		allOut.remove(nickName);
	}
	 * 将给定的消息发送给所有客户端
	private void sendMessage(String message) {
		Set<Map.Entry<String, PrintWriter>> entryset = allOut.entrySet();
		for(Map.Entry<String, PrintWriter> e: entryset) {
			e.getValue().println(message);
		}		
	}
	 * 将给定消息发送给特定上线用户
	private void sendOneMessage(String whoName,String message) {
		String keyName = message.substring(1,message.indexOf(":"));
		PrintWriter out = allOut.get(keyName);
		System.out.println("out is "+out+keyName);
		if(out != null) {
			out.println(whoName+"对你说："+message.substring(message.indexOf(":")));
		}else {
			System.out.println("发送失败");
		}
	}

	 * 服务端开始工作的方法
	public void start() {
		try {
			 * ServerSocket的accept方法，是一个堵塞方法，作用是监听服务端口，
			 * 直到一个客户端连接并创建一个Socket，使用该Socket即可与刚连接的客户端进行交互
			while(true) {
				System.out.println("等待客户端连接····");
				Socket socket = server.accept();
				System.out.println("一个客户端连接了");
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 * 该线程处理客户端的Socket
	class ClientHandler implements Runnable{
		private Socket socket;
		private String nickName;
		private String host;   * 客户端的地址信息
		
		public ClientHandler(Socket socket) {
			this.socket = socket;

			 * 通过Socket可以获取远端计算机的地址信息。
			InetAddress address = socket.getInetAddress();
			 * 获取ip地址
			host = address.getHostAddress();
		}
		
		public void run() {
			 * Socket提供的方法
			 * InputStream getInputStream()
			 * 该方法可以获取一个输入流，从该流读取的数据就是从远端计算机发送过来的
			PrintWriter pw = null;
			try {
				InputStream input = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(input,"UTF-8");
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
				pw = new PrintWriter(osw,true);
				
				 * br.readLine()在读取客户端发送过来的消息时，由于客户端断线，而其操作系统的不同，
				 * 这里读取的结果不同：
				 * 当windows的客户端断开时，br.readLine会抛出异常
				 * 当linux的客户端断开时，br.readLine会返回null

				nickName = br.readLine();
				System.out.println(host+" "+nickName+" 上线了");
				
				 * 将该客户端的输出流存入到共享集合中
				addOut(nickName,pw);
				System.out.println(allOut);
				System.out.println("等待接收消息");
				while(true) {
					String message = br.readLine();
					if(message == null) {
						break;
					}
					System.out.println(host+" "+nickName+": "+message);
					if(message.startsWith("@")) {
						sendOneMessage(nickName,message);
					}else {
						sendMessage(host+nickName+": "+message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
					removeOut(nickName);
					System.out.println(nickName+"断开连接");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```
Client 客户端，监听发送事件，启动一个线程专门处理接收数据
```
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

 * 聊天室客户端
public class Client {
	 * 套接字，java.net.Socket
	 * 封装了TCP协议，使用它就可以基于TCP协议进行网络通讯。
	 * Socket是运行在客户端的
	private Socket socket;
	
	 * 构造方法，用来初始化客户端
	public Client() throws Exception {
		 * 实例化Socket的时候需要传入两个参数：
		 * 1：服务器地址，通过IP地址可以找到服务器的计算机
		 * 2：服务器端口，通过端口可以找到服务器计算机上服务端应用程序
		 * 实例化Socket的过程就是连接的过程，若远程计算机没有响应会抛出异常。
		System.out.println("正在连接服务端····");
		socket = new Socket("localhost",8088);
		System.out.println("已与服务端建立连接");
	}

	 * 入口方法
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.start();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("客户端启动失败!");
		}
	}

	 * 启动客户端的方法
	public void start() {
		try {
			 * Socket提供的方法：
			 * OutputStream getOutputStream
			 * 获取一个字节输出流，通过该流写出的数据会被发送至远端计算机。

			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
			PrintWriter pw = new PrintWriter(osw,true);
			
			 * 启动读取服务端发送过来消息的线程
			ServerHandler handler = new ServerHandler();
			Thread t = new Thread(handler);
			t.start();

			 * 将字符串发送至服务端，，，--漏写发送nick昵称了，
			Scanner scan = new Scanner(System.in);
			while(true) {
				System.out.print("请输入昵称：");
				String nick = scan.nextLine();
				if(nick.length()==0) {
					System.out.println("输入有误，请重新输入!");
				}else {
					break;
				}
			}
			while(true) {
				System.out.print("说点什么吧：");
				String str = scan.nextLine();
				if("".equals(str)) {
					break;
				}
				pw.println(str);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();
				System.out.println("连接结束");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	 * 该线程用来读取服务端发送过来的消息，并输出到客户端控制台显示。
	class ServerHandler implements Runnable{
		public void run() {
			try {
				InputStream input = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(input,"UTF-8");
				BufferedReader br = new BufferedReader(isr);
				while(true) {
					String str = br.readLine();
					System.out.println("服务端回复："+str);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
```
<hr>
效果如下，有个bug没解决，就是Client输入昵称无效，服务端没有收到，而是把Client说的第一句话当成了nickName····
原来是输入的nick字符串没有发送到服务器······

![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416221959299-184384593.png)

可以 @nickName:word，与其他上线的人交流
![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416222029607-35216732.png)

<hr>
整个程序很简单，要十分注意一点，就是缓冲流要设置自动刷新写入，要不然客户端就收不到消息了


<hr><hr>

###多人聊天
用Swing和JFrame做的聊天窗口，例子
与上面的实现是完全不同的方式写的，但基本是一样的。

可以实现多人聊天，一对一聊天没写。

发送数据的方式都是经过包装的，发送文字时，首先发送一个字节的数据类型，再发送文字长度，再发送文字内容；

发送文件时，开始的部分是跟发送文字一样的，在发送文字内容时改为四个字节的文件名称长度，再写入文件名称，后面加上文件内容，取出数据时依据文件类型来特殊取出。

服务器server端，转发数据给所有上线的客户端
```
package day19;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
	 * Socket列表，用于存放所有上线的用户Socket
	private static List<Socket> sockets;
	
	 * 循环监听8888端口，一有连接就新建线程去处理，再继续监听端口
	public static void main(String[] args) {
		ServerSocket serverSocket;
		sockets = new ArrayList<Socket>();
		try {
			serverSocket = new ServerSocket(8888);
			System.out.println("服务器已启动，等待客户端连接");
			while(true) {
				Socket socket = serverSocket.accept();
				sockets.add(socket);
				
				offlineNotice(socket, "上线了");
				new MyThread(socket, sockets).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 * 对所有上线的客户端消息通知
	public static void offlineNotice(Socket socket, String message) {
		String descClient = "[" + socket.getRemoteSocketAddress() + "]";
		byte[] userByte = getUsersByte();
		for(Socket s : sockets) {
			Utils.write(descClient + message, s, Utils.STRINGTYPE);
			Utils.write(userByte, s, Utils.USERSTYPE);
		}
	}
	
	 * 将Sockets取出其中的IP地址和端口，转成字符串添加到List中，再序列化成byte[]返回
	public static byte[] getUsersByte() {
		byte[] userByte = null;
		try {
			List<String> users = new ArrayList<String>();
			for(Socket s : sockets) {
				users.add("[" + s.getRemoteSocketAddress() + "]");
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(users);
			
			oos.close();
			baos.close();
			userByte = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userByte;
	}
}

 * Server对每个客户端连接都启动一个线程负责处理，转发数据
class MyThread extends Thread{
	private Socket socket;
	private List<Socket> sockets;
	
	public MyThread(Socket socket, List<Socket> sockets) {
		this.socket = socket;
		this.sockets = sockets;
	}
	
	 * 循环处理转发数据
	@Override
	public void run() {
		String descClient = "[" + socket.getRemoteSocketAddress() + "]";
		try {
			System.out.println(descClient + "上线了");
			while(true) {
				System.out.println("上线主机列表：" + sockets);
				
				Map<Byte, byte[]> map = Utils.read(socket);
				Byte type = Utils.getMapKey(map);
				byte[] data = Utils.getMapValue(map);
				
				System.out.println(socket.getInetAddress() + ": 数据类型--" + type + "， 数据--" + new String(data));
				
				 * 群发消息，排除发出消息的客户端的群发
				for(Socket s : sockets) {
					if(s == socket) {
						continue;
					}
					if(type == Utils.STRINGTYPE) {
						Utils.write(descClient + ":" + new String(data), s, type);
					}else if(type == Utils.FILETYPE){
						Utils.write(descClient + "发送文件", s, Utils.STRINGTYPE);
						Utils.write(data, s, type);
					}
				}
				if("exit".getBytes().equals(data) || data == null) {
					break;
				}
			}
			 * 客户端正常下线通知
			sockets.remove(socket);
			socket.close();
			Server.offlineNotice(socket, "下线了");
			System.out.println(descClient + ":下线了");
		} catch (Exception e) {
			 * 客户端异常正常下线通知
			e.printStackTrace();
			sockets.remove(socket);
			Server.offlineNotice(socket, "异常下线了");
			System.out.println(descClient + "异常下线了");
		}
	}
}
```
ChatUI界面，整个界面布局，对按钮的事件处理，调用客户端处理连接和显示数据
```
package day19;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 7517634945840465934L;
	public JTextArea chatArea;
	private JButton sendNewsButton;
	private JButton sendFileButton;
	public JTextArea friendsArea;
	private JTextField newsField;
	private Client client = null;
	
	public static void main(String[] args) {
		ChatUI chatui = new ChatUI();
	}
	
	public ChatUI() {
		init();
	}
	
	 * 初始化聊天窗口
	public void init() {
		this.setTitle("聊天窗口");
		this.setBounds(270, 80, 800, 600);
		this.setLayout(null);
		
		Font font = new Font("宋体", Font.BOLD, 22);
		
		 * 聊天对话界面
		chatArea = new JTextArea();
		JScrollPane chatPane = new JScrollPane(chatArea);
		chatPane.setBounds(30, 20, 450, 400);
		chatArea.setBounds(30, 20, 430, 380);
		chatArea.setFont(font);
		this.add(chatPane);

		 * 填写发送消息的输入框
		newsField = new JTextField();
		newsField.setBounds(30, 460, 380, 40);
		newsField.setFont(font);
		this.add(newsField);
		
		 * 发送按钮
		sendNewsButton = new JButton("发送");
		sendNewsButton.setBounds(450, 460, 60, 40);
		sendNewsButton.addActionListener(this);
		this.add(sendNewsButton);
		
		 * 发送文件按钮
		sendFileButton = new JButton("发送文件");
		sendFileButton.setBounds(540, 460, 100, 40);
		sendFileButton.addActionListener(this);
		this.add(sendFileButton);
		
		 * 所有上线客户端列表
		friendsArea = new JTextArea();
		friendsArea.setFont(new Font("宋体", Font.BOLD, 18));
		JScrollPane friendsPane = new JScrollPane(friendsArea);
		friendsPane.setBounds(550, 20, 200, 400);
		this.add(friendsPane);
		
		 * 添加窗口事件处理程序，使用适配器，监听JFame退出事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("关闭窗口");
				System.exit(-1);
			}
		});
		
		this.setVisible(true);
		 * 实例化一个负责通信的 Client
		client = new Client(this);
	}
	
	 * 对两个按钮事件进行处理
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sendNewsButton) {  * 发送按钮
			String msg = newsField.getText().trim();
			if(msg.length() != 0) {
				boolean status = Utils.write(msg, client.getSocket(), Utils.STRINGTYPE);
				if(!status) {
					msg = "连接出错";
					 * 关闭socket
					client.close();
				}
				 * 设置聊天窗口的数据
				String old;
				if("\r\n".equals(chatArea.getText())) {
					 old = "";
				}else {
					old = chatArea.getText();
				}
				chatArea.setText(old + "我：" + msg + "\r\n");
				newsField.setText("");
				
			}
		}else if(e.getSource() == sendFileButton) { * 发送文件按钮
			 * 对于发送文件的处理，需要重构发送的数据格式，定义文件头
			try {
				FileDialog dialog = new FileDialog(this, "选择文件", FileDialog.LOAD);
				dialog.setVisible(true);
				String path = dialog.getDirectory() + dialog.getFile();
				System.out.println(path);
				Utils.write(path, new FileInputStream(path), client.getSocket());
				String old;
				if("\r\n".equals(chatArea.getText())) {
					 old = "";
				}else {
					old = chatArea.getText();
				}
				chatArea.setText(old + "我发送文件：" + path + "\r\n");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
```
Client，ChatUI界面调用Client中的方法来与server交互
```
package day19;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

 * 负责ChatUI后台与服务器Server进行通信
public class Client {
	private Socket socket = null;
	
	public Client(ChatUI chatui) {
		getSocket();
		System.out.println("等待读取数据");
		
		 * 启动一个守护线程，用于循环读取数据
		Thread t = new Thread() {
			public void run() {
			try {
				while(true) {
					Map<Byte, byte[]> map = Utils.read(socket, chatui);
					Byte type = Utils.getMapKey(map);
					byte[] buffer = Utils.getMapValue(map);
					 * 检测获得的数据是否是字符串类型
					if(type == Utils.STRINGTYPE) {
						 * 检测是否满足退出条件
						if("exit".equals(new String(buffer)) || buffer == null) {
							System.out.println("连接关闭");
							socket.close();
							chatui.chatArea.setText(chatui.chatArea.getText() + "连接关闭" + "\r\n");
							break;
						}
						 * 将字符串直接写入到聊天窗口中
						System.out.println("STRINGTYPE = " + new String(buffer));
						chatui.chatArea.setText(chatui.chatArea.getText() + new String(buffer) + "\r\n");
					}else if(type == Utils.USERSTYPE){  * 检测是否是用户列表类型
						 * 用户列表数据是经过序列化成byte的数据，所以要先转换为对象
						ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
						ObjectInputStream ois = new ObjectInputStream(bais);
						List<String> usersList =  (List<String>) ois.readObject();
						String d = "";
						for(String str : usersList) {
							d = d + str + "\r\n";
						}
						 * 读取所有的用户数据，构造好字符串，填入好友列表框中
						chatui.friendsArea.setText(d);
						ois.close();
						bais.close();
						
					}else if(type == Utils.FILETYPE) {  * 检查是否是文件类型
						 * 是文件类型，需要反重构，将文件名称和文件数据分别取出来
						 * 前四个字节是文件名称的长度，接下来是文件名称，最后就是文件数据了
						System.out.println("接收到文件了：" + new String(buffer));
						 * 文件名称长度
						byte[] lenByte = new byte[4];
						System.arraycopy(buffer, 0, lenByte, 0, 4);
						 * 将byte[]转成int类型
						int lenName = Utils.byte2Int(lenByte);
						System.out.println("lenName = " + lenName);
						
						 * 构造一个特定长度的byte[]，取出文件名称
						byte[] nameByte = new byte[lenName];
						System.arraycopy(buffer, 4, nameByte, 0, lenName);
						 * 构造文件数据长度的byte[]，取出文件数据
						byte[] bufferData = new byte[buffer.length-4-lenName];
						System.arraycopy(buffer, 4+lenName, bufferData, 0, bufferData.length);
						 * 将文件数据写入文件中
						FileOutputStream fos = new FileOutputStream("E://TestCase//day20//" + new String(nameByte));
						fos.write(bufferData);
						fos.close();
						 * 在聊天窗口中显示文件保存位置
						chatui.chatArea.setText(chatui.chatArea.getText() + "文件保存在E://TestCase//day20//" + new String(nameByte) + "\r\n");
						System.out.println("文件保存位置 E://TestCase//day20//" + new String(nameByte));
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
					try {
						socket.close();
						chatui.chatArea.setText(chatui.chatArea.getText() + "异常，连接已关闭" + "\r\n");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		};
		 * 设置为守护线程，并启动
		t.setDaemon(true);
		t.start();
	}
	
	 * 检测socket是否为null，是则创建，可以避免创建多个socket
	public Socket getSocket() {
		if(socket == null) {
			try {
				socket = new Socket("localhost", 8888);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("服务器没有开启");
				System.exit(-1);
			}
		}
		return socket;
	}
	
	 * 关闭socket方法
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```
Utils，这个是一个工具类，客户端和服务端都是用其内部的方法处理数据
```
package day19;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Utils {
	 * 三种数据类型，用于区分发送的数据
	 * 字符串标识
	public final static byte STRINGTYPE = 0;
	 * 所有上线用户信息标识
	public final static byte USERSTYPE = 1;
	 * 发送文件标识
	public final static byte FILETYPE = 2;
	
	 * int转成四字节的byte[]，便于写入到socket流中，也便于解码
	public static byte[] int2Byte(int number) {
		byte[] b = new byte[4];
		b[0] = (byte) (number >> 24);
		b[1] = (byte) (number >> 16);
		b[2] = (byte) (number >> 8);
		b[3] = (byte)number;
		return b;
	}
	
	 * byte转int，和上面的int2byte对应
	public static int byte2Int(byte[] b) {
		int i3 = (b[0] & 0xFF)<< 24;
		int i2 = (b[1] & 0xFF)<< 16;
		int i1 = (b[2] & 0xFF)<< 8;
		int i0 =  b[3] & 0xFF;
		return i3 | i2 | i1 | i0;
	}
	
	 * 重写read方法
	public static Map<Byte, byte[]> read(Socket socket) throws Exception {
		Map<Byte, byte[]> map = read(socket, null);
		return map;
	}
	
	 * 获取字典数据(只有一个Entry)中的key，也就是Byte
	public static Byte getMapKey(Map<Byte, byte[]> map) {
		Set<Entry<Byte, byte[]>> entrySet = map.entrySet();
		Iterator<Entry<Byte, byte[]>> it = entrySet.iterator();
		Entry<Byte, byte[]> entry = it.next();
		return entry.getKey();
	}
	
	 * 获取字典数据(只有一个Entry)中的value，也就是byte[]数组
	public static byte[] getMapValue(Map<Byte, byte[]> map) {
		Set<Entry<Byte, byte[]>> entrySet = map.entrySet();
		Iterator<Entry<Byte, byte[]>> it = entrySet.iterator();
		Entry<Byte, byte[]> entry = it.next();
		return entry.getValue();
	}
	
	 * read方法对封装的数据进行解编码，取出封装的数据，但对数据类型不予区分，只返回类型和数据的map
	 * 具体的区分数据类型在client中处理，对于文件类型需要特殊处理
	public static Map<Byte, byte[]> read(Socket socket, ChatUI chatui) throws Exception{
		Map<Byte, byte[]> map = new HashMap<Byte, byte[]>();
		InputStream in = socket.getInputStream();
		byte[] type = new byte[1];
		in.read(type);
		byte[] lengthByte = new byte[4];
		in.read(lengthByte);
		int length = Utils.byte2Int(lengthByte);

		byte[] buffer = new byte[length];
		in.read(buffer);
		
		map.put(type[0], buffer);
		return map;
	}
	
	 * 重写write方法，使得可以写入文件，但注意不能写入过大的文件，对数据内容进一步重构
	public static void write(String path, FileInputStream fis, Socket socket) {
		try {
			 * 把文件名称也写到数组中，首先是四个字节的文件名称长度，加上文件名称，再加内容
			String fileName = path.substring(path.lastIndexOf("\\")+1, path.length());
			 * 文件名称
			byte[] nameByte = fileName.getBytes();
			 * 文件名称长度
			byte[] lenByte = Utils.int2Byte(nameByte.length);
			 * 文件数据
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			 * 新建足够长的数组用于存放合并的数据
			byte[] newByte = new byte[4 + nameByte.length + buffer.length];
			 * 合并数组
			System.arraycopy(lenByte, 0, newByte, 0, lenByte.length);
			System.arraycopy(nameByte, 0, newByte, lenByte.length, nameByte.length);
			System.arraycopy(buffer, 0, newByte, lenByte.length + nameByte.length, buffer.length);
			
			write(newByte, socket, Utils.FILETYPE);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	 * 重写write方法，使得可以写入String
	public static boolean write(String data, Socket socket, byte type) {
		return write(data.getBytes(), socket, type);
	}
	
	 * 将byte类型数据写入流中， 对数据进行编码
	 * 首先是数据类型一个字节，第二个是数据内容长度四个字节，第三个是数据内容
	public static boolean write(byte[] data, Socket socket, byte type) {
		try {
			OutputStream out = socket.getOutputStream();
			 * 获得数据类型，数据长度，转成byte
			int length = data.length;
			byte[] typeByte = {(byte)type};
			byte[] lengthByte = Utils.int2Byte(length);
			 * 新建足够长的数组用于存放合成的数据
			byte[] end = new byte[1 + 4 + data.length];
			 * 合并数组
			System.arraycopy(typeByte, 0, end, 0, typeByte.length);
			System.arraycopy(lengthByte, 0, end, 1, lengthByte.length);
			System.arraycopy(data, 0, end, 5, data.length);
			
			out.write(end);
			System.out.println("发送完毕:" + new String(data));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
}
```
发送消息
![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416222109222-616704339.png)


可以获得上线的所有客户端，显示在面板上
![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416222134289-1963410641.png)


点击发送文件按钮可以发送文件
![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416222158760-740765678.png)

代码链接：github https://github.com/libai2017/Java-demo
