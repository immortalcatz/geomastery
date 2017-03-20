package com.jayavery.jjmod.items;

import com.jayavery.jjmod.entities.projectile.EntitySpearWood;
import com.jayavery.jjmod.utilities.EquipMaterial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/** Wood spear tool item. */
public class ItemSpearWood extends ItemSpearAbstract {

    public ItemSpearWood() {

        super("spear_wood", EquipMaterial.WOOD_TOOL);
    }

    @Override
    protected void throwSpear(World world, EntityPlayer player, float velocity,
            int damage) {

        EntitySpearWood thrown = new EntitySpearWood(world, player, damage);
        thrown.setAim(player, player.rotationPitch,
                player.rotationYaw, 0.0F, velocity, 1.0F);
        world.spawnEntity(thrown);
    }
}