package com.company;
import java.awt.*;
import java.awt.image.*;

public class JImageDisplay extends javax.swing.JComponent{
    private BufferedImage BufImage;

    public JImageDisplay(int width, int height)
    {
        BufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension ImageDimension = new Dimension(width, height);
        super.setPreferredSize(ImageDimension);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage (BufImage, 0, 0, BufImage.getWidth(), BufImage.getHeight(), null);
    }

    public void clearImage()
    {
        int[] PixelArray = new int[getWidth() * getHeight()];
        BufImage.setRGB(0, 0, getWidth(), getHeight(), PixelArray, 0,0);
    }

    public void drawPixel(int x, int y, int RGBColor)
    {
        BufImage.setRGB(x, y, RGBColor);
    }

    public BufferedImage getBufImage() {
        return BufImage;
    }
}
