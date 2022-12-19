# CS611 - Final Project
## Bank
---------------------------------------------------------------------------

## Files
---------------------------------------------------------------------------
src/
├── app
│     ├── ApplyLoanScreen.form
│     ├── ApplyLoanScreen.java
│     ├── BalanceScreen.form
│     ├── BalanceScreen.java
│     ├── bank.png
│     ├── BuyStock.form
│     ├── BuyStock.java
│     ├── CloseAccountScreen.form
│     ├── CloseAccountScreen.java
│     ├── DashBoardScreen.form
│     ├── DashBoardScreen.java
│     ├── DepositScreen.form
│     ├── DepositScreen.java
│     ├── icon.png
│     ├── MainScreen.form
│     ├── MainScreen.java
│     ├── ManagerDashBoard.form
│     ├── ManagerDashBoard.java
│     ├── NewAccountScreen.form
│     ├── NewAccountScreen.java
│     ├── Pwdreset.form
│     ├── Pwdreset.java
│     ├── RegisterScreen.form
│     ├── RegisterScreen.java
│     ├── SellStock.form
│     ├── SellStock.java
│     ├── Transactions.form
│     ├── Transactions.java
│     ├── TransferAmountScreen.form
│     ├── TransferAmountScreen.java
│     ├── viewTransaction.form
│     ├── viewTransaction.java
│     ├── Withdrawable.form
│     └── Withdrawable.java
├── bank
│     ├── accounts
│     │     ├── Account.java : Account is an abstract base class for Checking, Saving & Securities
                               * @see CheckingAccount
                               * @see SavingAccount
                               * @see SecuritiesAccount
                               * @see LoanAccount
│     │     ├── AccountType.java : Type of accounts
│     │     ├── CheckingAccount.java : Checking account extend account to apply TRANSACTION SERVICE CHARGES on each transaction
│     │     ├── LoanAccount.java : Creates a new loan account and also put interest rate on each transaction made through it
                                   * The interest rate is fixed for simplicity and it will be added to the loan value.
│     │     ├── SavingAccount.java : Child class of account to manage all saving account operations
│     │     ├── SecuritiesAccount.java : Child class of Account and manages all Securities related operations
                                         * to buy and sell stocks using this account
│     │     └── Transaction.java : Transaction keeps all the history for credit and debit
                                   * happens on any of the account. It also stores tax info
                                   * This works similar to the logs of an application.
│     ├── atm
│     │     ├── ATM.java : Common abstract class for both manager and customer
│     │     ├── CustomerATMController.java : Customer controller is responsible for managing all customer operations
│     │     ├── CustomerATM.java : CustomerATM is an interface used for the customer session
                                   * ATM can have specific sessions for different customers and one session for ManagerATM.
│     │     ├── ManagerATMController.java : Manager controller is responsible for managing all manager operations
│     │     └── ManagerATM.java : ManagerATM represents the manager session to view all customers
                                  * check specific customer, should see the daily reports on transactions.
│     ├── currencies
│     │     ├── CanadianDollar.java : Canadian currency which follows the Currency interface
│     │     ├── Currency.java : Currency interface provides an interface to all
                                * types of currency. The base value would be USD
                                * The conversion rates would be applied based on
                                * base value. The Account always stores value in
                                * base USD. The conversion happens when customer
                                * withdraw/deposit different currency.
│     │     ├── CurrencyType.java : Type of currency
│     │     ├── Euro.java : Euro currency which follows the Currency interface
│     │     └── USDollar.java : USDollar currency which follows the Currency interface
│     ├── customers
│     │     ├── assets
│     │     │     └── Collateral.java : Collateral is to store details of collateral a customer gives
│     │     ├── Customer.java : Customer class represent the customers of the bank
                                It extends the Person class which is basic for any Person
│     │     └── Person.java : Abstract base class for any Person
│     ├── events
│     │     ├── Event.java : All events happening in the bank application
                             should be of event type. This is base abstract class
