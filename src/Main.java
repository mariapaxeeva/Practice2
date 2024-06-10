import graphics.MainWindow;

// Запуск приложения
public class Main {
    public static void main(String[] args) {
        try {
            MainWindow app = new MainWindow(); // Создание экземпляра приложения
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}