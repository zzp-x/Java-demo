package com.tarena.shoot;

import java.util.Random;

public class Bullet extends FlyingObject{
	private int speed = 3;
	
	public Bullet(int x,int y) {
		image = ShootGame.bullet;
		width = image.getWidth();  //获取图片的宽
		height = image.getHeight(); //获取图片的高
		Random rand = new Random(); //随机数对象
		this.x = x;  //x跟随英雄机
		this.y = y;  //y跟随英雄机
	}
	
	// 重写
	public void step() {
		y-=speed;
	}
	
	// 重写是否越界函数
	public boolean outOfBounds() {
		if(y<-this.height) {
			return true;
		}else {
			return false;
		}
	}
}
