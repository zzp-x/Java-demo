package com.tarena.shoot;

import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1; //X坐标速度
	private int ySpeed = 2; //Y坐标速度
	private int awardType;  //奖励的类型 0/1
	
	public Bee() {
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width+1);
		y = -this.height;
		awardType = rand.nextInt(2); //0到1之间
		
		int xSymbol = rand.nextInt(2);
		if(xSymbol == 0) {
			xSpeed = -xSpeed;
		}
	}
	public int getType() {
		return awardType;   //返回奖励类型 0/1
	}
	
	// 重写
	public void step() {
		y+=ySpeed;
		x+=xSpeed;
		if(x>ShootGame.WIDTH-this.width || x<=0) {
			xSpeed = -xSpeed;
		}
	}
	
	// 重写是否越界函数
	public boolean outOfBounds() {
		if(y>ShootGame.HEIGHT) {
			return true;
		}else {
			return false;
		}
	}
	
	// 敌人被子弹射击
	public boolean shootBy(Bullet bullet) {
		int x1 = this.x;
		int x2 = this.x + this.width;
		int y1 = this.y;
		int y2 = this.y + this.height;
		// x在 x1 和 x2 之间，y在y1和y2之间，既是碰撞了
		if(bullet.x<x2 && bullet.x>x1 && bullet.y<y2 && bullet.y>y1) {
			return true;
		}else {
			return false;
		}
	}
	
} 
