package kangal.chunk_protector.mixin;

import kangal.chunk_protector.protection.ProtectedChunkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chunk_protector$preventBucketUse(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir
    ){

        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ItemStack itemStack = (ItemStack)(Object)this;

        if (!(itemStack.getItem() instanceof BucketItem)) return;

        BlockHitResult blockHitResult = (BlockHitResult) player.pick(5.0D, 0.0F, false);

        BlockPos placePos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());

        if (!ProtectedChunkManager.isProtectedChunk((ServerLevel) level, ChunkPos.containing(placePos))) return;

        player.inventoryMenu.sendAllDataToRemote();
        cir.setReturnValue(InteractionResult.FAIL);

    }

}
