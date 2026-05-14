package kangal.chunk_protector.mixin;

import kangal.chunk_protector.protection.ProtectionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(
            method = "canPlace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chunk_protector$preventPlacement(
            BlockPlaceContext placeContext, BlockState stateForPlacement, CallbackInfoReturnable<Boolean> cir
    ){

        // CHECK SERVER AND PLAYER
        if (!(placeContext.getLevel() instanceof ServerLevel level)) return;
        if (!(placeContext.getPlayer() instanceof ServerPlayer player)) return;

        BlockPos placePos = placeContext.getClickedPos().relative(placeContext.getClickedFace());

        if (!ProtectionUtils.canModify(player, placePos)){

            player.inventoryMenu.sendAllDataToRemote();

            cir.setReturnValue(false);

        }

    }
}
