package nstu.vehicles;

import javax.swing.*;
import java.io.Serializable;

public abstract class Vehicle implements Serializable {
	protected float x, y, speed;
	protected int id;
	protected long timeAppear;
	protected ImageIcon image;

	public float getSpeed() {
		return speed;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
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
