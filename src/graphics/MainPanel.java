package graphics;

import graphics.Controller.ControlPanel;
import graphics.Map.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.*;

public class MainPanel extends JPanel {

    public static JPanel mapPanelGroup;
    public static JPanel infoPanelGroup;
    public static JPanel emptyPanel;
    public static Map map;
    public static ControlPanel controlPanel;

    public MainPanel(){
        view();
        panels();
        addMapPanel();
        addControlPanel();
        setVisible(true);
    }

    private void view() { setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); }

    private void panels(){
        //emptyPanel = new JPanel();
        //add(emptyPanel);

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
}
