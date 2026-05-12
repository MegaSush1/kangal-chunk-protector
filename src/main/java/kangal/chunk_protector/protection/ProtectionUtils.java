package kangal.chunk_protector.protection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

public class ProtectionUtils {

    // check if player can modify block at position
    public static final boolean canModify(Player player, BlockPos pos){

        ChunkPos chunkPos = ChunkPos.containing(pos);

        if (ProtectedChunkManager.isProtectedChunk(player.level(), chunkPos)) {

            // add admin bypass

            return false;
        }

        return true;
    }

    // Can
    public static final boolean canAttack(Player attacker, Entity target){

        BlockPos target_blockPos = target.getOnPos();
        ChunkPos targetChunk = ChunkPos.containing(target_blockPos);

        //can't attack entities inside protect chunk
        if (ProtectedChunkManager.isProtectedChunk(target.level(), targetChunk)) {
            return false;
        }

        BlockPos attacker_blockPos = attacker.getOnPos();
        ChunkPos attackerChunk = ChunkPos.containing(attacker_blockPos);

        if (ProtectedChunkManager.isProtectedChunk(attacker.level(), attackerChunk)) {
            return false;
        }

        return true;

    }

    public static boolean canDamage (Entity entity, DamageSource damageSource){

        BlockPos victim_blockPos = entity.getOnPos();
        ChunkPos victimChunk = ChunkPos.containing(victim_blockPos);

        if (ProtectedChunkManager.isProtectedChunk(entity.level(), victimChunk)) {
            return false;
        }

        Entity attacker = damageSource.getEntity();

        if (attacker != null){

            BlockPos attacker_blockPos = entity.getOnPos();
            ChunkPos attackerChunk = ChunkPos.containing(attacker_blockPos);

            if (ProtectedChunkManager.isProtectedChunk(entity.level(), attackerChunk)) {
                return false;
            }

        }

        return true;
    }

}
