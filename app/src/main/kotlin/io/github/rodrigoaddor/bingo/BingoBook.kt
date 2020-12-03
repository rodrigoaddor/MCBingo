package io.github.rodrigoaddor.bingo

import io.github.rodrigoaddor.bingo.generator.RegisterListener
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@RegisterListener
object BingoBook : Listener {
    fun createBook() = ItemStack(Material.KNOWLEDGE_BOOK).apply {
        itemMeta = itemMeta.apply {
            setDisplayName("§6Bingo Book")
            addEnchant(Enchantment.DURABILITY, 1, false)
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    @EventHandler
    fun onUseBook(e: PlayerInteractEvent) {
        if ((e.action == Action.RIGHT_CLICK_AIR || (e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock?.type?.isInteractable != true)) && e.item == createBook()) {
            BingoInventory.show(e.player)
            e.isCancelled = true
        }
    }
}