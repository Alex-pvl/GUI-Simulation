package nstu;

import nstu.vehicles.Car;
import nstu.vehicles.Motorbike;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Habitat {
    private final JFrame jFrame;
    private final int width;
    private final int height;
    private List<Car> cars;
    private List<Motorbike> bikes;

    public Habitat(int width, int height) {
        this.width = width;
        this.height = height;
        jFrame = getFrame();
        this.cars = new ArrayList<>();
        this.bikes = new ArrayList<>();

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addComponent() {
        jFrame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Image moto = new ImageIcon("JavaLabs/src/nstu/imgs/moto.png").getImage();
                Image car = new ImageIcon("JavaLabs/src/nstu/imgs/car.png").getImage();
                Image icon = new ImageIcon("JavaLabs/src/nstu/imgs/icon.png").getImage();
                jFrame.setIconImage(icon);
                g2.drawImage(moto, new Random().nextInt(this.getWidth()-moto.getWidth(null)),
                        new Random().nextInt(this.getHeight()-moto.getHeight(null)),
                        null
                );
                g2.drawImage(car, new Random().nextInt(this.getWidth()-car.getWidth(null)),
                        new Random().nextInt(this.getHeight()-car.getHeight(null)),
                        null
                );
            }
        });
    }


    private JFrame getFrame() {
        JFrame jFrame = new JFrame("Road"){};
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width/2 - width/2, dimension.height/2 - height/2, width, height);
        return jFrame;
    }
}
