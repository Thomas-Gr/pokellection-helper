package card_reader.data_capture;

import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.util.jh.JHFlipFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelHelper {

  public static WebcamPanel.DefaultPainter getPainter(WebcamPanel panel) {
    return panel.new DefaultPainter() {
      final JHFlipFilter rotate = new JHFlipFilter(JHFlipFilter.FLIP_90CW);
      @Override
      public void paintImage(WebcamPanel owner, BufferedImage image, Graphics2D g2) {
        super.paintImage(owner, rotate.filter(image.getSubimage(0, 0, image.getWidth() - 450, image.getHeight()), null), g2);
      }
    };
  }
}