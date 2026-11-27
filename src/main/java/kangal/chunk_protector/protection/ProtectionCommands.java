package kangal.chunk_protector.protection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.impl.util.Arguments;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.core.BlockPos;
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
                                .executes(ProtectionCommands::addChunk)

                                .then(Commands.argument("Chunk x Range", RangeArgument.intRange())
                                        .then(Commands.argument("Chunk z Range", RangeArgument.intRange())
                                                .executes(ProtectionCommands::addChunkWithRange)
                                        )
                                )




                        )
                        .then(Commands.literal("remove")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(permissionlevelcommand)))
                                .executes(ProtectionCommands::removeChunk)

                                .then(Commands.argument("Chunk x Range", RangeArgument.intRange())
                                        .then(Commands.argument("Chunk z Range", RangeArgument.intRange())
                                                .executes(ProtectionCommands::removeChunkWithRange)
                                        )
                                )


                        )
                        .then(Commands.literal("list")
                                .requires(source ->source.permissions().hasPermission(new Permission.HasCommandLevel(permissionlevelcommand)))
                                .executes(ProtectionCommands::listChunks)
                        )
        );

    }

    private static int addChunk(CommandContext<CommandSourceStack> commandContext) {

        ServerPlayer player = commandContext.getSource().getPlayer();

        assert player != null;
        ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

        ProtectedChunkManager.addProtectedChunk(
                player.level(),
                chunkPos
        );

        player.sendSystemMessage(CHAT_TAG_COMPONENT.copy().append(Component.literal("Chunk "+chunkPos+" protected")),false);

        return 1;
    }

    private static int removeChunk(CommandContext<CommandSourceStack> commandContext) {

        ServerPlayer player = commandContext.getSource().getPlayer();

        assert player != null;
        ChunkPos chunkPos = ChunkPos.containing(player.getOnPos());

        ProtectedChunkManager.removeProtectedChunk(
                player.level(),
                chunkPos
        );

        player.sendSystemMessage(Component.empty().append(CHAT_TAG_COMPONENT).append(Component.literal("Chunk "+chunkPos+" unprotected")));

        return 1;
    }

    private static int addChunkWithRange(CommandContext<CommandSourceStack> commandContext) {
        ServerPlayer player = commandContext.getSource().getPlayer();
        assert player != null;

        MinMaxBounds.Ints rangeX = commandContext.getArgument("Chunk x Range",MinMaxBounds.Ints.class);
        MinMaxBounds.Ints rangeZ = commandContext.getArgument("Chunk z Range",MinMaxBounds.Ints.class);

        if (rangeX.min().isEmpty() || rangeX.max().isEmpty() || rangeZ.min().isEmpty() || rangeZ.max().isEmpty()) {
            player.sendSystemMessage(Component.empty().append(CHAT_TAG_COMPONENT).append(Component.literal("Only fixed range are accepted").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
            return 1;
        }

        for(int x = rangeX.min().get(); x <= rangeX.max().get(); x++){
            for  (int z = rangeZ.min().get(); z <= rangeZ.max().get(); z++) {
                ChunkPos chunkPos = ChunkPos.containing(new BlockPos(x*16, 0, z*16));

                ProtectedChunkManager.addProtectedChunk(
                        player.level(),
                        chunkPos
                );
            }
        }

        int chunkXaxis = (Math.abs(rangeX.min().get()) + Math.abs(rangeX.max().get()) );
        int chunkZaxis = (Math.abs(rangeZ.min().get()) + Math.abs(rangeZ.max().get()) );

        player.sendSystemMessage(Component.empty().append(CHAT_TAG_COMPONENT)
                .append(Component.literal("Chunk from ["+rangeX.min().get()+";"+rangeZ.min().get()+"] to ["+rangeX.max().get()+";"+rangeZ.max().get()+"] added")).append("\n")
                .append(Component.literal("Total Chunk affected : "+ (chunkXaxis*chunkZaxis) )));

        return 1;
    }

    private static int removeChunkWithRange(CommandContext<CommandSourceStack> commandContext) {
        ServerPlayer player = commandContext.getSource().getPlayer();
        assert player != null;

        MinMaxBounds.Ints rangeX = commandContext.getArgument("Chunk x Range",MinMaxBounds.Ints.class);
        MinMaxBounds.Ints rangeZ = commandContext.getArgument("Chunk z Range",MinMaxBounds.Ints.class);

        if (rangeX.min().isEmpty() || rangeX.max().isEmpty() || rangeZ.min().isEmpty() || rangeZ.max().isEmpty()) {
            player.sendSystemMessage(Component.empty().append(CHAT_TAG_COMPONENT).append(Component.literal("Only fixed range are accepted").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
            return 1;
        }

        for(int x = rangeX.min().get(); x <= rangeX.max().get(); x++){
            for  (int z = rangeZ.min().get(); z <= rangeZ.max().get(); z++) {
                ChunkPos chunkPos = ChunkPos.containing(new BlockPos(x*16, 0, z*16));

                ProtectedChunkManager.removeProtectedChunk(
                        player.level(),
                        chunkPos
                );
            }
        }

        int chunkXaxis = (Math.abs(rangeX.min().get()) + Math.abs(rangeX.max().get()) );
        int chunkZaxis = (Math.abs(rangeZ.min().get()) + Math.abs(rangeZ.max().get()) );

        player.sendSystemMessage(Component.empty().append(CHAT_TAG_COMPONENT)
                .append(Component.literal("Chunk from ["+rangeX.min().get()+";"+rangeZ.min().get()+"] to ["+rangeX.max().get()+";"+rangeZ.max().get()+"] removed")).append("\n")
                .append(Component.literal("Total Chunk affected : "+ (chunkXaxis*chunkZaxis) )));

        return 1;
    }

    private static int listChunks(CommandContext<CommandSourceStack> commandContext) {

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
    }
}
