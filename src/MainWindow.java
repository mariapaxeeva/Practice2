import javax.swing.JFrame;

public class MainWindow extends JFrame { //Наследуя от JFrame мы получаем всю функциональность окна

        public MainWindow() {
            super("Practice Pakseeva PI-21"); //Заголовок окна
            setBounds(100, 100, 680, 700); //размер и положение
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.add(new Map());
            setVisible(true);
        }
}
