import javax.swing.JFrame;

public class MainWindow extends JFrame { //Наследуя от JFrame мы получаем всю функциональность окна

        public MainWindow() {
            super("Practice Pakseeva PI-21"); //Заголовок окна
            setBounds(100, 100, 400, 400); //Если не выставить
            //размер и положение
            //то окно будет мелкое и незаметное
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //это нужно для того чтобы при
            //закрытии окна закрывалась и программа,
            //иначе она останется висеть в процессах
        }
}
