package app;

import bank.accounts.Transaction;
import bank.atm.ManagerATMController;
import bank.customers.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;


public class ManagerDashBoard extends JFrame {
    private final ManagerATMController managerATMController;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                inimanagerDashBoard();
            }
        });
    }
    public static void inimanagerDashBoard(){
        JFrame ManagerDashBoard = new JFrame("managerDashBoard");
        ManagerDashBoard.setContentPane(new ManagerDashBoard().BackGround);
        ManagerDashBoard.setSize(100, 100);
        ManagerDashBoard.pack();
        ManagerDashBoard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ManagerDashBoard.setVisible(true);

        //ManagerDashBoard.dispose();
    }
    private Customer customer;
    private JButton customerInfoButton;
    private JButton transactionHistoryButton;
    private JLabel CustomerEmail;
    private JTextField Email;
    private JButton allcustomer;
    private JPanel BackGround;
    private JButton payInterestButton;
    private JButton chargeInterestButton;
    private JTextField payInterestRate;
    private JTextField chargeInterestRate;
    private JScrollPane showAllCustomer;
    private JTextArea CustomerTextarea;
    private JScrollPane showAllTransaction;
    private JLabel p_rate_per;
    private JLabel c_rate_per;
    private JButton back;
    private JTextArea TransactionTextarea;
    public ManagerDashBoard() {
        this.managerATMController = ManagerATMController.getInstance();

        CustomerTextarea = new JTextArea("show AllCustomer \n");
        CustomerTextarea.setEditable(true);
        showAllCustomer.setViewportView(CustomerTextarea);

        TransactionTextarea = new JTextArea("show Alltransaction \n");
        TransactionTextarea.setEditable(true);
        showAllTransaction.setViewportView(TransactionTextarea);

        BackGround.setBackground(new java.awt.Color(254, 254, 254));
        BackGround.setForeground(new java.awt.Color(254, 254, 254));

        CustomerEmail.setText("Enter Customer Email: ");

        customerInfoButton.setText("customer Information");
        customerInfoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerInfoButtonMouseClicked(evt);

            }
        });
        transactionHistoryButton.setText("transaction History");
        transactionHistoryButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transactionHistoryButtonMouseClicked(evt);

            }
        });

        allcustomer.setText("all customers");
        allcustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            //@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allcustomerMouseClicked(evt);
            }
        });
        showAllCustomer.setPreferredSize(new Dimension(600, 200));
        showAllCustomer.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        showAllTransaction.setPreferredSize(new Dimension(600, 200));
        showAllTransaction.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        p_rate_per.setText("Enter pay interest rate(%)");
        payInterestButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                payInterestButtonmouseClicked(e);
            }
        });

        c_rate_per.setText("Enter charge interest rate(%)");
        chargeInterestButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chargeInterestButtonmouseClicked(e);
            }
        });
        back.setText("LogOut to MainScreen");
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backmouseClicked(e);
            }
        });
    }

    private void backmouseClicked(MouseEvent e){
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
        dispose();
        managerATMController.logout();
    }

    private Customer getCustomer(String email){
        customer = managerATMController.getCustomer(email);
        return customer;
    }
    private void customerInfoButtonMouseClicked(java.awt.event.MouseEvent evt){
        String email = Email.getText();
        Customer customer = getCustomer(email);
        // TODO Here should have a Customer login without password.

        // TODO
        //  Instead you have to pop a popup window and show this customer information
        //  the reason is manager is trying to see this information of the customer
    }

    private List<Transaction> getTransaction(LocalDate todayDate){
        return this.managerATMController.getTransaction(todayDate);
    }

    private void transactionHistoryButtonMouseClicked(java.awt.event.MouseEvent evt){
        for (Transaction transaction : getTransaction(LocalDate.now())) {
            TransactionTextarea.append(transaction.toString()+"\n");
        }
        showAllTransaction.setViewportView(TransactionTextarea);
    }

    private List<Customer> getCustomers(){
        return managerATMController.getCustomers();
    }
    private void allcustomerMouseClicked(java.awt.event.MouseEvent evt){
        for (Customer customer : getCustomers()) {
            CustomerTextarea.append(
                    customer.getFirstName() + " " + customer.getLastName()
                    + " | " + customer.getEmail()
                    + " | " + customer.getPhoneNumber()
                    + "\n"
            );
        }
        showAllCustomer.setViewportView(CustomerTextarea);
    }

    private void payInterestButtonmouseClicked(MouseEvent e){
        if (payInterestRate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Pay Interest field can't be blank!\nAdd 0 for default Interest rate", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        float rate;
        try {
            rate = Float.parseFloat(payInterestRate.getText());
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(this, "Pay Interest rate should be number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.managerATMController.payInterest(rate);
    }

    private void chargeInterestButtonmouseClicked(MouseEvent e){
        if (chargeInterestRate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Charge Interest field can't be blank!\nAdd 0 for default Interest rate", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        float rate;
        try {
            rate = Float.parseFloat(chargeInterestRate.getText());
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(this, "Charge Interest rate should be number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        rate = rate*-1;
        this.managerATMController.chargeInterest(rate);
    }
}
