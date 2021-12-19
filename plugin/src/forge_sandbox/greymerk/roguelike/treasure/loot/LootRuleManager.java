package forge_sandbox.greymerk.roguelike.treasure.loot;

import forge_sandbox.greymerk.roguelike.treasure.Treasure;
import forge_sandbox.greymerk.roguelike.treasure.TreasureManager;
import forge_sandbox.greymerk.roguelike.util.IWeighted;
import forge_sandbox.greymerk.roguelike.util.WeightedChoice;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootRuleManager {

    private List<LootRule> rules;

    public LootRuleManager() {
        this.rules = new ArrayList<>();
    }

    public LootRuleManager clearRules() {
        this.rules.clear();
        return this;
    }

    public void add(Treasure type, IWeighted<ItemStack> item, int level, boolean toEach, int amount) {
        this.rules.add(new LootRule(type, item, level, toEach, amount));
    }

    public void add(Treasure type, ItemStack item, int level, boolean toEach, int amount) {
        IWeighted<ItemStack> toAdd = new WeightedChoice<>(item, 1);
        this.rules.add(new LootRule(type, toAdd, level, toEach, amount));
    }

    public void add(LootRule toAdd) {
        this.rules.add(toAdd);
    }

    public void add(LootRuleManager other) {
        if (other == null) return;
        this.rules.addAll(other.rules);
    }

    public void process(Random rand, TreasureManager treasure) {
        for (LootRule rule : this.rules) {
            rule.process(rand, treasure);
        }
    }

    @Override
    public String toString() {
        return Integer.toString(this.rules.size());
    }
}
