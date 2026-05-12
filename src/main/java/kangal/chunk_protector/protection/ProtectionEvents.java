package kangal.chunk_protector.protection;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;

import static kangal.chunk_protector.Kangal_chunk_protector.MOD_ID;
import static kangal.chunk_protector.Kangal_chunk_protector.MOD_LOGGER;

public class ProtectionEvents {

    public static void register(){

        // BLOCK BREAK
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity)->{
            MOD_LOGGER.info(MOD_ID + ": BlockBreakEvents.BEFORE");
            return ProtectionUtils.canModify(player, pos);
        });

        // BLOCK PLACE
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) ->{
            MOD_LOGGER.info(MOD_ID + ": UseBlockCallback.EVENT");

            if(!ProtectionUtils.canModify(player, blockHitResult.getBlockPos())) {
                return InteractionResult.FAIL;
            }

            return InteractionResult.PASS;
        });

        // PLAYER ATTACK ENTITY
        AttackEntityCallback.EVENT.register((player, level, interactionHand, entity, entityHitResult) -> {
            MOD_LOGGER.info(MOD_ID + ": AttackEntityCallback.EVENT");

            if (!ProtectionUtils.canAttack(player, entity)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });

        // ENTITY DAMAGE
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((livingEntity, damageSource, v) ->{
            MOD_LOGGER.info(MOD_ID + ": ServerLivingEntityEvents.ALLOW_DAMAGE");
            return ProtectionUtils.canDamage(livingEntity, damageSource);
        });

    }
}
