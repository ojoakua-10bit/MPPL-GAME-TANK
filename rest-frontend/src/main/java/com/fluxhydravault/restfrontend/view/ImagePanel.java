package com.fluxhydravault.restfrontend.view;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;
    private int imageSizeX;
    private int imageSizeY;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getImageSizeX() {
        return imageSizeX;
    }

    public void setImageSizeX(int imageSizeX) {
        this.imageSizeX = imageSizeX;
    }

    public int getImageSizeY() {
        return imageSizeY;
    }

    public void setImageSizeY(int imageSizeY) {
        this.imageSizeY = imageSizeY;
    }

    public void setImageSize(int x, int y) {
        imageSizeX = x;
        imageSizeY = y;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (image == null) {
            setBackground(Color.BLACK);
        }
        else {
            setBackground(Color.BLACK);
            graphics.drawImage(image, 0, 0, imageSizeX, imageSizeY,this);
        }
    }
}
