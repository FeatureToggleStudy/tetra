package se.mickelus.tetra.blocks.workbench.action;


import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.module.data.CapabilityData;

/**
 * ConfigActions are a variant of workbench actions which can be defined using configuration files.  Loot action is
 * currently the only action type available, the provided item will be broken and rewards from the loot table will be
 * awarded to the player.
 */
public abstract class ConfigAction implements WorkbenchAction {

    /**
     * The identifier for the action, this has to be unique.
     * The key suffixed with ".label" is the localization entry used for the action label displayed in the UI.
     * E.g: break_geode_action.label=Break open
     */
    public String key;

    /**
     * Defines if this action can be performed on a given itemstack, if the itemstack matches the predicate then
     * the action is allowed (if the capability requirements are met)
     */
    public ItemPredicate requirement;

    /**
     * Defines which capabilities are required for this action, a map where the capability is the key and the value
     * is the required level.
     *
     * Json format:
     * {
     *     "capabilityA": level,
     *     "capabilityB": level
     * }
     */
    public CapabilityData requiredCapabilities = new CapabilityData();

    /**
     * The loot table that rewards will be picked from when the action is performed.
     */
    public ResourceLocation lootTable;
}
