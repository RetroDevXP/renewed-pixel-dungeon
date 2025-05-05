package com.retrodevxp.pixeldungeon.items.weapon.enchantments;

import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Slow;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Slowness extends Weapon.Enchantment {
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x0044FF);
    private static final String TXT_CHILLING = "chilling %s";

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 4) < 3) {
            return false;
        }
        Buff.prolong(defender, Slow.class, Random.Float(1.0f, level + 2) * Slow.durationFactor(defender));
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }

    @Override
    public String name(String weaponName) {
        return Utils.format(TXT_CHILLING, weaponName);
    }
}