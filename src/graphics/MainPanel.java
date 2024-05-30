package graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

    public static JPanel mapPanelGroup;
    public static JPanel infoPanelGroup;
    public static Map map;

    public MainPanel(){
        view();
        panels();
        mapPanel();
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

    private void mapPanel() {
        map = new Map();
        mapPanelGroup.add(map);
    }
}
