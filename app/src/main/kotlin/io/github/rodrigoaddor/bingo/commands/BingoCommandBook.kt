package io.github.rodrigoaddor.bingo.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.github.rodrigoaddor.bingo.BingoBook
import io.github.rodrigoaddor.bingo.data.GameData
import io.github.rodrigoaddor.bingo.generator.Subcommand
import io.github.rodrigoaddor.bingo.utils.ChatUtils
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

@Subcommand
class BingoCommandBook : CommandAPICommand("book") {
    init {
        withPermission("bingo.book")
        executesPlayer(PlayerCommandExecutor { player: Player, _: Array<Any?>? ->
            if (GameData.started) {
                player.inventory.addItem(BingoBook.createBook())
            } else {
                player.sendMessage(ChatUtils.message(TextComponent("The game hasn't started yet!").apply {
                    color = ChatColor.DARK_RED
                }))
            }
        })
    }
}