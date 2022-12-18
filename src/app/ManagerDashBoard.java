package app;

import bank.accounts.Transaction;
import bank.atm.ManagerATMController;
import bank.customers.Customer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;


public class ManagerDashBoard extends JFrame {
    private final ManagerATMController managerATMController;

//    public static void main(String[] args) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ManagerDashBoard().setVisible(true);
//            }
//        });
//    }

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
//        customerInfoButton = new JButton();
//        transactionHistoryButton = new JButton();
//        CustomerEmail = new JLabel();
//        Email = new JTextField();
//        allcustomer = new JButton();
//        BackGround = new JPanel();
//        payInterestButton = new JButton();
//        chargeInterestButton = new JButton();
//        payInterestRate = new JTextField();
//        chargeInterestRate = new JTextField();
//        showAllCustomer = new JScrollPane();
        CustomerTextarea = new JTextArea();
//        showAllTransaction = new JScrollPane();
//        p_rate_per = new JLabel();
//        c_rate_per = new JLabel();
//        back = new JButton();
//        D_stock_id = new JTextField();
//        D_stockbutton = new JButton();
//        A_stock_name = new JTextField();
//        A_stock_value = new JTextField();
//        A_stockbutton = new JButton();
//        U_Stock_value = new JTextField();
//        U_Stock_id = new JTextField();
//        U_stockbuton = new JButton();
//        customerinfolabel = new JLabel();
        TransactionTextarea = new JTextArea();

        BackGround.setBackground(new Color(254, 254, 254));
        BackGround.setForeground(new Color(254, 254, 254));

        setContentPane(BackGround);
        CustomerTextarea = new JTextArea("show AllCustomer \n");
        CustomerTextarea.setEditable(true);
        showAllCustomer.setViewportView(CustomerTextarea);

        TransactionTextarea = new JTextArea("show Alltransaction \n");
        TransactionTextarea.setEditable(true);
        showAllTransaction.setViewportView(TransactionTextarea);

