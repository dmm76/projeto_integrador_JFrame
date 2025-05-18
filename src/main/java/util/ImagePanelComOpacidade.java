package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagePanelComOpacidade extends JPanel {
    private String caminhoImagem;
    private float opacidade = 0.2f;
    private int largura = 300;
    private int altura = 300;

    public ImagePanelComOpacidade(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public void setOpacidade(float opacidade) {
        this.opacidade = opacidade;
    }

    public void setTamanhoImagem(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            BufferedImage imagem = ImageIO.read(new File(caminhoImagem));
            Image redimensionada = imagem.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidade));
            int x = (getWidth() - largura) / 2;
            int y = (getHeight() - altura) / 2;
            g2d.drawImage(redimensionada, x, y, this);
            g2d.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

