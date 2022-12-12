package app;

import javax.swing.*;

import bank.account.Account;
import bank.customer.Customer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import app.viewTransaction;
import database.Database;

public class managerDashBoard extends JFrame {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new managerDashBoard().setVisible(true);
            }
        });
    }
    public managerDashBoard(){
        /*
        String dateOfBirth="03/10/2000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dob = dateOfBirth.trim();
        Customer customer=new Customer(-1,"Example","Example", LocalDate.parse(dob, formatter),"example@bu.edu",false);
        customer.setPassword("example");
        user=customer;
        */

        iniDashBoard();

    }
    private Account act;
    private Customer cus;
    private JButton customerInfoButton;
    private JButton transactionHistoryButton;
    private JLabel CustomerEmail;
    private JTextField Email;
    private JButton allcustomer;
    private JPanel BackGround;
    private JButton payInterestButton;
    private JButton chargeInterestButton;
    private JTextField pay_interest_rate;
    private JTextField charge_interest_rate;

    //@SuppressWarnings("unchecked")
    private void iniDashBoard() {
        setContentPane(BackGround);
        setTitle("Manager DashBoard");
        setSize(600,600);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        customerInfoButton=new JButton();
        transactionHistoryButton=new JButton();
        Email=new JTextField();
        CustomerEmail=new JLabel();
        allcustomer =new JButton();
        BackGround=new JPanel();
        pay_interest_rate = new JTextField();
        charge_interest_rate = new JTextField();
        payInterestButton = new JButton();
        chargeInterestButton = new JButton();

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
            //@Override
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

        payInterestButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                payInterestButtonmouseClicked(e);
            }
        });
        chargeInterestButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chargeInterestButtonmouseClicked(e);
            }
        });

    }

    private Customer getCustomer(String email){
        cus = Database.getCustomer(email);
        return cus;
    }
    private void customerInfoButtonMouseClicked(java.awt.event.MouseEvent evt){
        String email=Email.getText();
        //String password="example";
        //cus= Database.getCustomer(email);
        // jump to Customer Info page here
    }
    private void transactionHistoryButtonMouseClicked(java.awt.event.MouseEvent evt){
        viewTransaction viewtransaction = new viewTransaction();
        viewtransaction.setVisible(true);
        dispose();
    }
    private void allcustomerMouseClicked(java.awt.event.MouseEvent evt){
        //AllcustomerFrame allcustomerFrame = new AllcustomerFrame();
        //allcustomerFrame.setVisible(true);
        //dispose();
    }

    private void payInterestButtonmouseClicked(MouseEvent e){
        float p_rate = Float.valueOf(pay_interest_rate.getText());
        // note: the pay Interest function in interface should add a float parameter
        payInterest(p_rate);
    }

    void payInterest(float p_rate){

    }

    private void chargeInterestButtonmouseClicked(MouseEvent e){
        float c_rate = Float.valueOf(charge_interest_rate.getText());
        // note: the pay Interest function in interface should add a float parameter
        chargeInterest(c_rate);
    }

    void chargeInterest(float c_rate){

    }
    /*
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

     */
}
