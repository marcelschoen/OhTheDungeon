package forge_sandbox.greymerk.roguelike.worldgen.spawners;

import com.google.gson.JsonObject;
import forge_sandbox.greymerk.roguelike.treasure.loot.Equipment;
import forge_sandbox.greymerk.roguelike.treasure.loot.Quality;
import otd.config.WorldConfig;
import otd.util.nbt.JsonToNBT;

import java.util.Random;

public class SpawnPotential {

    String name;
    int weight;
    boolean equip;
    Object nbt;


    public SpawnPotential(String name) {
        this(name, 1);
    }

    public SpawnPotential(String name, int weight) {
        this(name, true, weight, null);
    }

    public SpawnPotential(String name, boolean equip, int weight) {
        this(name, equip, weight, null);
    }

    public SpawnPotential(String name, boolean equip, int weight, Object nbt) {
        this.name = name;
        this.equip = equip;
        this.weight = weight;
        this.nbt = nbt;
    }

    public SpawnPotential(JsonObject entry) throws Exception {
        this.weight = entry.has("weight") ? entry.get("weight").getAsInt() : 1;
        if (!entry.has("name")) {
            throw new Exception("Spawn potential missing name");
        }

        this.name = entry.get("name").getAsString();
        this.equip = entry.has("equip") ? entry.get("equip").getAsBoolean() : false;

        if (entry.has("nbt")) {
            String metadata = entry.get("nbt").getAsString();
            this.nbt = JsonToNBT.getTagFromJson(metadata);
        }
    }

    private static class GetNBTTagCompound117R1 {
        public Object get(int level, String name, Object inbt, SpawnPotential sp) {
            Object nbt;
            if (inbt == null) {
                nbt = new net.minecraft.nbt.NBTTagCompound();
            } else {
                nbt = ((net.minecraft.nbt.NBTTagCompound) inbt).clone();
            }
            return sp.getPotential(sp.getRoguelike(level, name, nbt));
        }
    }

    public Object getNBTTagCompound(int level) {
        return (new GetNBTTagCompound117R1()).get(level, name, this.nbt, this);
    }

    private static class GetNBTTagList117R1 {
        public Object get(Random rand, int level, SpawnPotential sp) {
            net.minecraft.nbt.NBTTagList potentials = new net.minecraft.nbt.NBTTagList();
            if (sp.name.equals(Spawner.getName(Spawner.ZOMBIE))) {
                for (int i = 0; i < 24; ++i) {


                    net.minecraft.nbt.NBTTagCompound mob = new net.minecraft.nbt.NBTTagCompound();
                    mob = (net.minecraft.nbt.NBTTagCompound) sp.getRoguelike(level, sp.name, mob);

                    Equipment tool;
                    switch (rand.nextInt(3)) {
                        case 0:
                            tool = Equipment.SHOVEL;
                            break;
                        case 1:
                            tool = Equipment.AXE;
                            break;
                        case 2:
                            tool = Equipment.PICK;
                            break;
                        default:
                            tool = Equipment.PICK;
                            break;
                    }

                    mob = (net.minecraft.nbt.NBTTagCompound) sp.equipHands(mob, Equipment.getName(tool, Quality.getToolQuality(rand, level)), "minecraft:shield");
                    mob = (net.minecraft.nbt.NBTTagCompound) sp.equipArmour(mob, rand, level);

                    potentials.add((net.minecraft.nbt.NBTBase)
                            sp.getPotential(mob));
                }

                return potentials;
            }

            if (sp.name.equals(Spawner.getName(Spawner.SKELETON))) {
                for (int i = 0; i < 12; ++i) {
                    net.minecraft.nbt.NBTTagCompound mob = new net.minecraft.nbt.NBTTagCompound();
                    mob = (net.minecraft.nbt.NBTTagCompound) sp.getRoguelike(level, sp.name, mob);
                    mob = (net.minecraft.nbt.NBTTagCompound) sp.equipHands(mob, "minecraft:bow", null);
                    mob = (net.minecraft.nbt.NBTTagCompound) sp.equipArmour(mob, rand, level);
                    potentials.add((net.minecraft.nbt.NBTBase)
                            sp.getPotential(mob));
                }

                return potentials;
            }

            potentials.add((net.minecraft.nbt.NBTBase)
                    sp.getPotential(sp.getRoguelike(level, sp.name, new net.minecraft.nbt.NBTTagCompound())));
            return potentials;
        }
    }

