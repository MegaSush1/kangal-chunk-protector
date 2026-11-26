package kangal.chunk_protector.protection;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.Map;
import java.util.Set;

import static kangal.chunk_protector.Kangal_chunk_protector.CHAT_TAG_COMPONENT;

public class ProtectionCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        final PermissionLevel permissionlevelcommand = PermissionLevel.ADMINS;

        dispatcher.register(
                Commands.literal("protectchunk")
                        .then(Commands.literal("add")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(permissionlevelcommand)))
                                .executes(commandContext -> {

                                    ServerPlayer player = commandContext.getSource().getPlayer();

                                    assert player != null;
                                    ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

                                    ProtectedChunkManager.addProtectedChunk(
                                            player.level(),
                                            chunkPos
                                    );

                                player.sendSystemMessage(CHAT_TAG_COMPONENT.copy().append(Component.literal("Chunk "+chunkPos+" protected")),false);

                                    return 1;
                                })
                        )
                        .then(Commands.literal("remove")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(permissionlevelcommand)))
                                .executes(commandContext -> {

                                    ServerPlayer player = commandContext.getSource().getPlayer();

                                    assert player != null;
                                    ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

                                    ProtectedChunkManager.removeProtectedChunk(
                                            player.level(),
                                            chunkPos
                                    );

                                    player.sendSystemMessage(CHAT_TAG_COMPONENT.copy().append(Component.literal("Chunk "+chunkPos+" unprotected")),false);

                                    return 1;
                                })
                        )
                        .then(Commands.literal("list")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(permissionlevelcommand)))
                                .executes(commandContext -> {
                                    ServerPlayer player = commandContext.getSource().getPlayer();

                                    assert player != null;

                                    Map<String, Set<Long>> chunks =
                                            ProtectedChunkManager.getProtectedChunks(player.level());

                                    // If entry set is empty
                                    if (chunks.isEmpty()) {
                                        player.sendSystemMessage(
                                                CHAT_TAG_COMPONENT.copy().append(
                                                        Component.literal("There are no protected chunks in this world")
                                                ),
                                                false
                                        );
                                        return 1;
                                    }

                                    // Print title
                                    player.sendSystemMessage(
                                            CHAT_TAG_COMPONENT.copy().append(
                                                    Component.literal(" List of protected chunks")
                                            ),
                                            false
                                    );

                                    for (Map.Entry<String, Set<Long>> entry : chunks.entrySet()) {

                                        // Dimension title
                                        Component message = Component.literal("Dimension: ")
                                                .append(Component.literal(entry.getKey())
                                                        .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));

                                        message = message.copy().append("\n");

                                        for (Long value : entry.getValue()) {

                                            ChunkPos pos = ChunkPos.unpack(value);

                                            Component chunkComponent = Component.literal("  " + pos)
                                                    .withStyle(Style.EMPTY
                                                            .withColor(ChatFormatting.GRAY)
                                                            .withClickEvent(
                                                                    new ClickEvent.RunCommand(
                                                                            "tp @s " + (pos.x() * 16) + " ~ " + (pos.z() * 16)
                                                                    )
                                                            )
                                                            .withHoverEvent(
                                                                    new HoverEvent.ShowText(
                                                                            Component.literal("Click to teleport to this chunk")
                                                                    )
                                                            ));

                                            message = message.copy().append(chunkComponent);
                                        }

                                        message = message.copy().append("\n");

                                        player.sendSystemMessage(message, false);
                                    }

                                    return 1;
                                })
                        )
        );

    }
}
