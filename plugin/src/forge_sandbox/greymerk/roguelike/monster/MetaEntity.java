package forge_sandbox.greymerk.roguelike.monster;

//import net.minecraft.entity.Entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.monster.EntityZombie;
//import net.minecraft.inventory.EntityEquipmentSlot;
//import net.minecraft.item.ItemStack;

public class MetaEntity implements IEntity {

	private Entity mob;
	
	public MetaEntity(Entity mob){
		this.mob = mob;
	}
	
	@Override
	public void setSlot(EquipmentSlot slot, ItemStack item) {
//		mob.setItemStackToSlot(slot, item);
                if(mob instanceof LivingEntity) {
                    LivingEntity tmp = (LivingEntity) mob;
                    EntityEquipment ee = tmp.getEquipment();
                    if(ee == null) return;
                    if(null != slot) switch (slot) {
                        case OFF_HAND:
                            ee.setItemInOffHand(item);
                            break;
                        case HAND:
                            ee.setItemInMainHand(item);
                            break;
                        case CHEST:
                            ee.setChestplate(item);
                            break;
                        case FEET:
                            ee.setBoots(item);
                            break;
                        case HEAD:
                            ee.setHelmet(item);
                            break;
                        case LEGS:
                            ee.setLeggings(item);
                            break;
                        default:
                            break;
                    }
                }
	}

	@Override
	public void setMobClass(MobType type, boolean clear) {
		
		LivingEntity oldMob = (LivingEntity)this.mob;
		LivingEntity newMob = (LivingEntity)MobType.getEntity(this.mob.getWorld(), this.mob.getLocation(), type);
		
		this.mob = (Entity)newMob;
		
		if(newMob instanceof Zombie){
			((Zombie)newMob).setBaby(((Zombie)oldMob).isBaby());
		}
		
                EntityEquipment nee = newMob.getEquipment();
                EntityEquipment ee = oldMob.getEquipment();
                if(nee != null && ee != null) {
                    nee.setItemInMainHand(ee.getItemInMainHand());
                    nee.setItemInOffHand(ee.getItemInOffHand());
                    nee.setBoots(ee.getBoots());
                    nee.setLeggings(ee.getLeggings());
                    nee.setChestplate(ee.getChestplate());
                    nee.setHelmet(ee.getHelmet());
                }
		
                oldMob.remove();
	}

	@Override
	public void setChild(boolean child) {
		if(!(this.mob instanceof Zombie)) return;
		
		((Zombie)this.mob).setBaby(child);
	}

	@Override
	public boolean instance(Class<?> type) {
		return type.isInstance(this.mob);
	}

	@Override
	public void setName(String name) {
		this.mob.setCustomName(name);
		this.mob.setCustomNameVisible(true);
	}

}
