package hu.jlaci.mockchain.block;

import hu.jlaci.mockchain.Hashable;
import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.util.HashUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Block implements Serializable, Hashable {

    private UUID blockId;

    private List<Transaction> transactions = new ArrayList<>();

    private String previousHash;

    private long nonce;

    @Override
    public String getHash() {
        StringBuilder sb = new StringBuilder();
        for (Transaction transaction : transactions) {
            sb.append(transaction.getHash());
        }
        return HashUtil.sha256(blockId + previousHash + nonce + sb.toString());
    }

    @Override
    public String toString() {
        return "Block id:" + blockId + " hash: " + getHash();
    }
}
