package graphics;

import javax.swing.*;
import java.awt.*;

// Класс определяет вид главного окна приложения
public class MainWindow extends JFrame {

        public MainWindow() {
            super("Practice Pakseeva PI-21"); //Заголовок окна
            view();
            menuBar();
            panel();
            setVisible(true);
        }

        // Размер окна, его положение и иконка
        private void view(){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setIconImage(new ImageIcon("src\\resources\\icon.png").getImage());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private void menuBar() {
            JMenuBar menu = new JMenuBar();
            menu.setLayout(new FlowLayout(FlowLayout.LEFT));
            menu.setBackground(Color.LIGHT_GRAY);
            menu.setPreferredSize(new Dimension(0, 33));

            JMenuItem physicalMap = new JMenuItem("Физическая карта");
            physicalMap.setFont(new Font("serif", Font.ROMAN_BASELINE, 13));
            physicalMap.setBackground(new Color(0xE6E6E6));

            JMenuItem help = new JMenuItem("Справка");
            help.setFont(new Font("serif", Font.ROMAN_BASELINE, 13));
            help.setBackground(new Color(0xE6E6E6));

            menu.add(physicalMap);
            menu.add(help);
            setJMenuBar(menu);
        }

        // Добавление главной панели
        private void panel() { add(new MainPanel()); }
}
