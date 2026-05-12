package kangal.chunk_protector.protection;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ProtectedChunkManager {

    private static final Map<ResourceKey<Level>, LongSet> PROTECTED_CHUNKS = new HashMap<>();

    public static void addProtectedChunk(Level world, ChunkPos chunkPos){
        long chunk = chunkPos.pack();

        PROTECTED_CHUNKS
                .computeIfAbsent(
                        world.dimension(),
                        key -> new LongOpenHashSet()
                )
                .add(chunk);
    }

    public static void removeProtectedChunk(Level world, ChunkPos chunkPos){

        long chunk = chunkPos.pack();

        LongSet chunks = PROTECTED_CHUNKS.get(world.dimension());

        if (chunks != null){
            chunks.remove(chunk);
        }

    }

    public static boolean isProtectedChunk(Level world, ChunkPos chunkPos){

        LongSet chunks = PROTECTED_CHUNKS.get(world.dimension());

        if(chunks == null) return false;

        return chunks.contains(chunkPos.pack());
    }
}
