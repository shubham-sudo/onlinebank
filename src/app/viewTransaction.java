package app;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class viewTransaction extends JFrame {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new viewTransaction().setVisible(true);
            }
        });
    }

    public viewTransaction(){
        iniView();

    }

    private JButton backButton;
    private JPanel BackGround;
    private JScrollPane accounts;
    private JLabel title;

    private void iniView(){
        setContentPane(BackGround);
        setTitle("All transaction today");
        //pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setAlwaysOnTop(true);

        backButton = new JButton();
        accounts = new JScrollPane();
        title = new JLabel();

        title.setText("All transaction today:");

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backButtonmouseClicked(e);
            }
        });
    }

    private void backButtonmouseClicked(MouseEvent e){
        ManagerDashBoard managerdashboard = new ManagerDashBoard();
        managerdashboard.setVisible(true);
        dispose();
    }

}
