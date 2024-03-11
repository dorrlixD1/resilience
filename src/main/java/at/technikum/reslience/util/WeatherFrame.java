package at.technikum.reslience.util;

import at.technikum.reslience.services.WeatherService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Profile("!test")
public class WeatherFrame extends JFrame {

    private WeatherService weatherService;

    public WeatherFrame(WeatherService weatherService) throws Exception {
        super("Weather Forecast Checker");
        this.weatherService = weatherService;

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        JTextField jTextField = new JTextField(20);
        JButton jButton = new JButton("Load Weather");

        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(new JLabel("Weather Data"));

        topPanel.add(jTextField);
        topPanel.add(jButton);

        setLayout(new GridLayout(2, 1));
        add(topPanel);
        add(bottomPanel);

        setSize(900, 600);
        setVisible(true);
    }
}
