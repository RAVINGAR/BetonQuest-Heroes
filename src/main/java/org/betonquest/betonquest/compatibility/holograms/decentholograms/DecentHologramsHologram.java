package org.betonquest.betonquest.compatibility.holograms.decentholograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.CustomLog;
import org.betonquest.betonquest.compatibility.holograms.BetonHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"PMD.CommentRequired", "PMD.TooManyMethods"})
@CustomLog
public class DecentHologramsHologram implements BetonHologram {
    private final Hologram hologram;

    public DecentHologramsHologram(final String name, final Location location) {
        String hologramName = name;
        if (DHAPI.getHologram(hologramName) != null) {
            //In the rare case that a hologram is created with a name that already exists...
            hologramName += UUID.randomUUID();
        }
        hologram = DHAPI.createHologram(hologramName, location);
        hologram.enable();
    }

    @Override
    public void appendLine(final ItemStack item) {
        DHAPI.addHologramLine(hologram, item);
    }

    @Override
    public void appendLine(final String text) {
        DHAPI.addHologramLine(hologram, text);
    }

    @Override
    public void setLine(final int index, final ItemStack item) {
        DHAPI.setHologramLine(hologram, index, item);
    }

    @Override
    public void setLine(final int index, final String text) {
        DHAPI.setHologramLine(hologram, index, text);
    }

    @Override
    public void createLines(final int startingIndex, final int linesAdded) {
        final HologramPage page = DHAPI.getHologramPage(hologram, 0);
        for (int i = startingIndex; i < linesAdded; i++) {
            if (i >= page.size()) {
                DHAPI.addHologramLine(hologram, "");
            }
        }
    }

    @Override
    public void insertLine(final int index, final ItemStack item) {
        DHAPI.insertHologramLine(hologram, index, item);
    }

    @Override
    public void insertLine(final int index, final String text) {
        DHAPI.insertHologramLine(hologram, index, text);
    }

    @Override
    public void removeLine(final int index) {
        DHAPI.removeHologramLine(hologram, index);
    }

    @Override
    public void show(final Player player) {
        hologram.removeHidePlayer(player);
        hologram.show(player, 0);
    }

    @Override
    public void hide(final Player player) {
        hologram.setHidePlayer(player);
        hologram.hide(player);
    }

    @Override
    public void move(final Location location) {
        DHAPI.moveHologram(hologram, location);
    }

    @Override
    public void showAll() {
        final List<Player> players = hologram.getHidePlayers().stream().map(Bukkit::getPlayer).collect(Collectors.toList());
        players.forEach(hologram::removeHidePlayer);
        hologram.showAll();
    }

    @Override
    public void hideAll() {
        final List<Player> players = hologram.getViewerPlayers();
        players.forEach(hologram::setHidePlayer);
        hologram.hideAll();
    }

    @Override
    public void delete() {
        hologram.disable();
        DHAPI.removeHologram(hologram.getName());
    }

    @Override
    public int size() {
        return hologram.getPage(0).size();
    }

    @Override
    public void clear() {
        final HologramPage page = hologram.getPage(0);
        for (int i = page.size() - 1; i >= 0; i--) {
            page.removeLine(i);
        }
    }

    @Override
    public void refresh() {
        hologram.updateAll();
    }
}
