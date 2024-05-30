import graphics.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow app = new MainWindow(); //Создаем экземпляр нашего приложения
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}