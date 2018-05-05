package hu.jlaci.mockchain.mining;

import hu.jlaci.mockchain.block.Block;

public interface MiningListener {

    void blockChanged(Block block);
}
