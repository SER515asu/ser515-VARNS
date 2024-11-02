package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * This is an attempt to use the JFree library to implement a dynamically altering burndown chart.
 * Credits to the library belong to https://github.com/jfree/jfreechart
 * Tutorials used to learn how to utilize it effectively
 * https://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm
 * https://stackoverflow.com/questions/7328326/dynamically-change-y-axis-range-in-jfreechart
 */
public class BurndownChart extends JPanel {

    private DefaultCategoryDataset defaultDataset;
    private JFreeChart lineChart;

    public BurndownChart() {
        defaultDataset = new DefaultCategoryDataset();
        lineChart = ChartFactory.createLineChart("Burndown Chart", "Time", "Velocity", defaultDataset, PlotOrientation.VERTICAL, false, false, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800,600));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);

    }

    public DefaultCategoryDataset getDefaultDataset() {
        return defaultDataset;
    }

    public void setBurndown(Integer day, Double points) {
        System.out.println(points);
        defaultDataset.addValue(points, "velocity", day.toString());


    }

    public void updateChart(){
        lineChart.fireChartChanged();
    }


}
