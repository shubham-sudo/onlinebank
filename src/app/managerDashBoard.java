package app;

import bank.account.Account;
import bank.account.Transaction;
import bank.customer.Customer;
import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class managerDashBoard extends JFrame {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                inimanagerDashBoard();
            }
        });
    }
    public static void inimanagerDashBoard(){
        JFrame ManagerDashBoard = new JFrame("managerDashBoard");
        ManagerDashBoard.setContentPane(new managerDashBoard().BackGround);
        ManagerDashBoard.pack();
        ManagerDashBoard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ManagerDashBoard.setVisible(true);

    }
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
    private JScrollPane showAllCustomer;
    private JTextArea CustomerTextarea;
    private JScrollPane showAllTransaction;
    private JLabel p_rate_per;
    private JLabel c_rate_per;
    private JTextArea TransactionTextarea;
    public managerDashBoard() {
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

    }

    private Customer getCustomer(String email){
        cus = Database.getCustomer(email);
        return cus;
    }
    private void customerInfoButtonMouseClicked(java.awt.event.MouseEvent evt){
        String email=Email.getText();
        //String password="example";
        //cus= Database.getCustomer(email);
        Customer ncus=getCustomer(email);
        // TODO Here should have a Customer login without password.
    }
    private List<Account> getAllAccounts(List<Customer> customers){
        List<Account> ans = new ArrayList<Account>();
        for(int i=0;i<customers.size();i++){
            List<Account> accounti = Database.getAccounts(customers.get(i).getId());
            Iterator j = accounti.iterator();
            while (j.hasNext())
            {
                ans.add((Account)j.next());
            }
        }
        return ans;
    }
    private List<Transaction> getTransaction(LocalDate todayDate){
        List<Transaction> ans = new ArrayList<Transaction>();
        List<Customer> customers = getCustomers();
        for(int i=0;i<customers.size();i++){
            List<Transaction> transactioni = Database.getTransactions(customers.get(i).getId());
            Iterator j = transactioni.iterator();
            while (j.hasNext())
            {
                Transaction t = (Transaction)j.next();
                if(t.getTodayDate().isEqual(todayDate)) {
                    ans.add(t);
                }
            }
        }
        return ans;
    }
    private void transactionHistoryButtonMouseClicked(java.awt.event.MouseEvent evt){
        List<Transaction> transactions = getTransaction(LocalDate.now());
        for(int i=0;i<transactions.size();i++){
            TransactionTextarea.append(transactions.get(i).toString()+"\n");
        }
        showAllTransaction.setViewportView(TransactionTextarea);
    }

    private List<Customer> getCustomers(){
        List<Customer> ans = new ArrayList<Customer>();
        ans = Database.getAllCustomers();
        return ans;
    }
    private void allcustomerMouseClicked(java.awt.event.MouseEvent evt){
        List<Customer> customers = getCustomers();
        for(int i=0;i<customers.size();i++){
            CustomerTextarea.append(
                      customers.get(i).getFirstName()+" " + customers.get(i).getLastName()
                    + "\n"
                    + customers.get(i).getEmail()
                    + "\n"
                    + customers.get(i).getPhoneNumber()
                    + "\n"
                    );
        }
        showAllCustomer.setViewportView(CustomerTextarea);
    }

    private void payInterestButtonmouseClicked(MouseEvent e){
        float p_rate = Float.valueOf(pay_interest_rate.getText());
        // TODO: the pay Interest function in interface should add a float parameter
        payInterest(p_rate);
    }

    void payInterest(float p_rate){
        List<Account> accounts = getAllAccounts(getCustomers());
        for(int i=0;i<accounts.size();i++){
            Account a = accounts.get(i);
            if(a.getAccountType().toString().equals("Saving")){
                a.interest(p_rate);
            }
        }
    }

    private void chargeInterestButtonmouseClicked(MouseEvent e){
        float c_rate = Float.valueOf(charge_interest_rate.getText());
        c_rate = c_rate*-1;
        // TODO: the pay Interest function in interface should add a float parameter
        chargeInterest(c_rate);
    }

    void chargeInterest(float c_rate){
        List<Account> accounts = getAllAccounts(getCustomers());
        for(int i=0;i<accounts.size();i++){
            Account a = accounts.get(i);
            if(a.getAccountType().toString().equals("Loan")){
                a.interest(c_rate);
            }
        }
    }
    /*
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

     */
}
