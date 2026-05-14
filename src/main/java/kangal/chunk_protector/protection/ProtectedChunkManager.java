package kangal.chunk_protector.protection;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class ProtectedChunkManager {

    public static void addProtectedChunk(ServerLevel world, ChunkPos chunkPos){

        ProtectionSavedData.get(world.getServer()).protectChunk(world,chunkPos);
    }

    public static void removeProtectedChunk(ServerLevel world, ChunkPos chunkPos){

        ProtectionSavedData.get(world.getServer()).unprotectChunk(world,chunkPos);
    }

    public static boolean isProtectedChunk(ServerLevel world, ChunkPos chunkPos){

        return  ProtectionSavedData.get(world.getServer()).isProtectedChunk(world,chunkPos);
    }

    public static Map<String, Set<Long>> getProtectedChunks(ServerLevel world){
        return ProtectionSavedData.get(world.getServer()).getProtectedChunks();
    }
}
