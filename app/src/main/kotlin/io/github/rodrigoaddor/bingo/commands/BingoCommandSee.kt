package io.github.rodrigoaddor.bingo.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.TeamArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor
import io.github.rodrigoaddor.bingo.BingoInventory
import io.github.rodrigoaddor.bingo.generator.Subcommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Subcommand
class BingoCommandSee : CommandAPICommand("see") {
    init {
        withPermission("bingo.see")
        withArguments(TeamArgument("team").withPermission("bingo.see.others"))
        executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any?>? ->
            if (args?.get(0) != null) {
                val team = Bukkit.getScoreboardManager().mainScoreboard.getTeam(args[0] as String)
                BingoInventory.show(player, team)
            } else {
                BingoInventory.show(player)
            }
        })
    }
}

//@Subcommand
//class BingoCommandSeeOthers : CommandAPICommand("see") {
//    init {
//        withArguments(TeamArgument("team").withPermission("bingo.see.others"))
//        executesPlayer(PlayerResultingCommandExecutor { player: Player, args: Array<Any?>? ->
//            if (args?.get(0) != null) {
//                val team = Bukkit.getScoreboardManager().mainScoreboard.getTeam(args[0] as String)
//                BingoInventory.show(player, team)
//                return@PlayerResultingCommandExecutor 1
//            } else {
//                return@PlayerResultingCommandExecutor 0
//            }
//        })
//    }
//}