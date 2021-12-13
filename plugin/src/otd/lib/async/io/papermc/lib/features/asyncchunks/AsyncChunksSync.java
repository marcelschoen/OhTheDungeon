package otd.lib.async.io.papermc.lib.features.asyncchunks;

import org.bukkit.Chunk;
import org.bukkit.World;
import otd.lib.async.io.papermc.lib.PaperLib;

import java.util.concurrent.CompletableFuture;

public class AsyncChunksSync implements AsyncChunks {

    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
        if (!gen && !PaperLib.isChunkGenerated(world, x, z)) {
            return CompletableFuture.completedFuture(null);
        } else {
            return CompletableFuture.completedFuture(world.getChunkAt(x, z));
        }
    }
}
