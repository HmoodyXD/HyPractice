package rip.crystal.practice.essentials.abilities.impl;

import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class Rocket extends Ability {

    private HashSet<Player> nofall;

    public Rocket() {
        super("ROCKET");
    }

    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }

            if (profile.getRocket().onCooldown(player)) {
                player.sendMessage(CC.translate("&7You are on &4&lRocket &4cooldown for &4" + DurationFormatter.getRemaining(profile.getRocket().getRemainingMilis(player), true, true)));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (profile.getPartneritem().onCooldown(player)) {
                player.sendMessage(CC.translate("&7You are on &4&lPartner Item &4cooldown &7for &4" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (isAbility(player.getItemInHand())) {
                player.setVelocity(new Vector(0.1D, 2.0D, 0.0D));

                PlayerUtil.decrement(player);

                profile.getRocket().applyCooldown(player,  60 * 1000);
                profile.getPartneritem().applyCooldown(player, 10 * 1000);
                player.setMetadata("rocket", new FixedMetadataValue(cPractice.get(), true));
            }
        }
    }

    @EventHandler
    public void fallDamage(final EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player)event.getEntity();
            Profile profile = Profile.get(player.getUniqueId());
            if (profile.getRocket().onCooldown(player)) {
                event.setCancelled(true);
            }
        }
    }
}