package kangal.chunk_protector.protection;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class ProtectionSavedData extends SavedData {

    private final Map<String, Set<Long>> protectedChunks;

    // EMPTY CONSTRUCTOR
    public ProtectionSavedData(){
        this.protectedChunks = new HashMap<>();
    }

    // CODEC CONSTRUCTOR
    public ProtectionSavedData(Map<String, Set<Long>> protectedChunks){
        this.protectedChunks = protectedChunks;
    }

    // GETTER
    public Map<String, Set<Long>> getProtectedChunks(){
        return this.protectedChunks;
    }

    // DIMENSION SET GETTER
    private Set<Long> getDimensionSet(ResourceKey<Level> worldKey){
        return this.protectedChunks.computeIfAbsent(worldKey.identifier().toString(), key -> new HashSet<>());
    }

    // ADD PROTECT
    public void protectChunk(Level world, ChunkPos chunkPos){
        getDimensionSet(world.dimension()).add(chunkPos.pack());
        setDirty();
    }

    // REMOVE PROTECT
    public void unprotectChunk(Level world, ChunkPos chunkPos){
        getDimensionSet(world.dimension()).remove(chunkPos.pack());
        setDirty();
    }

    // CHECK
    public boolean isProtectedChunk(Level world, ChunkPos chunkPos){
        Set<Long> chunks = protectedChunks.get(world.dimension().identifier().toString());

        if (chunks == null) {
            return false;
        }

        return chunks.contains(chunkPos.pack());
    }

    // CODEC
    private static final Codec<ProtectionSavedData> CODEC =
            Codec.unboundedMap(
                    Codec.STRING,
                    Codec.LONG.listOf()
            ).xmap(
                    // LOAD
                    map -> {
                        Map<String, Set<Long>> protectedChunks = new HashMap<>();

                        for (var entry : map.entrySet()) {
                            String key = entry.getKey();

                            protectedChunks.put(
                                    key,
                                    new HashSet<>(entry.getValue())
                            );

                        }

                        return new ProtectionSavedData(protectedChunks);
                    },
                    // SAVE

                    save -> {
                        Map<String, List<Long>> protectedChunks = new HashMap<>();

                        for (var entry : save.protectedChunks.entrySet()) {

                            protectedChunks.put(
                                    entry.getKey(),
                                    List.copyOf(entry.getValue())
                            );

                        }

                        return protectedChunks;
                    }

            );

    // TYPE
    public static final SavedDataType<ProtectionSavedData> TYPE = new SavedDataType<>(
            Objects.requireNonNull(Identifier.tryParse("protected_chunks")),
            ProtectionSavedData::new,
            CODEC,
            null
    );

    // GET
    public static ProtectionSavedData get(MinecraftServer server){

        if (server == null){
            return new ProtectionSavedData();
        }

        return server.getDataStorage().computeIfAbsent(TYPE);
    }

}
