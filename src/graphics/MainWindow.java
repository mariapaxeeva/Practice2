package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Класс определяет вид главного окна приложения
public class MainWindow extends JFrame {

        public MainWindow() {
            super("Практика Паксеева ПИ-21"); //Заголовок окна
            view();
            menuBar();
            panel();
            setVisible(true);
        }

        // Размер окна, его положение и иконка
        private void view(){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setIconImage(new ImageIcon("src\\resources\\icon.png").getImage());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // Создание меню и его добавление в окно
        private void menuBar() {
            JMenuBar menu = new JMenuBar();
            menu.setLayout(new FlowLayout(FlowLayout.LEFT));
            menu.setBackground(Color.LIGHT_GRAY);
            menu.setPreferredSize(new Dimension(0, 33));

            JMenuItem physicalMap = new JMenuItem("Физическая карта");
            physicalMap.setFont(new Font("serif", Font.ROMAN_BASELINE, 13));
            physicalMap.setBackground(new Color(0xE6E6E6));
            physicalMap.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = new JDialog();
                    dialog.setUndecorated(true);
                    JLabel label = new JLabel( new ImageIcon("src/resources/mapOfAltayRegion.png") );
                    dialog.add( label );
                    dialog.setLocation(120, 40);
                    dialog.pack();
                    dialog.setVisible(true);
                }
            });

            JMenuItem help = new JMenuItem("Справка");
            help.setFont(new Font("serif", Font.ROMAN_BASELINE, 13));
            help.setBackground(new Color(0xE6E6E6));
            help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "<html><h2>ПО “Популяция лосей в Алтайском крае”</h2><i> представляет из себя модель жизнедеятельности лосей, " +
                                    "предусматривающую удовлетворение их базовых<br>потребностей, взаимодействие с хищниками, стихийные бедствия" +
                                    "и вмешательство человека в природу.</i><br><br>" +
                                    "<p>&nbsp&nbsp&nbspНа центральной панели расположена карта, кликнув на любую ячейку которой, справа на информационной панели<br>" +
                                    "появятся сведения о данной территории и ее обитателях. Лоси на карте обозначены квадратами &#10065;<br>" +
                                    "(самки – розовые, самцы – темно-синие,), хищники – треугольниками &#9660; (самки – розовые, самцы – темно-синие),<br>" +
                                    "охотники – крестами &#10006;. Территории со съедобными растениями обозначены точками.</p><br>" +
                                    "<p>&nbsp&nbsp&nbspПеред запуском модели требуется установить бегунками на левой панели начальные параметры,<br>" +
                                    "затем переключить кнопку <off> на <on>. Теперь доступны кнопки стихийных бедствий на панели параметров<br>" +
                                    "и панель со статистикой. Наиболее важные события будут отображаться в истории событий на правой панели.</p><br>" +
                                    "<p>&nbsp&nbsp&nbspПеред началом бедствия требуется выделить ячейку карты, с которой начнется катастрофа. Пожар распространяется<br>" +
                                    "в случайном направлении. Чтобы прекратить его, нужно повторно нажать на кнопку “Устроить пожар”.</p><br>" +
                                    "<p>&nbsp&nbsp&nbspЧтобы вырубить лес, после выделения ячейки и нажатия на кнопку “Вырубить лес”, требуется выбрать радиус<br>" +
                                    "вырубки в появившемся окне и подтвердить действие.</p><br>" +
                                    "<p>&nbsp&nbsp&nbspДля просмотра физической карты Алтайского края можно нажать кнопку “Физическая карта” в строке меню</p>",
                            "Справка", JOptionPane.INFORMATION_MESSAGE,
                            new ImageIcon("src/resources/helpIcon.png"));
                }
            });

            menu.add(physicalMap);
            menu.add(help);
            setJMenuBar(menu);
        }

        // Добавление главной панели
        private void panel() { add(new MainPanel()); }
}
