package hu.jlaci.mockchain.transaction;

import hu.jlaci.mockchain.Hashable;
import hu.jlaci.mockchain.util.HashUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Transaction implements Serializable, Hashable {

    private UUID transactionId;

    private UUID originator;

    private String message;

    private byte[] signature;

    @Override
    public String getHash() {
        return HashUtil.sha256(message + transactionId + originator); // Signature is missing intentionally;
    }

    @Override
    public String toString() {
        return "Transaction id: " + transactionId + " hash: " + getHash();
    }
}