│     │     ├── EventReceiver.java : All events receiver should implement this interface
│     │     ├── EventSender.java : All events sender should implement this interface
│     │     ├── EventType.java : Event could be different type in bank application
                                 Action taken by the sender would be smart based on type of event
│     │     ├── StockDeleteEvent.java : Stock Delete event only bothers about the stock getting deleted by manager, This will
                                        remove that stock from customer holding and credit the amount respectively to customer account.
│     │     ├── StockDeleteEventReceiver.java : Stock Delete event Receiver applies the action to the holding of customer when stock gets deleted
│     │     ├── StockDeleteEventSender.java : Sender will notify all StockDelete receiver about the stock deletion
│     │     ├── StockUpdateEvent.java : Stock Update event only bothers about the stock prices going up or down, This will update the prices of customer holdings
│     │     ├── StockUpdateEventReceiver.java : Stock Update event Receiver applies the action to the holding of customer when price updates
│     │     └── StockUpdateEventSender.java : Sender will notify all StockUpdate receiver about the price change with stock event
│     ├── factories
│     │     ├── AccountFactory.java : Account factory
│     │     ├── CollateralFactory.java : Collateral Factory
│     │     ├── CustomerFactory.java : Customer Factory is very helpful for formatting dates and everything
│     │     ├── HoldingFactory.java : Holding Factory
│     │     ├── LoanFactory.java : Loan factory
│     │     ├── StockFactory.java : Stock Factory
│     │     └── TransactionFactory.java : Transaction Factory
│     ├── loans
│     │     └── Loan.java : Loan is for creating new loan for customer
│     ├── repositories
│     │     ├── AccountAdapter.java : Account adapter is an adapter based on AccountRepository, This follows AccountRepository interface.
│     │     ├── AccountRepository.java : Account repository extend Repository for db connection
│     │     ├── CollateralAdapter.java : Collateral Adapter implements collateral repository
│     │     ├── CollateralRepository.java : Collateral Repository extends repository
│     │     ├── CustomerAdapter.java : Customer Adapter implemented Customer Repository
│     │     ├── CustomerRepository.java : Customer Repository extends repository
│     │     ├── HoldingAdapter.java : Holding repository implements Holding Repository
│     │     ├── HoldingRepository.java : Holding repository extend repository
│     │     ├── LoanAdapter.java : Loan Adapter implements Loan Repository
│     │     ├── LoanRepository.java : Loan Repository extends Repository
│     │     ├── Repository.java : Repository is a database separation layer which throws all basic methods we want for CRUD operations
│     │     ├── StockAdapter.java : Stock Adapter implements Stock Repository
│     │     ├── StockRepository.java : Stock Repository extends Repository
│     │     ├── TransactionAdapter.java : Transaction Adapter implements Transaction repository
│     │     └── TransactionRepository.java : Transaction Repository extends Repository
│     └── trades
│         ├── Holding.java : Holding represents the current holding of the customer
│         └── Stock.java : Stock represent stock model
├── databases
│     ├── DbConnection.java : DbConnection is singleton class for SQLITE database
│     └── onlinebank.sqlite : database file
└── Main.java : Main class for running whole project


## Notes
---------------------------------------------------------------------------
1. queries.sql, onlinebank.sqlite are used for database purpose. Few images are added to app for UI screens
2. (a) Implemented Observer pattern for stock price updates.
   (b) Repository pattern is implemented for decoupling database layer with application.


## How to compile and run
---------------------------------------------------------------------------
1. Navigate to the directory "bank" after unzipping the files
2. javac -cp ".:bin/sqlite-jdbc-3.40.0.0.jar:bin/forms-1.2.1.jar" -d prod/src/ @sources.txt
3. cd prod/src/
4. java -cp .:../../bin/sqlite-jdbc-3.40.0.0.jar:../../bin/forms-1.2.1.jar Main