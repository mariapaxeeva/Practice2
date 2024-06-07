package graphics.ParametersPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ParametersPanel extends JPanel {

    public ParametersPanel() {
        view();

        setVisible(true);
    }

    private void view() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(new TitledBorder("Параметры модели"));
        setPreferredSize(new Dimension(200, 0));
    }

    public void update() {

    }

    public void reset() {

    }
}
