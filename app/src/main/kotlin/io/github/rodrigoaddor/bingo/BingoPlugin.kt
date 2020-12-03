package io.github.rodrigoaddor.bingo

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import io.github.rodrigoaddor.bingo.commands.*
import org.bukkit.plugin.java.JavaPlugin

import io.github.rodrigoaddor.bingo.generated.RegisterListeners
import io.github.rodrigoaddor.bingo.generated.RegisterSubcommands

@Suppress("unused")
class BingoPlugin : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: BingoPlugin
            private set
    }

    init {
        INSTANCE = this
    }

    override fun onLoad() {
        CommandAPI.onLoad(true)
    }

    override fun onEnable() {
        CommandAPI.onEnable(this)

        RegisterSubcommands("bingo")
        RegisterListeners(this)
    }
}