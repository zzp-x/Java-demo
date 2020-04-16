package com.tarena.shoot;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;

import java.util.Timer;
import java.util.TimerTask;

import java.util.Arrays;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Color;
import java.awt.Font;

//主程序类
public class ShootGame extends JPanel {
	public static final int WIDTH = 400;  //窗口宽
	public static final int HEIGHT = 654; //窗口高
	
	public static final int START = 0;    //启动状态
	public static final int RUNNING = 1;  //运行状态
	public static final int PAUSE = 2;    //停止状态
	public static final int GAME_OVER = 3;//结束状态
	private int state = START;  //当前状态
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	private Hero hero = new Hero();
	private FlyingObject[] flyings = {}; //敌人数组
	private Bullet[] bullets = {}; //子弹数组
	
	static { //初始化静态资源(图片)
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			start= ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 随机生成敌人对象
	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(2);
		if(type == 0) {
			return new Bee();
		}else {
			return new Airplane();
		}
	}
	
	
	// 敌人入场
	int flyEnteredIndex = 0;
	public void enterAction() {
		//生成敌人对象，将对象添加到flyings数组中
		flyEnteredIndex++;
		if(flyEnteredIndex%40==0) {
			FlyingObject one = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1);
			flyings[flyings.length-1] = one;
		}
	}
	// 飞行物走一步
	public void stepAction() {
		hero.step();
		for(int i=0; i<flyings.length; i++) {
			flyings[i].step();
		}
		for(int i=0; i<bullets.length; i++) {
			bullets[i].step();
		}
	}
	// 子弹入场---英雄机发射子弹
	int shootIndex = 0;
	public void shootAction() {
		shootIndex++;
		if(shootIndex%30 == 0) {
			Bullet[] bs = hero.shoot();
//			System.arraycopy(bs,0, bullets, bullets.length-bs.length, bs.length);
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
			for(int i=0; i<bs.length; i++) {
				bullets[bullets.length-bs.length+i] = bs[i];
			}
		}
	}
	
	// 删除越界的敌人,,敌机，小蜜蜂，子弹
	public void outOfBoundsAction() {
		// 删除越界的敌机，小蜜蜂
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for(int i=0; i<flyings.length; i++) {
			if(!flyings[i].outOfBounds()) {
				flyingLives[index] = flyings[i];
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);
		
		// 删除越界的子弹
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i=0; i<bullets.length; i++) {
			if(!bullets[i].outOfBounds()) {
				bulletLives[index] = bullets[i];
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}
	
	// 所有子弹与所有敌人(敌机+小蜜蜂)的碰撞
	public void bangAction() {
		for(int a=0; a<bullets.length; a++) {
			bang(a);
		}
	}
	// 一个子弹与所有敌人的碰撞
	int score = 0;
	public void bang(int a) {
		Bullet b = bullets[a];
		int bangPoint = -1;
		for(int i=0; i<flyings.length; i++) {
			if(flyings[i].shootBy(b)) {
				bangPoint = i;
				break;
			}
		}
		if(bangPoint != -1) {
			FlyingObject obj = flyings[bangPoint];
			if(obj instanceof Enemy) {
				Enemy one = (Enemy)obj;
				score += one.getScore();
			}
			if(obj instanceof Award) {
				Award two = (Award)obj;
				int type = two.getType();
				switch(type) {
				case Award.DOUBLE_FILE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
			FlyingObject l = flyings[flyings.length-1];  //最后一个敌人(敌机，小蜜蜂)
			flyings[bangPoint] = l;                      //放到被撞物体的位置上
			flyings = Arrays.copyOf(flyings, flyings.length-1); //直接缩容
			
			Bullet ll = bullets[bullets.length-1];  //最后一个子弹
			bullets[a] = ll;                        //放到被撞子弹的位置上
			bullets = Arrays.copyOf(bullets, bullets.length-1); //直接缩容
		}
	}
	
	// 检查游戏是否结束
	public void checkGameOverAction() {
		for(int i=0; i<flyings.length; i++) {
			if(hero.hit(flyings[i])) {
				//碰撞
				hero.reduceLife();
				hero.zeroDoubleFire();
				if(hero.getLife() <= 0) {
					state = GAME_OVER;
				}
				FlyingObject l = flyings[flyings.length-1];  //最后一个敌人(敌机，小蜜蜂)
				flyings[i] = l;                              //放到被撞物体的位置上
				flyings = Arrays.copyOf(flyings, flyings.length-1); //直接缩容
			}
		}
	}
	int num=0;
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			// 鼠标移动事件
			public void mouseMoved(MouseEvent e) {
				if(state == RUNNING) {
					hero.moveTo(e.getX(), e.getY());
				}
			}
			// 鼠标点击事件
			public void mouseClicked(MouseEvent e) {
				switch(state) { //根据不同的状态做不同的处理
				case START:  //启动状态时变为运行状态
					state = RUNNING;
					break;
				case GAME_OVER: //游戏结束状态时变为启动状态
					score = 0;  //清空数据
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			//鼠标移入事件
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE) {
					state = RUNNING;
				}
			}
			
			//鼠标移出事件
			public void mouseExited(MouseEvent e) {
				if(state == RUNNING) {
					state = PAUSE;
				}
			}
		};
		this.addMouseListener(l); //处理鼠标操作事件
		this.addMouseMotionListener(l); //处理鼠标滑动事件
		
		Timer timer = new Timer(); //创建定时器对象
		int interval = 10; //时间间隔 (以毫秒为单位)
		timer.schedule(new TimerTask() {
			public void run() {
				if(state == RUNNING) {
					enterAction();
					stepAction();
					shootAction();
					outOfBoundsAction(); //删除越界的敌人
					bangAction(); //子弹和敌人碰撞
				}
				checkGameOverAction();  //英雄机和敌人的碰撞
				repaint();
			}
		}, interval,interval);
	}

	
//	重写paint()  g:画笔
	public void paint(Graphics g) {
		g.drawImage(background,0, 0, null);
		paintHero(g);
		paintFlyingObjects(g);
		paintBullets(g);
		paintScoreandLife(g);
		paintState(g); //画状态
	}
	//画英雄机对象
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y,null);
	}
	//画敌人对象
	public void paintFlyingObjects(Graphics g) {
		for(int i=0; i<flyings.length; i++) {
			g.drawImage(flyings[i].image,flyings[i].x,flyings[i].y,null);			
		}
	}
	//画子弹对象
	public void paintBullets(Graphics g) {
		for(int i=0; i<bullets.length; i++) {
			g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);
		}
	}
	
	//画分和得命
	public void paintScoreandLife(Graphics g) {
		g.setColor(new Color(0xFF0000)); //设置画笔红色
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,24)); //设置字体样式
		g.drawString("SCORE: "+score, 10, 25);
		g.drawString("LIFE: "+hero.getLife(), 10, 45);
	}
	
	public void paintState(Graphics g) {
		switch(state) { //根据状态的不同来画不同的图片
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly"); //窗口
		ShootGame game = new ShootGame(); //面板
		frame.add(game); //将面板添加到窗口上
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); //1.设置窗口可见，2.尽快调用paint()方法
		
		game.action(); //启动程序的执行
	}
}
