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
    private JLabel customerinfolabel;
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
        customerinfolabel.setText("customer Information");
        customerInfoButton.setText("Show Information");
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
        int stockId;

        try {
            stockId = Integer.parseInt(D_stock_id.getText());
        } catch (Exception ase) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Id", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (managerATMController.removeStock(stockId)) {
            JOptionPane.showMessageDialog(this, "Stock deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Couldn't find the stock with Id = " + stockId, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void A_stockbuttonmouseClicked(MouseEvent e){
        String stockName = A_stock_name.getText();
        double stockValue;

        try {
            stockValue = Double.parseDouble(A_stock_value.getText());
        } catch (Exception ase) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Value", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (stockName.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Stock Name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stockValue < 1) {
            JOptionPane.showMessageDialog(this, "Sock value should be greater than $1", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (managerATMController.addStock(stockName, stockValue)) {
            JOptionPane.showMessageDialog(this, "New Stock " + stockName + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Unable to add New Stock, Try again!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void U_stockbuttonmouseClicked(MouseEvent e){
        int stockId;
        double stockValue;

        try {
            stockId = Integer.parseInt(U_Stock_id.getText());
        } catch (Exception ase) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Id", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            stockValue = Double.parseDouble(U_Stock_value.getText());
        } catch (Exception ase) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Value", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stockValue < 1) {
            JOptionPane.showMessageDialog(this, "Sock value should be greater than $1", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (managerATMController.updateStock(stockId, stockValue)) {
            JOptionPane.showMessageDialog(this, "Stock with Id " + stockId + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Unable to update stock, Try again!", "Error", JOptionPane.ERROR_MESSAGE);
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
        try {
            Customer customer = getCustomer(email);
            String info = customer.getFirstName() + " " + customer.getLastName()
                    + " | " + customer.getEmail()
                    + " | " + customer.getPhoneNumber();
            customerinfolabel.setText(info);
            }
        catch(Exception e) {customerinfolabel.setText("Enter email plz");;}
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
