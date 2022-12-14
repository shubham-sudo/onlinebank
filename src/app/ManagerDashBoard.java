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
                new ManagerDashBoard().setVisible(true);
            }
        });
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
    private JTextField D_stock_id;
    private JButton D_stockbutton;
    private JTextField A_stock_name;
    private JTextField A_stock_value;
    private JButton A_stockbutton;
    private JTextField U_Stock_value;
    private JTextField U_Stock_id;
    private JButton U_stockbuton;
    private JTextArea TransactionTextarea;
    public ManagerDashBoard() {
        this.managerATMController = ManagerATMController.getInstance();
        initComponents();
    }

    private void initComponents() {
        setContentPane(BackGround);
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

        D_stockbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                D_stockbuttonmouseClicked(e);
            }
        });
        A_stockbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                A_stockbuttonmouseClicked(e);
            }
        });
        U_stockbuton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                U_stockbuttonmouseClicked(e);
            }
        });
        setSize(100, 100);
        pack();
    }
    private void D_stockbuttonmouseClicked(MouseEvent e){
        int stock_id = Integer.parseInt(D_stock_id.getText());
        managerATMController.removeStock(stock_id);
    }
    private void A_stockbuttonmouseClicked(MouseEvent e){
        String stock_name = A_stock_name.getText();
        double stock_value = Double.parseDouble(A_stock_value.getText());
        managerATMController.addStock(stock_name,stock_value);
    }
    private void U_stockbuttonmouseClicked(MouseEvent e){
        int stock_id = Integer.parseInt(U_Stock_id.getText());
        double stock_value = Double.parseDouble(U_Stock_value.getText());
        managerATMController.updateStock(stock_id, stock_value);
    }

    private void backmouseClicked(MouseEvent e){
        managerATMController.logout();
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
        dispose();
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
        CustomerTextarea = new JTextArea("show AllCustomer \n");
        CustomerTextarea.setEditable(true);

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
