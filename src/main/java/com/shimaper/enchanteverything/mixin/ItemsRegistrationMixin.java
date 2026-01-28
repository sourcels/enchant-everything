package com.shimaper.enchanteverything.mixin;

import com.shimaper.enchanteverything.EnchantEverything;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.lang.reflect.Field;

@Mixin(Items.class)
public class ItemsRegistrationMixin {
    private static Field componentsField;

    static {
        for (Field f : Item.Properties.class.getDeclaredFields()) {
            if (f.getType() == DataComponentMap.Builder.class) {
                componentsField = f;
                componentsField.setAccessible(true);
                break;
            }
        }
    }

    @ModifyVariable(
            method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static Item.Properties modifyProperties(Item.Properties properties) {
        if (componentsField == null) return properties;

        try {
            var builder = (DataComponentMap.Builder) componentsField.get(properties);

            boolean hasEnchantability = builder.build().has(DataComponents.ENCHANTABLE);

            if (!hasEnchantability) {
                properties.enchantable(EnchantEverything.enchantabilityValue);
            }

        } catch (Exception e) {
            EnchantEverything.LOGGER.error("Error modifying item properties: ", e);
        }

        return properties;
    }
}