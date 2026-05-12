package kangal.chunk_protector.protection;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ProtectedChunkManager {

    private static final LongSet PROTECTED_CHUNKS = new LongOpenHashSet();

    static {
        //addProtectedChunk(new ChunkPos(0, 0));
    }

    public static void addProtectedChunk(Level world, ChunkPos chunkPos){

        PROTECTED_CHUNKS.add( chunkPos.pack() );
    }

    public static void removeProtectedChunk(Level world, ChunkPos chunkPos){
        PROTECTED_CHUNKS.remove( chunkPos.pack() );
    }

    public static boolean isProtectedChunk(Level world, ChunkPos chunkPos){
        return PROTECTED_CHUNKS.contains(chunkPos.pack());
    }
}
