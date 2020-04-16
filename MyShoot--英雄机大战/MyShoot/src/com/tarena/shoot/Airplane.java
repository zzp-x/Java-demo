package com.tarena.shoot;
import java.util.Random;

//敌机
public class Airplane extends FlyingObject implements Enemy{
	private int speed = 2;  //走的步数
	
	public Airplane() {
		image = ShootGame.airplane;
		width = image.getWidth();  //获取图片的宽
		height = image.getHeight(); //获取图片的高
		Random rand = new Random(); //随机数对象
		x = rand.nextInt(ShootGame.WIDTH-this.width+1);
		y = -this.height;
	}
	public int getScore() {
		return 5;  //打掉一个敌机得5分
	}
	
	// 重写
	public void step() {
		y+=speed;
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
