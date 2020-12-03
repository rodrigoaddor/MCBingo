package io.github.rodrigoaddor.bingo.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.ItemStackArgument
import dev.jorel.commandapi.executors.CommandExecutor
import io.github.rodrigoaddor.bingo.BingoConfig
import io.github.rodrigoaddor.bingo.data.GameData
import io.github.rodrigoaddor.bingo.generator.Subcommand
import io.github.rodrigoaddor.bingo.utils.ChatUtils
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.inventory.ItemStack

@Subcommand
class BingoCommandItems : CommandAPICommand("item") {
    init {
        withPermission("bingo.manage")
        withArguments(listOf(
            IntegerArgument("index", 0, BingoConfig.itemAmount - 1),
            ItemStackArgument("item")
        ))
        executes(CommandExecutor { sender, args ->
            val index = args[0] as Int
            val item = args[1] as ItemStack
            val oldItem = GameData.items[index]
            val newItems = GameData.items.toMutableList().apply { set(index, item.type) }
            GameData.items = newItems.toList()
            sender.sendMessage(ChatUtils.prefix.apply {
                addExtra(TextComponent("Set the item at index $index to "))
                addExtra(TranslatableComponent(item.type.translationKey).apply { color = ChatColor.AQUA })
                addExtra(TextComponent(" (was ").apply { color = ChatColor.GRAY })
                addExtra(TranslatableComponent(oldItem.translationKey).apply { color = ChatColor.GRAY })
                addExtra(TextComponent(")").apply { color = ChatColor.GRAY })
            })
        })
    }

}