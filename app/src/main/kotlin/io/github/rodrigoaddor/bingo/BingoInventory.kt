package io.github.rodrigoaddor.bingo

import io.github.rodrigoaddor.bingo.data.GameData
import io.github.rodrigoaddor.bingo.generator.RegisterListener
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Team
import kotlin.math.*

@RegisterListener
object BingoInventory : Listener {
    private val inventories: MutableMap<Inventory, Runnable> = mutableMapOf()

    private fun makeItem(item: Material, team: Team?): ItemStack {
        return ItemStack(item).apply {
            val meta = itemMeta
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (team != null && GameData.found[team]?.map { it.second }?.contains(item) == true) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false)
                meta.lore = listOf("§fFound!")
            }
            itemMeta = meta
        }
    }

    fun show(player: Player, team: Team? = player.scoreboard.getEntryTeam(player.name)) {
        val type = if (BingoConfig.itemAmount <= 9) InventoryType.DROPPER else InventoryType.CHEST
        val inv = Bukkit.createInventory(null, type, "Bingo").apply {
            if (type == InventoryType.CHEST) {
                ceil(BingoConfig.itemAmount.toDouble() / 9).toInt().coerceIn(9 * 3, 9 * 6)
            }
            for (item in GameData.items) {
                addItem(makeItem(item, team))
            }
        }
        val update = Runnable {
            GameData.items.withIndex().forEach { (index, item) ->
                inv.setItem(index, makeItem(item, team))
            }
        }
        inventories[inv] = update
        GameData.addListener(update)
        player.openInventory(inv)
    }

    fun Inventory.isBingoInventory() = this in inventories.keys

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        e.run {
            if (!inventory.isBingoInventory()) return
            isCancelled = isCancelled ||
                    clickedInventory?.isBingoInventory() == true ||
                    clickedInventory?.type == InventoryType.PLAYER &&
                    isShiftClick
        }
    }

    @EventHandler
    fun onInventoryDrag(e: InventoryDragEvent) {
        e.run event@{
            if (!inventory.isBingoInventory()) return

            val items = newItems.filterKeys { it < inventory.size }
            val oldContents = listOf(*inventory.contents)
            val modifiedAmount = items.values.sumBy { it.amount }

            cursor = cursor?.apply { amount += modifiedAmount } ?: oldCursor.apply { amount = modifiedAmount }

            Bukkit.getScheduler().runTask(BingoPlugin.INSTANCE, Runnable {
                items.forEach { (slot, _) ->
                    inventory.setItem(slot, oldContents[slot])
                }
            })
        }
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        inventories.remove(e.inventory)?.run { GameData.removeListener(this) }
    }
}