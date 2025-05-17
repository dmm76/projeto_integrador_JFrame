package view;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image backgroundImage;
    private float opacidade;

    public BackgroundPanel(ImageIcon imageIcon) {
        this.backgroundImage = imageIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // pinta o fundo usando a cor setBackground()
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            int largura = backgroundImage.getWidth(this);
            int altura = backgroundImage.getHeight(this);
            int x = (getWidth() - largura) / 2;
            int y = (getHeight() - altura) / 2;
            g2d.drawImage(backgroundImage, x, y, this);
            g2d.dispose();
        }
    }
}
