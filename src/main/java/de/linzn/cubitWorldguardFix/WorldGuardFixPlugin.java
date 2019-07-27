/*
 *  Copyright (C) 2019. MineGaming - All Rights Reserved
 *  You may use, distribute and modify this code under the
 *  terms of the LGPLv3 license, which unfortunately won't be
 *  written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.cubitWorldguardFix;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardFixPlugin extends JavaPlugin {

    @Override
    public void onDisable() {
        this.getLogger().info("Disable cubit-worldguard-fix");
        HandlerList.unregisterAll(this);

    }

    @Override
    public void onEnable() {
        this.getLogger().info("Enable cubit-worldguard-fix");
        this.getServer().getPluginManager().registerEvents(new PistonCoreListener(), this);
    }

}
