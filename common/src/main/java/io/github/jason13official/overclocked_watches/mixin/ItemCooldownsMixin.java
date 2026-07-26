package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.api.common.data.CoolDownRecord;
import io.github.jason13official.overclocked_watches.api.common.data.IItemCooldowns;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin implements IItemCooldowns {

  @Shadow
  @Final
  private Map<Identifier, ItemCooldowns.CooldownInstance> cooldowns;
  @Shadow
  private int tickCount;

  public ItemCooldownsMixin() {
  }

  @Shadow
  public abstract void addCooldown(Identifier cooldownGroup, int time);

  public List<CoolDownRecord> overclocked_watches$getCooldownTicks() {
    return this.cooldowns.entrySet().stream().map((e) -> {
      AccessorCooldownInstance instance = (AccessorCooldownInstance) (Object) e.getValue();
      Item item = BuiltInRegistries.ITEM.getValue(e.getKey());
      return new CoolDownRecord(item, instance.getEndTime() - this.tickCount, instance.getEndTime() - instance.getStartTime());
    }).toList();
  }

  public void overclocked_watches$addCoolDown(CoolDownRecord cd) {
    Identifier group = BuiltInRegistries.ITEM.getKey(cd.item());
    this.addCooldown(group, 50000);
    AccessorCooldownInstance instance = (AccessorCooldownInstance) (Object) this.cooldowns.get(group);
    int end = this.tickCount + cd.remain();
    int start = end - cd.total();
    instance.setStartTime(start);
    instance.setEndTime(end);
  }
}
