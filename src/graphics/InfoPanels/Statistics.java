package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.MainPanel;
import logic.Protection;
import logic.StatisticsLogic;

// Класс реализует панель с общей информацией о процессе моделирования
public class Statistics extends JScrollPane {
    private JLabel text; // Строка с информацией

    public Statistics() {
        view();
        text();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "Статистика", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY));
        setPreferredSize(new Dimension(380, 260));
    }

    // Отображение информации на панели
    private void text() {
        this.text = new JLabel("-");
        text.setFont(new Font("Arial Narrow", Font.ROMAN_BASELINE, 13));
        setViewportView(this.text);
    }

    // Обновление отображаемой информации (date - количество прошедших дней)
    public void update(int days) {
        String info = String.format("<html>С начала симуляции прошло %s"
                    + "<br> Общая площадь территории: 167 996 км<sup>2</sup> (1911 ячеек)"
                    + "<br>"
                    + "<br> Популяция лосей: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Самцов: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Самок: %s особей"
                    + "<br> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp • Беременных: %s особей"
                    + "<br> Родилось %s особей"
                    + "<br> Умерло %s особей:"
                    + "<br> &nbsp&nbsp&nbsp • От старости: %s"
                    + "<br> &nbsp&nbsp&nbsp • От усталости: %s"
                    + "<br> &nbsp&nbsp&nbsp • От голода: %s"
                    + "<br> &nbsp&nbsp&nbsp • От нападения хищника: %s"
                    + "<br> &nbsp&nbsp&nbsp • От встречи с охотником: %s"
                    + "<br> &nbsp&nbsp&nbsp • В пожаре: %s"
                    + "<br> Средний возраст популяции: %s"
                    + "<br>"
                    + "<br> Численность охотников: %s"
                    + "<br> Популяция хищников: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Самцов: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Самок: %s особей"
                    + "<br> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp • Беременных: %s особей"
                    + "<br> Родилось %s особей"
                    + "<br> Умерло %s особей:"
                    + "<br> &nbsp&nbsp&nbsp • От старости: %s"
                    + "<br> &nbsp&nbsp&nbsp • От усталости: %s"
                    + "<br> &nbsp&nbsp&nbsp • От голода: %s"
                    + "<br> &nbsp&nbsp&nbsp • От встречи с охотником: %s"
                    + "<br> &nbsp&nbsp&nbsp • В пожаре: %s"
                    + "<br>"
                    + "<br> Мигрировали %s особей:"
                    + "<br> &nbsp&nbsp&nbsp • Лосей: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Хищников: %s особей"
                    + "<br> Популяция обитает на %d %% территории"
                    + "<br> Обеспеченность пищей: %d </html>", // количество пищи на особь (в среднем)
                    MainPanel.creatureInfo.formatDate(days),
                    StatisticsLogic.elk,
                    StatisticsLogic.elkMale,
                    StatisticsLogic.elkFemale,
                    StatisticsLogic.elkPregnant,
                    StatisticsLogic.babyElkWereBorn,
                    StatisticsLogic.elkDied,
                    StatisticsLogic.elkDiedByAge,
                    StatisticsLogic.elkDiedByEnergy,
                    StatisticsLogic.elkDiedByHunger,
                    StatisticsLogic.elkDiedByPredator,
                    StatisticsLogic.elkDiedByHunter,
                    StatisticsLogic.elkDiedByFire,
                    MainPanel.creatureInfo.formatDate(StatisticsLogic.getAverageElkAge()),

                    StatisticsLogic.hunter,
                    StatisticsLogic.predator,
                    StatisticsLogic.predatorMale,
                    StatisticsLogic.predatorFemale,
                    StatisticsLogic.predatorPregnant,
                    StatisticsLogic.babyPredatorWereBorn,
                    StatisticsLogic.predatorDied,
                    StatisticsLogic.predatorDiedByAge,
                    StatisticsLogic.predatorDiedByEnergy,
                    StatisticsLogic.predatorDiedByHunger,
                    StatisticsLogic.predatorDiedByHunter,
                    StatisticsLogic.predatorDiedByFire,

                    StatisticsLogic.elkMigrated + StatisticsLogic.predatorMigrated,
                    StatisticsLogic.elkMigrated,
                    StatisticsLogic.predatorMigrated,
                    StatisticsLogic.getElkPopulationDensity(),
                    Protection.getValueInRange(StatisticsLogic.getFoodSupply(), 0, 100));
        this.text.setText(info);
    }

    public void reset() {
        this.text.setText("-");
    }
}
