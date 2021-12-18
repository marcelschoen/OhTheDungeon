package forge_sandbox.greymerk.roguelike.treasure.loot;

import forge_sandbox.greymerk.roguelike.util.DyeColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Firework {

    public static ItemStack get(Random rand, int stackSize) {
        List<Color> colors = new ArrayList<>();
        int size = rand.nextInt(4) + 1;
        for(int i = 0; i < size; ++i){
            colors.add(Color.fromRGB(DyeColor.HSLToColor(rand.nextFloat(), (float)1.0, (float)0.5)));
        }

        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        switch(rand.nextInt(4)) {
            case 0:
                type = FireworkEffect.Type.BALL_LARGE;
            case 1:
                type = FireworkEffect.Type.BURST;
            case 2:
                type = FireworkEffect.Type.CREEPER;
            case 3:
                type = FireworkEffect.Type.STAR;
        }
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(colors)
                .trail(rand.nextBoolean())
                .flicker(rand.nextBoolean())
                .with(type)
                .build();
        ItemStack charge = new ItemStack(Material.FIREWORK_ROCKET, stackSize);
        FireworkEffectMeta meta = (FireworkEffectMeta) charge.getItemMeta();
        meta.setEffect(effect);
        charge.setItemMeta(meta);

        return charge;
    }
}
