package se.mickelus.tetra.gui.impl.statbar.getter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ITooltipGetter {
    public String getTooltip(EntityPlayer player, ItemStack itemStack);
}
