package nstu;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        JFrame myFrame = getFrame();
        myFrame.add(new MyComponent());

    }

    public static class MyComponent extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            try {
                URL url = new URL("https://w7.pngwing.com/pngs/838/363/png-transparent-speech-balloon-computer-icons-thinking-bubble-miscellaneous-photography-auto-part-thumbnail.png");
                Image moto = new ImageIcon(url).getImage();
                g2.drawImage(moto, 0, 0, null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

//            g2.drawString("Hello", 100, 100);
        }
    }

    public static JFrame getFrame() {
        JFrame jFrame = new JFrame("Test"){};
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width/2 - 400, dimension.height/2 - 400, 800, 800);
        return jFrame;
    }
}
