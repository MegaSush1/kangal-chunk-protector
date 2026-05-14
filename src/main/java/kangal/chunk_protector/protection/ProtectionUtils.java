package kangal.chunk_protector.protection;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

import static kangal.chunk_protector.Kangal_chunk_protector.MOD_LOGGER;

public class ProtectionUtils {

    // check if player can modify block at position
    public static final boolean canModify(ServerPlayer player, BlockPos pos){

        ChunkPos chunkPos = ChunkPos.containing(pos);

        if (ProtectedChunkManager.isProtectedChunk(player.level(), chunkPos)) {

            return bypass(player);
        }

        return true;
    }

    // Can
    public static final boolean canAttack(ServerPlayer attacker, Entity target){

        BlockPos target_blockPos = target.getOnPos();
        ChunkPos targetChunk = ChunkPos.containing(target_blockPos);

        //can't attack entities inside protect chunk
        if (ProtectedChunkManager.isProtectedChunk((ServerLevel) target.level(), targetChunk)) {
            return false;
        }

        BlockPos attacker_blockPos = attacker.getOnPos();
        ChunkPos attackerChunk = ChunkPos.containing(attacker_blockPos);

        if (ProtectedChunkManager.isProtectedChunk(attacker.level(), attackerChunk)) {
            return bypass(attacker);
        }

        return true;

    }

    public static boolean canDamage (Entity entity, DamageSource damageSource){

        BlockPos victim_blockPos = entity.getOnPos();
        ChunkPos victimChunk = ChunkPos.containing(victim_blockPos);

        if (ProtectedChunkManager.isProtectedChunk((ServerLevel) entity.level(), victimChunk)) {
            return false;
        }

        Entity attacker = damageSource.getEntity();

        if (attacker != null){

            BlockPos attacker_blockPos = entity.getOnPos();
            ChunkPos attackerChunk = ChunkPos.containing(attacker_blockPos);

            if (ProtectedChunkManager.isProtectedChunk((ServerLevel) entity.level(), attackerChunk)) {
                return false;
            }

        }

        return true;
    }

    public static boolean bypass(ServerPlayer player){
        MOD_LOGGER.info("Entering bypass function");
        if (player instanceof ServerPlayer) {
            //boolean permission = player.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS));
            boolean permission = false;
            MOD_LOGGER.info("Bypassing protection permissions : "+permission);
            return permission;
        }
        return false;
    }

}
