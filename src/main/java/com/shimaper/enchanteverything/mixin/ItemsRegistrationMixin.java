package com.shimaper.enchanteverything.mixin;

import com.shimaper.enchanteverything.EnchantEverything;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Items.class)
public class ItemsRegistrationMixin {
    @ModifyVariable(
            method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private static Item.Properties modifyProperties(Item.Properties properties) {
        try {
            var field = Item.Properties.class.getDeclaredField("enchantable");
            field.setAccessible(true);
            int currentValue = field.getInt(properties);

            if (currentValue == 0) {
                properties.enchantable(EnchantEverything.enchantabilityValue);
                EnchantEverything.LOGGER.debug("Added enchantability to item properties" + properties);
            }
        } catch (Exception e) {
            EnchantEverything.LOGGER.warn("Failed to modify item properties: {}", e.getMessage());
        }

        return properties;
    }
}