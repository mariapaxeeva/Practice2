package graphics;

import graphics.Controller.ControlPanel;
import graphics.InfoPanels.CreatureInfo;
import graphics.InfoPanels.Events;
import graphics.InfoPanels.LandscapeInfo;
import graphics.InfoPanels.Statistics;
import graphics.Map.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

    public static JPanel        mapPanelGroup;
    public static JPanel        infoPanelGroup;
    public static Map           map;
    public static ControlPanel  controlPanel;
    public static CreatureInfo  creatureInfo;
    public static Events        events;
    public static LandscapeInfo landscapeInfo;
    public static Statistics    statistics;

    public MainPanel(){
        view();
        panels();
        addMapPanel();
        addControlPanel();
        setVisible(true);
    }

    private void view() { setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); }

    private void panels(){
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
}
