package de.linzn.cubitWorldguardFix;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PistonCoreListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        System.out.println("onBlockPistonRetract");
        if (event.isSticky()) {

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        System.out.println("onBlockPistonExtend");
        final Block pistonBlock = event.getBlock();
        final List<Block> affectedBlocks = event.getBlocks();
        final List<Location> locs = new LinkedList<>();
        final BlockFace dir = event.getDirection();
        final Block extensionBlock = pistonBlock.getRelative(dir);
        locs.add(extensionBlock.getLocation());
        final int bSize;
        if (affectedBlocks == null) bSize = 0;
        else bSize = affectedBlocks.size();
        if (bSize > 0) {
            for (Block block : affectedBlocks) {
                locs.add(block.getLocation());
            }
        }
        final Block endBlock;
        if (bSize > 0) {
            endBlock = pistonBlock.getRelative(dir, bSize + 1);
            locs.add(endBlock.getLocation());
        }
        if (checkOwner(pistonBlock.getLocation(), locs)) {
            event.setCancelled(false);
            System.out.println("onBlockPistonExtend allow");
        } else {
            event.setCancelled(true);
            System.out.println("onBlockPistonExtend deny");
        }
    }

    private boolean checkOwner(final Location refLoc, final List<Location> locs) {
        final WorldGuardPlugin wg = WorldGuardPlugin.inst();
        if (wg == null) return false;
        final RegionManager mg = wg.getRegionManager(refLoc.getWorld());
        ApplicableRegionSet pistonRegions = mg.getApplicableRegions(refLoc);
        ProtectedRegion cubitRegion = null;
        for (ProtectedRegion region : pistonRegions.getRegions()) {
            //if (Cubit.isregion...){
            cubitRegion = region;
            //}
        }
        List<ProtectedRegion> regionSet = new ArrayList<>();

        for (Location loc : locs) {
            for (ProtectedRegion region : mg.getApplicableRegions(loc).getRegions()) {
                //if (Cubit.isregion...){
                regionSet.add(region);
                //}
            }
        }
        if (cubitRegion != null && !cubitRegion.getOwners().getUniqueIds().isEmpty()) {
            UUID owner = new ArrayList<>(cubitRegion.getOwners().getUniqueIds()).get(0);
            for (ProtectedRegion region : regionSet) {
                if (!region.getOwners().contains(owner)) {
                    return false;
                }
            }
        } else {
            return regionSet.isEmpty();
        }
        return true;
    }

}
