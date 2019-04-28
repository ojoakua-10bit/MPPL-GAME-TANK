package com.fluxhydravault.restfrontend.view;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (image == null) {
            setBackground(Color.BLACK);
        }
        else {
            setBackground(Color.BLACK);
            graphics.drawImage(image, 0, 0, 128, 128,this);
        }
    }
}
