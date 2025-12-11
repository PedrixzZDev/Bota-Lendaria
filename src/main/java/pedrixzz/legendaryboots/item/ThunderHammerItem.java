package pedrixzz.legendaryboots.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class ThunderHammerItem extends Item {
    public ThunderHammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        // CORREÇÃO: Adicionado ()
        if (!world.isClient()) {
            Vec3d start = player.getEyePos();
            Vec3d end = start.add(player.getRotationVector().multiply(50));
            BlockHitResult hit = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
            
            if (hit.getType() == HitResult.Type.BLOCK && world instanceof ServerWorld serverWorld) {
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(serverWorld, null, hit.getBlockPos(), SpawnReason.TRIGGERED, true, true);
                
                if (lightning != null) {
                    lightning.refreshPositionAfterTeleport(hit.getPos());
                    world.spawnEntity(lightning);
                }
                
                // CORREÇÃO: Passando o ItemStack
                player.getItemCooldownManager().set(player.getStackInHand(hand), 100);
            }
        }
        return ActionResult.SUCCESS;
    }
}