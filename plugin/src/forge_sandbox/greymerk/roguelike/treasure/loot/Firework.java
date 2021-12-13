package forge_sandbox.greymerk.roguelike.treasure.loot;

import forge_sandbox.greymerk.roguelike.util.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Firework {

    private static class Firework117R1 {
        public ItemStack get(Random rand, int stackSize) {
            ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET, stackSize);
            net.minecraft.nbt.NBTTagCompound tag = new net.minecraft.nbt.NBTTagCompound();
            net.minecraft.nbt.NBTTagCompound fireworks = new net.minecraft.nbt.NBTTagCompound();

            fireworks.setByte("Flight", (byte) (rand.nextInt(3) + 1));

            net.minecraft.nbt.NBTTagList explosion = new net.minecraft.nbt.NBTTagList();

            net.minecraft.nbt.NBTTagCompound properties = new net.minecraft.nbt.NBTTagCompound();
            properties.setByte("Flicker", (byte) (rand.nextBoolean() ? 1 : 0));
            properties.setByte("Trail", (byte) (rand.nextBoolean() ? 1 : 0));
            properties.setByte("Type", (byte) (rand.nextInt(5)));

            int size = rand.nextInt(4) + 1;
            int[] colorArr = new int[size];
            for(int i = 0; i < size; ++i){
                colorArr[i] = DyeColor.HSLToColor(rand.nextFloat(), (float)1.0, (float)0.5);
            }

            net.minecraft.nbt.NBTTagIntArray colors = new net.minecraft.nbt.NBTTagIntArray(colorArr);
            properties.set("Colors", colors);

            explosion.add(properties);
            fireworks.set("Explosions", explosion);
            tag.set("Fireworks", fireworks);

            net.minecraft.world.item.ItemStack tmp = org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asNMSCopy(rocket);
            tmp.setTag(tag);
            rocket = org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asBukkitCopy(tmp);
            
            return rocket;
        }
    }

    public static ItemStack get(Random rand, int stackSize) {
        return (new Firework117R1()).get(rand, stackSize);
    }
}
