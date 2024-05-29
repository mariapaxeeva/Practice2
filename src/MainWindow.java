import graphics.Map;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class MainWindow extends JFrame { //Наследуя от JFrame мы получаем всю функциональность окна

        public MainWindow() {
            super("Practice Pakseeva PI-21"); //Заголовок окна
            setBounds(100, 100, 680, 700); //размер и положение
            setIconImage(new ImageIcon("src\\resources\\icon.png").getImage());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.add(new Map());
            setVisible(true);
        }
}
