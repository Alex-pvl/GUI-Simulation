package nstu.vehicles;

import javax.swing.*;

public abstract class Vehicle {
	protected int x, y, id, speed;
	protected long timeAppear;
	protected ImageIcon image;

	public int getSpeed() {
		return speed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getTimeAppear() {
		return timeAppear;
	}

	public void setTimeAppear(long timeAppear) {
		this.timeAppear = timeAppear;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ImageIcon getImg() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
}
