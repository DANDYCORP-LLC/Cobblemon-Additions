package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.minecraft.block.Block;

/*
lazy initialization to avoid enum
initializing before blocks are registered
which just ends up returning AIR blocks.
*/

public enum GrinderPartTypes {
    MAIN {
        @Override
        public Block getBlock() {
            return DANDYCORPBlocks.GRINDER_BLOCK.get();
        }
    },
    INPUT {
        @Override
        public Block getBlock() {
            return DANDYCORPBlocks.GRINDER_INPUT_BLOCK.get();
        }
    },
    ROTATIONAL {
        @Override
        public Block getBlock() {
            return DANDYCORPBlocks.GRINDER_ROTATIONAL_BLOCK.get();
        }
    },
    OUTPUT {
        @Override
        public Block getBlock() {
            return DANDYCORPBlocks.GRINDER_OUTPUT_BLOCK.get();
        }
    },
    CORNER {
        @Override
        public Block getBlock() {
            return DANDYCORPBlocks.GRINDER_CORNER_BLOCK.get();
        }
    },
    EMPTY {
        @Override
        public Block getBlock() {
            return net.minecraft.block.Blocks.AIR;
        }
    };

    public abstract Block getBlock();
}

