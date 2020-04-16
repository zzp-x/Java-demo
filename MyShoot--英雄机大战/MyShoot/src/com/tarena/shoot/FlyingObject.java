package com.tarena.shoot;
import java.awt.image.BufferedImage;

//飞行物类
public abstract class FlyingObject {
	protected BufferedImage image; //图片
	protected int width;  		   //宽
	protected int height;		   //高
	protected int x;			   //坐标
	protected int y;
	
	// 飞行物走一步
	public abstract void step();
	
	//检查是否出界，，返回true表示已越界
	public abstract boolean outOfBounds();
	
	public boolean shootBy(Bullet b) {
		return false;
	};
}
