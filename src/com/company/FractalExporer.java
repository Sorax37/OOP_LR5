package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class FractalExporer {
    private  int ScreenSize;
    private JImageDisplay ImageDisplay;
    private FractalGenerator FracGen;
    private Rectangle2D.Double Range;

    public FractalExporer(int size)
    {
        ScreenSize = size;
        FracGen = new Mandelbrot();
        Range = new Rectangle2D.Double();
        FracGen.getInitialRange(Range);
        ImageDisplay = new JImageDisplay(ScreenSize, ScreenSize);
    }

    public void createAndShowGUI()
    {
        JFrame Frame = new JFrame("Fractal Explorer");
        ImageDisplay.setLayout(new BorderLayout());
        Frame.add(ImageDisplay, BorderLayout.CENTER);

        JPanel PanelButtons = new JPanel();
        JButton ResetButton = new JButton("Reset Button");
        JButton SaveButton = new JButton("Save Button");
        PanelButtons.add(SaveButton);
        PanelButtons.add(ResetButton);
        Frame.add(PanelButtons, BorderLayout.SOUTH);
        ClickEvent ResEv = new ClickEvent();
        ResetButton.addActionListener(ResEv);
        ClickEvent SaveEv = new ClickEvent();
        SaveButton.addActionListener(SaveEv);

        JComboBox ComBox = new JComboBox();

        JPanel PanelCB = new JPanel();
        JLabel Label = new JLabel("Fractal");
        PanelCB.add(Label);
        PanelCB.add(ComBox);
        Frame.add(PanelCB, BorderLayout.NORTH);

        FractalGenerator FractalMandelbrot = new Mandelbrot();
        ComBox.addItem(FractalMandelbrot);
        FractalGenerator FractalTricorn = new Tricorn();
        ComBox.addItem(FractalTricorn);
        FractalGenerator FractalBurningShip = new BurningShip();
        ComBox.addItem(FractalBurningShip);

        ClickEvent ChooseEv = new ClickEvent();
        ComBox.addActionListener(ChooseEv);

        ClickZoom Click = new ClickZoom();
        ImageDisplay.addMouseListener(Click);

        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame.pack ();
        Frame.setVisible (true);
        Frame.setResizable (false);
    }

    public void drawFractal()
    {
        for(int x = 0; x < ScreenSize; x++)
        {
            for (int y = 0; y < ScreenSize; y++)
            {
                int CountIterX = 0;
                int CountIterY = 0;
                double xCoord = FractalGenerator.getCoord (Range.x, Range.x + Range.width, ScreenSize, x);
                double yCoord = FractalGenerator.getCoord (Range.y, Range.y + Range.height, ScreenSize, y);
                int Iteration = FracGen.numIterations(xCoord, yCoord);
                if (Iteration == -1)
                    ImageDisplay.drawPixel(x, y, 0);
                else
                {
                    float hue = 0.1f + (float) Iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    ImageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        ImageDisplay.repaint();
    }

    private class ClickEvent implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String Command = e.getActionCommand();
            if (e.getSource() instanceof JComboBox)
            {
                JComboBox Source = (JComboBox) e.getSource();
                FracGen = (FractalGenerator) Source.getSelectedItem();
                FracGen.getInitialRange(Range);
                drawFractal();
            }
            else if (Command.equals("Reset Button"))
            {
                FracGen.getInitialRange(Range);
                drawFractal();
            }
            else if (Command.equals("Save Button"))
            {
                JFileChooser Chooser = new JFileChooser();
                FileNameExtensionFilter Filter = new FileNameExtensionFilter("PNG Images", "png");
                Chooser.setFileFilter(Filter);
                Chooser.setAcceptAllFileFilterUsed(false);
                int UserChoice = Chooser.showSaveDialog(ImageDisplay);
                if (UserChoice == Chooser.APPROVE_OPTION)
                {
                    File SelectedFile = Chooser.getSelectedFile();
                    try
                    {
                        BufferedImage BufIm = ImageDisplay.getBufImage();
                        ImageIO.write(BufIm, "png", SelectedFile);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ImageDisplay, ex.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }

    private class ClickZoom extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            double xCoord = FracGen.getCoord(Range.x, Range.x + Range.width, ScreenSize, x);
            double yCoord = FracGen.getCoord(Range.y, Range.y + Range.height, ScreenSize, y);
            FracGen.recenterAndZoomRange(Range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
}
