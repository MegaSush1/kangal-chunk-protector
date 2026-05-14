package kangal.chunk_protector.protection;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.InteractionResult;

public class ProtectionEvents {

    public static void register() {

        /*
         * BLOCK BREAK
         */
        PlayerBlockBreakEvents.BEFORE.register(
                (level, player, pos, state, blockEntity) -> {

                    if (!(player instanceof ServerPlayer serverPlayer)) {
                        return true;
                    }

                    return ProtectionUtils.canModify(
                            serverPlayer,
                            pos
                    );
                }
        );

        /*
         * BLOCK PLACE
         */
        UseBlockCallback.EVENT.register(
                (player, level, hand, hitResult) -> {

                    if (!(player instanceof ServerPlayer serverPlayer)) {
                        return InteractionResult.PASS;
                    }

                    if (!ProtectionUtils.canModify(
                            serverPlayer,
                            hitResult.getBlockPos()
                    )) {

                        return InteractionResult.FAIL;
                    }

                    return InteractionResult.PASS;
                }
        );

        /*
         * PLAYER ATTACK ENTITY
         */
        AttackEntityCallback.EVENT.register(
                (player, level, hand, entity, hitResult) -> {

                    if (!(player instanceof ServerPlayer serverPlayer)) {
                        return InteractionResult.PASS;
                    }

                    if (!ProtectionUtils.canAttack(
                            serverPlayer,
                            entity
                    )) {

                        return InteractionResult.FAIL;
                    }

                    return InteractionResult.PASS;
                }
        );

        /*
         * ENTITY DAMAGE
         */
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(
                (entity, source, amount) ->

                        ProtectionUtils.canDamage(
                                entity,
                                source
                        )
        );
    }
}