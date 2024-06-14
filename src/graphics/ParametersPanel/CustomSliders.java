package graphics.ParametersPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Класс реализует внешний вид JSlider в рыжих тонах с тенью,
// изображениием лося в виде бегунка и метками с обозначением значений с шагом 10
public class CustomSliders extends BasicSliderUI {

        private static final int TRACK_HEIGHT = 6;
        private static final int TRACK_WIDTH = 6;
        private static final int TRACK_ARC = 5;
        private static final Dimension THUMB_SIZE = new Dimension(20, 20);
        private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();

        public CustomSliders(JSlider b) {
            super(b);
        }

        // Создание скругленного прямоугольника шкалы
        @Override
        protected void calculateTrackRect() {
            super.calculateTrackRect();
            if (isHorizontal()) {
                trackRect.y = trackRect.y + (trackRect.height - TRACK_HEIGHT) / 2;
                trackRect.height = TRACK_HEIGHT;
            } else {
                trackRect.x = trackRect.x + (trackRect.width - TRACK_WIDTH) / 2;
                trackRect.width = TRACK_WIDTH;
            }
            trackShape.setRoundRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height, TRACK_ARC, TRACK_ARC);
        }

        // Расчет положения бегунка
        @Override
        protected void calculateThumbLocation() {
            super.calculateThumbLocation();
            if (isHorizontal()) {
                thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
            } else {
                thumbRect.x = trackRect.x + (trackRect.width - thumbRect.width) / 2;
            }
        }

        @Override
        protected Dimension getThumbSize() {
            return THUMB_SIZE;
        }

        private boolean isHorizontal() {
            return slider.getOrientation() == JSlider.HORIZONTAL;
        }

        //  Отрисовка полосы шкалы
        @Override
        public void paintTrack(final Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Shape clip = g2.getClip();

            boolean horizontal = isHorizontal();
            boolean inverted = slider.getInverted();

            g2.setColor(new Color(110, 110,110));
            g2.fill(trackShape);

            g2.setColor(new Color(200, 200 ,200));
            g2.setClip(trackShape);
            trackShape.y += 1;
            g2.fill(trackShape);
            trackShape.y = trackRect.y;

            g2.setClip(clip);

            // Рисование шкалы до бегунка
            if (horizontal) {
                boolean ltr = slider.getComponentOrientation().isLeftToRight();
                if (ltr) inverted = !inverted;
                int thumbPos = thumbRect.x + thumbRect.width / 2;
                if (inverted) {
                    g2.clipRect(0, 0, thumbPos, slider.getHeight());
                } else {
                    g2.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
                }

            } else {
                int thumbPos = thumbRect.y + thumbRect.height / 2;
                if (inverted) {
                    g2.clipRect(0, 0, slider.getHeight(), thumbPos);
                } else {
                    g2.clipRect(0, thumbPos, slider.getWidth(), slider.getHeight() - thumbPos);
                }
            }
            g2.setColor(new Color(0xBA8416));
            g2.fill(trackShape);
            g2.setClip(clip);
        }

        // Загрузка изображения в качестве бегунка
        @Override
        public void paintThumb(final Graphics g) {
            String imagePath = "src/resources/icon.png";
            BufferedImage thumb = null;
            try {
                thumb = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.out.println("Что-то пошло не так! " + e); // если проблема с файлом картинки
            }
            g.drawImage(thumb, thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, null);
        }

        @Override
        public void paintFocus(final Graphics g) {}

        // Создание объекта кастомного стилизованного ползунка
        public static JSlider newCustomSlider(int min, int max, int value) {
            Font font = new Font("serif", Font.BOLD, 12);
            JSlider customSlider = new JSlider(min, max, value) {
                @Override
                public void updateUI() {
                    setUI(new CustomSliders(this));
                }
            };
            customSlider.setForeground(new Color(0xBA8416));
            customSlider.setFont(font);
            customSlider.setMajorTickSpacing(20);
            customSlider.setMinorTickSpacing(5);
            customSlider.setPaintLabels(true);
            customSlider.setPaintTicks(true);
            return customSlider;
        }
    }