        CustomerEmail.setText("Enter Customer Email: ");
        customerinfolabel.setText("customer Information");
        customerInfoButton.setText("Show Information");
        customerInfoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                customerInfoButtonMouseClicked(evt);

            }
        });
        transactionHistoryButton.setText("transaction History");
        transactionHistoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                transactionHistoryButtonMouseClicked(evt);

            }
        });

        allcustomer.setText("all customers");
        allcustomer.addMouseListener(new MouseAdapter() {
            //@Override
            public void mouseClicked(MouseEvent evt) {
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

    private void D_stockbuttonmouseClicked(MouseEvent e) {
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

    private void A_stockbuttonmouseClicked(MouseEvent e) {
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

    private void U_stockbuttonmouseClicked(MouseEvent e) {
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

    private void backmouseClicked(MouseEvent e) {
        managerATMController.logout();
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
        dispose();
    }

    private Customer getCustomer(String email) {
        customer = managerATMController.getCustomer(email);
        return customer;
    }

    private void customerInfoButtonMouseClicked(MouseEvent evt) {
        String email = Email.getText();
        try {
            Customer customer = getCustomer(email);
            String info = customer.getFirstName() + " " + customer.getLastName()
                    + " | " + customer.getEmail()
                    + " | " + customer.getPhoneNumber();
            customerinfolabel.setText(info);
        } catch (Exception e) {
            customerinfolabel.setText("Enter email plz");
            ;
        }
    }

    private List<Transaction> getTransaction(LocalDate todayDate) {
        return this.managerATMController.getTransaction(todayDate);
    }

    private void transactionHistoryButtonMouseClicked(MouseEvent evt) {
        for (Transaction transaction : getTransaction(LocalDate.now())) {
            TransactionTextarea.append(transaction.toString() + "\n");
        }
        showAllTransaction.setViewportView(TransactionTextarea);
    }

    private List<Customer> getCustomers() {
        return managerATMController.getCustomers();
    }

    private void allcustomerMouseClicked(MouseEvent evt) {
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

    private void payInterestButtonmouseClicked(MouseEvent e) {
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
        JOptionPane.showMessageDialog(this, "Interest paid successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void chargeInterestButtonmouseClicked(MouseEvent e) {
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
        rate = rate * -1;
        this.managerATMController.chargeInterest(rate);
        JOptionPane.showMessageDialog(this, "Interest charged successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        BackGround = new JPanel();
        BackGround.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:130px:noGrow,left:4dlu:noGrow,fill:40px:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
        BackGround.setPreferredSize(new Dimension(800, 400));
        CustomerEmail = new JLabel();
        CustomerEmail.setText("Enter customer email");
        CellConstraints cc = new CellConstraints();
        BackGround.add(CustomerEmail, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        Email = new JTextField();
        Email.setText("Email");
        BackGround.add(Email, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
//        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer1, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer2, cc.xy(5, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer3, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
//        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer4, cc.xy(1, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
//        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer5, cc.xy(7, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
//        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer6, cc.xy(7, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        allcustomer = new JButton();
        allcustomer.setText("all customer");
        BackGround.add(allcustomer, cc.xyw(3, 11, 3));
//        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer7, cc.xy(5, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer8, cc.xy(3, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer9, cc.xy(3, 27, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer10, cc.xy(5, 27, CellConstraints.DEFAULT, CellConstraints.FILL));
        showAllCustomer = new JScrollPane();
        showAllCustomer.setPreferredSize(new Dimension(80, 24));
        showAllCustomer.setToolTipText("All customer");
        BackGround.add(showAllCustomer, cc.xyw(3, 13, 3, CellConstraints.FILL, CellConstraints.FILL));
        showAllCustomer.setBorder(BorderFactory.createTitledBorder(null, "all customer", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
//        final com.intellij.uiDesigner.core.Spacer spacer11 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer11, cc.xy(3, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer12 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer12, cc.xy(5, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
        back = new JButton();
        back.setText("LogOut to MainScreen");
        BackGround.add(back, cc.xyw(3, 25, 3));
//        final com.intellij.uiDesigner.core.Spacer spacer13 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer13, cc.xy(5, 21, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer14 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer14, cc.xy(3, 21, CellConstraints.DEFAULT, CellConstraints.FILL));
        transactionHistoryButton = new JButton();
        transactionHistoryButton.setText("transaction history");
        BackGround.add(transactionHistoryButton, cc.xyw(9, 11, 3));
        showAllTransaction = new JScrollPane();
        showAllTransaction.setToolTipText("All Transaction today");
        BackGround.add(showAllTransaction, cc.xyw(9, 13, 3, CellConstraints.FILL, CellConstraints.FILL));
        showAllTransaction.setBorder(BorderFactory.createTitledBorder(null, "all transaction today", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
//        final com.intellij.uiDesigner.core.Spacer spacer15 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer15, cc.xy(13, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
//        final com.intellij.uiDesigner.core.Spacer spacer16 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer16, cc.xy(9, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
//        final com.intellij.uiDesigner.core.Spacer spacer17 = new com.intellij.uiDesigner.core.Spacer();
//        BackGround.add(spacer17, cc.xy(11, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        payInterestRate = new JTextField();
        BackGround.add(payInterestRate, cc.xy(9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        chargeInterestRate = new JTextField();
        BackGround.add(chargeInterestRate, cc.xy(11, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        payInterestButton = new JButton();
        payInterestButton.setText("Pay Interest");
        BackGround.add(payInterestButton, cc.xy(9, 7));
        chargeInterestButton = new JButton();
        chargeInterestButton.setText("charge interest");
        BackGround.add(chargeInterestButton, cc.xy(11, 7));
        p_rate_per = new JLabel();
        p_rate_per.setText("Enter pay interest(%)");
        BackGround.add(p_rate_per, cc.xy(9, 3));
        c_rate_per = new JLabel();
        c_rate_per.setText("Enter charge Interest(%)");
        BackGround.add(c_rate_per, cc.xy(11, 3));
        D_stock_id = new JTextField();
        D_stock_id.setText("Enter stock id");
        BackGround.add(D_stock_id, cc.xy(3, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
        D_stockbutton = new JButton();
        D_stockbutton.setText("Delete Stock");
        BackGround.add(D_stockbutton, cc.xy(5, 19));
        A_stock_name = new JTextField();
        A_stock_name.setText("Enter stock name");
        BackGround.add(A_stock_name, cc.xy(9, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
        A_stock_value = new JTextField();
        A_stock_value.setText("Enter stock value");
        BackGround.add(A_stock_value, cc.xy(9, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
        A_stockbutton = new JButton();
        A_stockbutton.setText("Add Stock");
        BackGround.add(A_stockbutton, cc.xy(11, 19));
        U_Stock_value = new JTextField();
        U_Stock_value.setText("Enter stock value");
        BackGround.add(U_Stock_value, cc.xy(9, 23, CellConstraints.FILL, CellConstraints.DEFAULT));
        U_Stock_id = new JTextField();
        U_Stock_id.setText("Enter stock id");
        BackGround.add(U_Stock_id, cc.xy(9, 25, CellConstraints.FILL, CellConstraints.DEFAULT));
        U_stockbuton = new JButton();
        U_stockbuton.setText("Update Stock");
        BackGround.add(U_stockbuton, cc.xy(11, 25));
        customerInfoButton = new JButton();
        customerInfoButton.setText("customer info");
        BackGround.add(customerInfoButton, cc.xyw(3, 5, 3));
        customerinfolabel = new JLabel();
        customerinfolabel.setText("Label");
        BackGround.add(customerinfolabel, cc.xyw(3, 7, 3));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return BackGround;
    }

}
