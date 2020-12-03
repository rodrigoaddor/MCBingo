package io.github.rodrigoaddor.bingo

import io.github.rodrigoaddor.bingo.BingoInventory.isBingoInventory
import io.github.rodrigoaddor.bingo.data.GameData
import io.github.rodrigoaddor.bingo.generator.RegisterListener
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

@RegisterListener
object BingoItemListener : Listener {
    @EventHandler
    fun onItemPickup(e: EntityPickupItemEvent) {
        if (e.entityType == EntityType.PLAYER && GameData.started) {
            GameData.addItem(e.entity as Player, e.item.itemStack.type)
        }
    }

    @EventHandler
    fun onItemMove(e: InventoryClickEvent) {
        if (GameData.started) {
            // Player placed item in inventory
            if (e.clickedInventory?.type == InventoryType.PLAYER) {
                e.cursor?.let { GameData.addItem(e.whoClicked as Player, it.type) }
            } else if (e.isShiftClick && e.clickedInventory?.let { it.isBingoInventory() } == false) {
                e.currentItem?.let { GameData.addItem(e.whoClicked as Player, it.type) }
            }
        }
    }
}