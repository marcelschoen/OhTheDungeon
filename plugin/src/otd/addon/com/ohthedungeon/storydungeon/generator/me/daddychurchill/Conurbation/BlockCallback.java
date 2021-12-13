// 
// Decompiled by Procyon v0.5.36
// 

package otd.addon.com.ohthedungeon.storydungeon.generator.me.daddychurchill.Conurbation;

import otd.addon.com.ohthedungeon.storydungeon.generator.me.daddychurchill.Conurbation.Support.ByteChunk;

import java.util.Random;

public class BlockCallback
{
    private ChunkCallback chunkGen;
    
    public BlockCallback(final ChunkCallback chunkGen) {
        this.chunkGen = chunkGen;
    }
    
    public void populate(final ByteChunk realChunk, final Random random, final int chunkX, int chunkZ) {
        this.chunkGen.populate(realChunk, random, chunkX, chunkZ);
    }
}
