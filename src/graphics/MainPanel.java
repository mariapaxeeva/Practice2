package graphics;

import graphics.Controller.ControlPanel;
import graphics.InfoPanels.CreatureInfo;
import graphics.InfoPanels.Events;
import graphics.InfoPanels.LandscapeInfo;
import graphics.InfoPanels.Statistics;
import graphics.Map.Map;
import graphics.ParametersPanel.ParametersPanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

// Класс реализует главную панель главного окна
public class MainPanel extends JPanel {

    public static JPanel          parametersPanelGroup;    // группа панелей для взаимодействия пользователя с моделью
    public static JPanel          mapPanelGroup;           // группа панелей, связанных с картой
    public static JPanel          infoPanelGroup;          // группа информационных панелей
    public static Map             map;                     // панель карты
    public static ControlPanel    controlPanel;            // панель с кнопками запуска/приостановки/прекращения симуляции
    public static CreatureInfo    creatureInfo;            // панель с информацией о живом существе на данной территории
    public static Events          events;                  // панель, отображающая историю случившихся событий
    public static LandscapeInfo   landscapeInfo;           // панель с информацией о ландшафте данной территории
    public static Statistics      statistics;              // статистическая информационная панель
    public static ParametersPanel parameters;              // панель для управления моделью

    public MainPanel(){
        view();
        panels();
        addParametersPanel();
        addMapPanel();
        addControlPanel();
        addStatisticsPanel();
        addLandscapeInfoPanel();
        addCreatureInfoPanel();
        addEventsPanel();
        setVisible(true);
    }

    private void view() { setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); }

    // Метод добавляет группы панелей на главную панель
    private void panels(){
        parametersPanelGroup = new JPanel();
        parametersPanelGroup.setLayout(new BoxLayout(parametersPanelGroup, BoxLayout.Y_AXIS));
        add(parametersPanelGroup);

        mapPanelGroup = new JPanel();
        mapPanelGroup.setLayout(new BoxLayout(mapPanelGroup, BoxLayout.Y_AXIS));
        add(mapPanelGroup);

        infoPanelGroup = new JPanel();
        infoPanelGroup.setLayout(new BoxLayout(infoPanelGroup, BoxLayout.Y_AXIS));
        add(infoPanelGroup);
    }

    private void addMapPanel() {
        map = new Map();
        mapPanelGroup.add(map);
    }

    private void addControlPanel() {
        controlPanel = new ControlPanel();
        mapPanelGroup.add(controlPanel);
    }

    private void addCreatureInfoPanel() {
        creatureInfo = new CreatureInfo();
        infoPanelGroup.add(creatureInfo);
    }

    private void addEventsPanel() {
        events = new Events();
        infoPanelGroup.add(events);
    }

    private void addLandscapeInfoPanel() {
        landscapeInfo = new LandscapeInfo();
        infoPanelGroup.add(landscapeInfo);
    }

    private void addStatisticsPanel() {
        statistics = new Statistics();
        infoPanelGroup.add(statistics);
    }

    private void addParametersPanel() {
        parameters = new ParametersPanel();
        parametersPanelGroup.add(parameters);
    }
}
