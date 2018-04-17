package hu.jlaci.mockchain.transaction.buffer;

import hu.jlaci.mockchain.transaction.Transaction;

public interface TransactionListener {

    void receivedNewTransaction(Transaction transaction);
}
