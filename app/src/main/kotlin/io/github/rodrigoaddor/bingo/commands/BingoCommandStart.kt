package io.github.rodrigoaddor.bingo.commands

import io.github.rodrigoaddor.bingo.data.GameData
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import io.github.rodrigoaddor.bingo.BingoBook
import io.github.rodrigoaddor.bingo.BingoConfig
import io.github.rodrigoaddor.bingo.generator.Subcommand
import io.github.rodrigoaddor.bingo.utils.CountdownTimer
import org.bukkit.Bukkit
import org.bukkit.Material

@Subcommand
class BingoCommandStart : CommandAPICommand("start") {
    init {
        withPermission("bingo.start")
        executes(CommandExecutor { _, _ ->
            CountdownTimer(
                time = 3,
                runEverySecond = { i ->
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendTitle("", "Starting in $i", 4, 12, 4)
                    }
                },
                runOnDone = {
                    GameData.started = true
                    GameData.generateItems()
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendTitle("The game has started!", "Collect them all", 10, 80, 10)
                        if (!it.inventory.contains(BingoBook.createBook())) {
                            it.inventory.addItem(BingoBook.createBook())
                        }
                    }
                }
            ).start()
        })
    }
}