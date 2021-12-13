/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge_sandbox.twilightforest;

import forge_sandbox.twilightforest.treasure.Tower_Library;
import forge_sandbox.twilightforest.treasure.Tower_Room;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

/**
 *
 * @author
 */
public enum TFTreasure {
    tower_room, tower_library, 
    basement, darktower_key, darktower_cache, labyrinth_deadend, labyrinth_room, labyrinth_vault;

    public static List<ItemStack> getLoot(TFTreasure treasure, Random random, Location location) {
        if(treasure == tower_room) {
            return (new Tower_Room()).getLoots(random, location);
        }
        if(treasure == tower_library) {
            return (new Tower_Library()).getLoots(random, location);
        }
        return null;
    }
}
