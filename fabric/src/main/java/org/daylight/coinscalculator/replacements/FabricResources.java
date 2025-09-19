package org.daylight.coinscalculator.replacements;

import net.minecraft.util.Identifier;
import org.daylight.coinscalculator.CoinsCalculator;
import org.daylight.coinscalculator.replacements.api.FabricResourceLocation;

public class FabricResources implements IModResources {
    public static final IResourceLocation SELECTION_DEFAULT_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/selection_default.png"));
    public static final IResourceLocation SELECTION_ACTIVE_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/selection_active.png"));
    public static final IResourceLocation GEAR_ICON_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/gear_icon.png"));
    public static final IResourceLocation CURRENCY_ICON_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/currency_icon.png"));
    public static final IResourceLocation CHECKBOX_NORMAL_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/checkmark_normal.png"));
    public static final IResourceLocation CHECKBOX_SELECTED_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/checkmark_selected.png"));
    public static final IResourceLocation SUM_ICON_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/sum_icon.png"));
    public static final IResourceLocation WEIGHING_MACHINE_ICON_RESOURCE = new FabricResourceLocation(new Identifier(CoinsCalculator.MOD_ID, "textures/gui/weighing_machine_icon.png"));

    @Override
    public IResourceLocation getResourceLocation(Type type) {
        return switch (type) {
            case SELECTION_DEFAULT -> SELECTION_DEFAULT_RESOURCE;
            case SELECTION_ACTIVE -> SELECTION_ACTIVE_RESOURCE;
            case GEAR_ICON -> GEAR_ICON_RESOURCE;
            case CURRENCY_ICON -> CURRENCY_ICON_RESOURCE;
            case CHECKBOX_NORMAL -> CHECKBOX_NORMAL_RESOURCE;
            case CHECKBOX_SELECTED -> CHECKBOX_SELECTED_RESOURCE;
            case SUM_ICON -> SUM_ICON_RESOURCE;
            case WEIGHING_MACHINE_ICON -> WEIGHING_MACHINE_ICON_RESOURCE;
        };
    }
}
