package graphics;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class MainWindow extends JFrame {

        public MainWindow() {
            super("Practice Pakseeva PI-21"); //Заголовок окна
            view();
            panel();
            setVisible(true);
        }

        private void view(){
            setBounds(100, 100, 680, 700); //размер и положение
            setIconImage(new ImageIcon("src\\resources\\icon.png").getImage());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private void panel() { add(new MainPanel()); }
}
