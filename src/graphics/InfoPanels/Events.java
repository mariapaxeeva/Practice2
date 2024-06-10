package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Events extends JScrollPane {
    private JList<String> events = new JList<String>(new DefaultListModel<String>()); // Список событий

    public Events() {
        view();
        events();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "История событий", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY));
        setPreferredSize(new Dimension(0, 0));
        setFont(new Font("Arial Narrow", Font.ROMAN_BASELINE, 13));
    }

    // Настройка свойств списка событий и его отображение на панели
    private void events() {
        this.events.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Режим выделения - 1 объект
        this.events.setLayoutOrientation(JList.VERTICAL);
        this.events.setBackground(new Color(0xEEEEEE));
        setViewportView(this.events); // Отображение списка событий на панели
    }

    // Обновление отображаемой информации (формирование списка событий)
    // days - кол-во прошедших с момента начала симуляции дней,
    // event - строка, содержащая информацию о событии
    public void update(int days, String event) {
        ((DefaultListModel<String>)this.events.getModel()).addElement(formatEvent(days, event));
        int lastIndex = events.getModel().getSize() - 1;
        if (lastIndex >= 0) {
            events.ensureIndexIsVisible(lastIndex);
        }
    }

    public void reset() {
        ((DefaultListModel<String>)this.events.getModel()).clear();
    }

    // Форматирование вывода сведений о событии (кол-во прошедшего времени + действие)
    private String formatEvent(int days, String event) {
        return formatDateShort(days) + " " + event;
    }

    // Форматирование сокращенного вывода прошедшего времени
    private String formatDateShort(int days) {
        int months = days / 30;
        int years = months / 12;
        return String.format("%02d.%02d.%04d", days % 30, months % 12, years);
    }
}
