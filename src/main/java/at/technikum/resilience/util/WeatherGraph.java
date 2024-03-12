package at.technikum.resilience.util;

import at.technikum.resilience.model.WeatherForecast;
import lombok.val;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class WeatherGraph extends JPanel {

    public WeatherGraph(WeatherForecast weatherForecast, String name) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < weatherForecast.daily().time().size(); i++) {
            val time = weatherForecast.daily().time().get(i);

            LocalDate date = LocalDate.parse(time);
            val format = date.format(DateTimeFormatter.ofPattern("dd.MM"));

            dataset.addValue(weatherForecast.daily().temperatureTwoMetersMin().get(i), "Temperature 2m (min.)", format);
            dataset.addValue(weatherForecast.daily().temperatureTwoMetersMax().get(i), "Temperature 2m (max.)", format);
            dataset.addValue(weatherForecast.daily().apparentTemperatureMin().get(i), "Apparent Temperature (min.)", format);
            dataset.addValue(weatherForecast.daily().apparentTemperatureMax().get(i), "Apparent Temperature (max.)", format);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Temperature in " + name,
                "Time",
                "Temperature (°C)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 500));
        add(chartPanel);
    }
}