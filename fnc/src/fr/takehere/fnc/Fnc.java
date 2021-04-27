package fr.takehere.fnc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Fnc{
    static boolean isSelecting = true;
    static JWindow window = new JWindow();
    static JFrame frame = new JFrame();
    static JPanel panel = new JPanel();
    static Label instructions = new Label();
    static JLabel picLabel = new JLabel();
    static Point coord1;
    static Point coord2;
    public static void main(String[] args) throws AWTException, IOException {
        frame.setContentPane(panel);
        frame.setSize(500,300);
        frame.setResizable(false);
        frame.setTitle("Friday night cheating !");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(instructions);
        panel.add(picLabel);
        panel.setBackground(new Color(125, 3, 172, 255));

        window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        window.setBackground(new Color(255,255,255,10));
        window.setVisible(true);
        window.setAlwaysOnTop(true);
        window.addMouseListener(new MouseRecognition());

        instructions.setForeground(Color.BLACK);
        instructions.setBackground(panel.getBackground());
        instructions.setFont(new Font("Helvetica", Font.BOLD, 20));
        instructions.setAlignment(Label.CENTER);
        instructions.setText("Cliquez sur la première coordonnée !");
    }

    public static void cheat(){
        try {
            String title = frame.getTitle();
            Robot robot = new Robot();
            Rectangle zone = new Rectangle(coord1);
            zone.add(coord2);

            if (zone.getHeight() == 0 || zone.getWidth() == 0){
                coord1 = null;
                coord2 = null;
                window.setBackground(new Color(255,255,255,10));
                isSelecting = true;
                instructions.setText("Cliquez sur la première coordonnée !");
                return;
            }

            HashMap<Color, Integer> keyColor = new HashMap<>();
            keyColor.put(new Color(18, 250, 5), KeyEvent.VK_UP);
            keyColor.put(new Color(0,255,255), KeyEvent.VK_DOWN);
            keyColor.put(new Color(249,57,63), KeyEvent.VK_RIGHT);
            keyColor.put(new Color(194,75,157), KeyEvent.VK_LEFT);

            HashMap<Color, Integer> keyFade = new HashMap<>();
            keyFade.put(new Color(65,215,72), KeyEvent.VK_UP);
            keyFade.put(new Color(54,218,222), KeyEvent.VK_DOWN);
            keyFade.put(new Color(203,99,107), KeyEvent.VK_RIGHT);
            keyFade.put(new Color(170,110,161), KeyEvent.VK_LEFT);

            Thread thread = new Thread() {
                public void run() {
                    long timer = System.currentTimeMillis();
                    int fps = 0;
                    HashMap<Color, Integer> keyColorDuplicate = new HashMap<>();
                    HashMap<Color, Integer> keyFadeDuplicate = new HashMap<>();
                    keyColor.forEach((color, integer) -> keyColorDuplicate.put(color, integer));
                    keyFade.forEach((color, integer) -> keyFadeDuplicate.put(color, integer));

                    while (true){
                        BufferedImage capture = robot.createScreenCapture(zone);

                        Image image = capture;
                        image = image.getScaledInstance(300, 120, Image.SCALE_SMOOTH);
                        picLabel.setIcon(new ImageIcon(image));

                        for (int y = 0; y < capture.getHeight(); y++) {
                            for (int x = 0; x < capture.getWidth(); x++) {
                                Color color = new Color(capture.getRGB(x, y));

                                for (Map.Entry<Color, Integer> entry : keyColorDuplicate.entrySet()) {
                                    if (equalsColor(color, entry.getKey(), 3)){
                                        try {
                                            instructions.setText(KeyEvent.getKeyText(entry.getValue()));
                                            System.out.println(KeyEvent.getKeyText(entry.getValue()));

                                            robot.keyPress(entry.getValue());
                                            Thread.sleep(20);
                                            robot.keyRelease(entry.getValue());

                                            System.out.println("--------");
                                            keyColorDuplicate.remove(entry.getKey(), entry.getValue());
                                            break;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                Iterator it = keyFadeDuplicate.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry<Color, Integer> pair = (Map.Entry) it.next();
                                    if (equalsColor(color, pair.getKey(), 2)){
                                        try {
                                            robot.keyPress(pair.getValue());
                                            Thread.sleep(50);
                                            robot.keyRelease(pair.getValue());
                                            it.remove();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        keyColorDuplicate.clear();
                        keyFadeDuplicate.clear();
                        keyColor.forEach((color, integer) -> keyColorDuplicate.put(color, integer));
                        keyFade.forEach((color, integer) -> keyFadeDuplicate.put(color, integer));

                        fps++;
                        if (System.currentTimeMillis() - timer > 1000){
                            frame.setTitle(fps + " fps | " + title);
                            timer += 1000;
                            fps = 0;
                        }
                    }
                }
            };
            thread.start();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    public static boolean equalsColor(Color base, Color target, int threshold){
        if (between(base.getRed(), (target.getRed() - threshold), (target.getRed() + threshold))){
            if (between(base.getBlue(), (target.getBlue() - threshold), (target.getBlue() + threshold))){
                if (between(base.getGreen(), (target.getGreen() - threshold), (target.getGreen() + threshold))){
                    return true;
                }
            }
        }
        return false;
    }
}