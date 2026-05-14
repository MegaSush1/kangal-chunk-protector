package kangal.chunk_protector.protection;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Set;

public class ProtectionCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("protectchunk")
                        .then(Commands.literal("add")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                                .executes(commandContext -> {

                                    ServerPlayer player = commandContext.getSource().getPlayer();

                                    assert player != null;
                                    ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

                                    ProtectedChunkManager.addProtectedChunk(
                                            player.level(),
                                            chunkPos
                                    );

                                    player.sendSystemMessage(Component.literal("Chunk "+chunkPos+" protected"),false);

                                    return 1;
                                })
                        )
                        .then(Commands.literal("remove")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                                .executes(commandContext -> {

                                    ServerPlayer player = commandContext.getSource().getPlayer();

                                    assert player != null;
                                    ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

                                    ProtectedChunkManager.removeProtectedChunk(
                                            player.level(),
                                            chunkPos
                                    );

                                    player.sendSystemMessage(Component.literal("Chunk "+chunkPos+" unprotected"),false);

                                    return 1;
                                })
                        )
                        .then(Commands.literal("list")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                                .executes(commandContext -> {

                                    ServerPlayer player = commandContext.getSource().getPlayer();

                                    assert player != null;
                                    ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

                                    Map<String, Set<Long>> chunks = ProtectedChunkManager.getProtectedChunks( player.level() );

                                    player.sendSystemMessage(Component.literal(" PROTECTED CHUNK LIST "),false);
                                    for  (Map.Entry<String, Set<Long>> entry : chunks.entrySet()) {
                                        player.sendSystemMessage(Component.literal(" KEY : "+ entry.getKey()),false);
                                        for (Long value : entry.getValue()) {
                                            player.sendSystemMessage(Component.literal(ChunkPos.unpack(value).toString()),false);

                                        }
                                    }

                                    return 1;
                                })
                        )
        );

    }
}
