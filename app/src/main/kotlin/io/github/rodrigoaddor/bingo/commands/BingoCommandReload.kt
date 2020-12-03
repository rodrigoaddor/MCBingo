package io.github.rodrigoaddor.bingo.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.executors.CommandExecutor
import io.github.rodrigoaddor.bingo.BingoConfig
import io.github.rodrigoaddor.bingo.generator.Subcommand
import io.github.rodrigoaddor.bingo.utils.ChatUtils

@Subcommand
class BingoCommandReload : CommandAPICommand("reload") {
    init {
        withPermission(CommandPermission.OP)
        executes(CommandExecutor { sender, _ ->
            BingoConfig.reload()
            sender.sendMessage(ChatUtils.message("Config reloaded."))
        })
    }
}