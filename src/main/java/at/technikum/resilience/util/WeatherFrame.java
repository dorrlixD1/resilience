package at.technikum.resilience.util;

import at.technikum.resilience.services.WeatherService;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@ConditionalOnProperty(value = "weather.ui.enabled", havingValue = "true")
public class WeatherFrame extends JFrame {

    public WeatherFrame(WeatherService weatherService) throws Exception {
        super("Weather Forecast Checker");

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel();

        JLabel label = new JLabel("Load a place to display the data here");

        JTextField jTextField = new JTextField(20);
        JButton jButton = new JButton("Load Weather");
        jButton.addActionListener(l -> {
            String place = jTextField.getText().trim();
            if (place.isEmpty()) return;

            val forecast = weatherService.getWeatherForecastByCountry(place);

            bottomPanel.removeAll();
            bottomPanel.add(new WeatherGraph(forecast, place.toUpperCase()));
            bottomPanel.updateUI();
        });

        topPanel.add(jTextField);
        topPanel.add(jButton);

        bottomPanel.add(label);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);
        add(bottomPanel, BorderLayout.CENTER);

        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
