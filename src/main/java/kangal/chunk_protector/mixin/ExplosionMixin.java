package kangal.chunk_protector.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import kangal.chunk_protector.protection.ProtectedChunkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ServerExplosion.class)
public abstract class ExplosionMixin {

    @Final
    @Shadow private ServerLevel level;

    @Shadow
    protected abstract List<BlockPos> calculateExplodedPositions();

    @ModifyExpressionValue(
            method = "explode()I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ServerExplosion;calculateExplodedPositions()Ljava/util/List;")
    )
    private List<BlockPos> chunk_protector$cancelExplosionInProtectedArea(List<BlockPos> original) {

        original.removeIf(blockPos -> ProtectedChunkManager.isProtectedChunk(level, ChunkPos.containing(blockPos)));

        return original;
    }

}
