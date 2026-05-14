package kangal.chunk_protector.mixin;

import kangal.chunk_protector.protection.ProtectedChunkManager;
import kangal.chunk_protector.protection.ProtectionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PistonStructureResolver.class)
public class PistonMixin {

    @Shadow
    private Level level;

    @Shadow
    private Direction pushDirection;

    @Shadow
    private BlockPos startPos;

    @Shadow
    @Final
    private List<BlockPos> toPush;

    @Inject(
            method = "resolve",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chunk_protector$cancelPistonIfProtected( CallbackInfoReturnable<Boolean> cir ){

        if (!(level instanceof ServerLevel serverLevel)) return;

        Direction direction = this.pushDirection;

        for (BlockPos blockPos : toPush) {
            BlockPos target = blockPos.relative(direction);

            ChunkPos chunkPos = ChunkPos.containing(target);
            ChunkPos chunkPos1 = ChunkPos.containing(startPos);

            if (ProtectedChunkManager.isProtectedChunk(serverLevel,chunkPos) ||
            ProtectedChunkManager.isProtectedChunk(serverLevel,chunkPos1)) {

                cir.setReturnValue(false);
                return;
            }

        }

    }

}
