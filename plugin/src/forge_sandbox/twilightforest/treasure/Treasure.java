/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge_sandbox.twilightforest.treasure;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

/**
 *
 * @author shadow
 */
public interface Treasure {
    public List<ItemStack> getLoots(Random random, Location location);
}
