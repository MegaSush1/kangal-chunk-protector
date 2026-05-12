package kangal.chunk_protector.protection;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

import static kangal.chunk_protector.Kangal_chunk_protector.MOD_LOGGER;

public class ProtectedChunkManager {

    private static final Set<ChunkPos> PROTECTED_CHUNKS = new HashSet<>();

    static {
        addProtectedChunk(0, 0);
    }

    public static void addProtectedChunk(int chunkX, int chunkZ){
        PROTECTED_CHUNKS.add(new ChunkPos(chunkX,chunkZ));
    }

    public static void removeProtectedChunk(int chunkX, int chunkZ){
        PROTECTED_CHUNKS.remove(new ChunkPos(chunkX,chunkZ));
    }

    public static boolean isProtectedChunk(Level world, ChunkPos chunkPos){
        MOD_LOGGER.info(String.format("Checking if chunk %s", chunkPos));
        MOD_LOGGER.info(String.format("Chunk : %b", PROTECTED_CHUNKS.contains(chunkPos)));
        return PROTECTED_CHUNKS.contains(chunkPos);
    }
}
