package graphics;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

// Класс определяет вид главного окна приложения
public class MainWindow extends JFrame {

        public MainWindow() {
            super("Practice Pakseeva PI-21"); //Заголовок окна
            view();
            panel();
            setVisible(true);
        }

        // Размер окна, его положение и иконка
        private void view(){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setIconImage(new ImageIcon("src\\resources\\icon.png").getImage());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // Добавление главной панели
        private void panel() { add(new MainPanel()); }
}