    public Object getNBTTagList(Random rand, int level) {
        return (new GetNBTTagList117R1()).get(rand, level, this);
    }

    private Object getPotential(Object mob) {
        net.minecraft.nbt.NBTTagCompound potential =
                new net.minecraft.nbt.NBTTagCompound();
        potential.set("Entity", (net.minecraft.nbt.NBTBase) mob);
        potential.setInt("Weight", this.weight);
        return potential;
    }

    private static class EquipHands117R1 {
        public Object get(Object mob, String weapon, String offhand, SpawnPotential sp) {
            net.minecraft.nbt.NBTTagList hands =
                    new net.minecraft.nbt.NBTTagList();
            hands.add((net.minecraft.nbt.NBTBase) sp.getItem(weapon));
            hands.add((net.minecraft.nbt.NBTBase) sp.getItem(offhand));
            ((net.minecraft.nbt.NBTTagCompound) mob).set("HandItems", hands);
            return mob;
        }
    }

    private Object equipHands(Object mob, String weapon, String offhand) {
        return (new EquipHands117R1()).get(mob, weapon, offhand, this);
    }

    private static class EquipArmour117R1 {
        public Object get(Object mob, Random rand, int level, SpawnPotential sp) {

            net.minecraft.nbt.NBTTagList armour = new net.minecraft.nbt.NBTTagList();
            armour.add((net.minecraft.nbt.NBTBase) sp.getItem(Equipment.getName(Equipment.FEET, Quality.getArmourQuality(rand, level))));
            armour.add((net.minecraft.nbt.NBTBase) sp.getItem(Equipment.getName(Equipment.LEGS, Quality.getArmourQuality(rand, level))));
            armour.add((net.minecraft.nbt.NBTBase) sp.getItem(Equipment.getName(Equipment.CHEST, Quality.getArmourQuality(rand, level))));
            armour.add((net.minecraft.nbt.NBTBase) sp.getItem(Equipment.getName(Equipment.HELMET, Quality.getArmourQuality(rand, level))));
            ((net.minecraft.nbt.NBTTagCompound) mob).set("ArmorItems", armour);

            return mob;
        }
    }

    private Object equipArmour(Object mob, Random rand, int level) {
        return (new EquipArmour117R1()).get(mob, rand, level, this);
    }

    private static class GetItem117R1 {
        public Object get(String itemName) {
            net.minecraft.nbt.NBTTagCompound item =
                    new net.minecraft.nbt.NBTTagCompound();
            if (itemName == null) return item;
            item.setString("id", itemName);
            item.setInt("Count", 1);
            return item;
        }
    }

    private Object getItem(String itemName) {
        return (new GetItem117R1()).get(itemName);
    }

    private static class GetRoguelike117R1 {
        public Object get(int level, String type, Object otag, SpawnPotential sp) {
            net.minecraft.nbt.NBTTagCompound tag = (net.minecraft.nbt.NBTTagCompound) otag;
            tag.setString("id", type);

            if (!(WorldConfig.wc.rogueSpawners
                    && sp.equip)) return tag;

            net.minecraft.nbt.NBTTagList activeEffects = new net.minecraft.nbt.NBTTagList();
            tag.set("ActiveEffects", activeEffects);

            net.minecraft.nbt.NBTTagCompound buff = new net.minecraft.nbt.NBTTagCompound();
            activeEffects.add(buff);

            buff.setByte("Id", (byte) 4);
            buff.setByte("Amplifier", (byte) level);
            buff.setInt("Duration", 10);
            buff.setByte("Ambient", (byte) 0);

            return tag;
        }
    }

    private Object getRoguelike(int level, String type, Object otag) {
        return (new GetRoguelike117R1()).get(level, type, otag, this);
    }

}
