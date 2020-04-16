package com.tarena.shoot;
import java.awt.image.BufferedImage;

//英雄机
public class Hero extends FlyingObject {
	private int life;   //命
	private int doubleFire=0;   //火力值
	private BufferedImage[] images; //两张图片切换
	private int index; //协助图片切换
	
	public Hero() {
		image = ShootGame.hero0;
		width = image.getWidth();  //获取图片的宽
		height = image.getHeight(); //获取图片的高
		x = 150;  //x:初始值
		y = 400;  //y:初始值
		life = 3; //默认3条命
		images = new BufferedImage[] {ShootGame.hero0,ShootGame.hero1};
		// 初始化images，两张图片
		index = 0; //协助切换
	}
	
	// 重写
	public void step() {
		index++;
		image = images[index/10%images.length];  //每100个毫秒切换一次
	}
	// 英雄机发射子弹
	public Bullet[] shoot() {
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire > 0) {
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire -=2; //发射一次双倍火力减2 
			return bs;
		}else {
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}
		
	}
	// 英雄机随着鼠标移动
	public void moveTo(int x, int y) {
		this.x = x - this.width/2;  //使得英雄机中心对准在鼠标
		this.y = y - this.height/2;
	}
	
	// 重写是否越界函数
	public boolean outOfBounds() {
		return false;  //永不越界
	}
	
	// 加命
	public void addLife() {
		life++;
	}
	// 返回命
	public int getLife() {
		return life;
	}
	
	public void reduceLife() {
		life--;
	}
	
	// 加火力
	public void addDoubleFire() {
		doubleFire += 40;
	}
	public void zeroDoubleFire() {
		doubleFire = 0;
	}
	
	// 英雄机撞敌人,,true表示碰撞
	public boolean hit(FlyingObject obj) {
		int x1 = this.x - this.width/2;
		int x2 = this.x + this.width/2;
		int y1 = this.y - this.height/2;
		int y2 = this.y + this.height/2;
		if(obj.x<x2 && obj.x>x1 && obj.y<y2 && obj.y>y1) {
			return true;
		}else {
			return false;
		}
		
	}
}
