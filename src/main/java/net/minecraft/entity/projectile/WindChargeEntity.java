package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Thrown Wind Charge projectile. Flies in a straight line (no gravity) and, on impact,
 * pushes nearby entities outward instead of dealing fire/explosion damage.
 */
public class WindChargeEntity extends ProjectileItemEntity {

    private static final byte WIND_BURST_STATUS = 24;
    private static final double BURST_RADIUS = 3.0D;

    public WindChargeEntity(EntityType<? extends WindChargeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public WindChargeEntity(World worldIn, LivingEntity throwerIn) {
        super(EntityType.WIND_CHARGE, throwerIn, worldIn);
    }

    public WindChargeEntity(World worldIn, double x, double y, double z) {
        super(EntityType.WIND_CHARGE, x, y, z, worldIn);
    }

    protected Item func_213885_i() {
        return Items.WIND_CHARGE;
    }

    protected float getGravityVelocity() {
        return 0.0F;
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity hit = ((EntityRayTraceResult) result).getEntity();
                Vec3d motion = this.getMotion();
                hit.addVelocity(motion.x * 0.4D, 0.2D, motion.z * 0.4D);
            }

            this.burst();
            this.world.setEntityState(this, WIND_BURST_STATUS);
            this.remove();
        }
    }

    private void burst() {
        AxisAlignedBB area = this.getBoundingBox().grow(BURST_RADIUS);
        List<Entity> nearby = this.world.getEntitiesInAABBexcluding(this, area,
                (e) -> !e.isSpectator() && e.canBeCollidedWith());

        for (Entity entity : nearby) {
            double dx = entity.posX - this.posX;
            double dy = entity.posY + (double) entity.getHeight() * 0.5D - this.posY;
            double dz = entity.posZ - this.posZ;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > BURST_RADIUS || dist < 1.0E-4D) {
                continue;
            }

            double falloff = 1.0D - (dist / BURST_RADIUS);
            double power = falloff * 0.6D;
            entity.addVelocity((dx / dist) * power, (dy / dist) * power + falloff * 0.15D, (dz / dist) * power);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == WIND_BURST_STATUS) {
            this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ITEM_ELYTRA_FLYING,
                    SoundCategory.NEUTRAL, 0.8F, 1.6F, false);

            for (int i = 0; i < 10; ++i) {
                double ox = this.rand.nextGaussian() * 0.3D;
                double oy = this.rand.nextGaussian() * 0.3D;
                double oz = this.rand.nextGaussian() * 0.3D;
                this.world.addParticle(ParticleTypes.CLOUD, this.posX + ox * 0.5D, this.posY + oy * 0.5D,
                        this.posZ + oz * 0.5D, ox, oy, oz);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public void tick() {
        super.tick();
        if (this.world.isRemote && this.ticksExisted % 2 == 0) {
            Vec3d motion = this.getMotion();
            this.world.addParticle(ParticleTypes.CLOUD, this.posX - motion.x * 0.25D, this.posY - motion.y * 0.25D,
                    this.posZ - motion.z * 0.25D, 0.0D, 0.0D, 0.0D);
        }
    }
}
