package at.technikum.reslience.util;

import javax.swing.*;
import java.awt.*;

public class WeatherFrame extends JFrame {

    public WeatherFrame() throws Exception{
       super("Weather Forecast Checker");

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

       JTextField jTextField = new JTextField(20);
       JButton jButton = new JButton("Load Weather");

        JPanel topPanel =new JPanel(new FlowLayout());
        JPanel bottomPanel =new JPanel();

        bottomPanel.add(new JLabel("dddd"));

        topPanel.add(jTextField);
        topPanel.add(jButton);

        setLayout(new GridLayout(2, 1));
        add(topPanel);
        add(bottomPanel);

        setSize(900, 600);
        setVisible(true);
    }
}
