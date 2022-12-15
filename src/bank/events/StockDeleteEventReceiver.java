package bank.events;

import bank.accounts.Account;
import bank.accounts.AccountType;
import bank.currencies.USDollar;
import bank.repositories.AccountAdapter;
import bank.repositories.AccountRepository;
import bank.repositories.HoldingAdapter;
import bank.repositories.HoldingRepository;
import bank.trades.Holding;
import databases.DbConnection;

import java.sql.Connection;
import java.util.List;


/**
 * Stock Delete event Receiver applies the action
 * to the holding of customer when stock gets deleted
 */
public class StockDeleteEventReceiver implements EventReceiver {
    private static final Connection connection = DbConnection.getConnection();
    private static final HoldingRepository holdingRepository = HoldingAdapter.getInstance(connection);
    private static final AccountRepository accountRepository = AccountAdapter.getInstance(connection);

    @Override
    public void receiveEvents(Event event) {
        applyAction(event);
    }

    /**
     * This method is to apply actions for this particular event
     * @param event StockDeleteEvent
     */
    @Override
    public void applyAction(Event event) {
        StockDeleteEvent stockEvent = (StockDeleteEvent) event;
        List<Holding> holdings = holdingRepository.read();

        for (Holding holding : holdings) {
            List<Account> accounts = accountRepository.readByCustomerId(holding.getCid());
            for (Account account : accounts) {
                if (account.getAccountType() == AccountType.SECURITIES) {
                    account.credit(holding.getCurrentValue(), new USDollar(holding.getCurrentValue()));
                    accountRepository.update(account);
                    break;
                }
            }
            holdingRepository.delete(holding);  // deleting from db so no concurrent modification
        }
    }
}
