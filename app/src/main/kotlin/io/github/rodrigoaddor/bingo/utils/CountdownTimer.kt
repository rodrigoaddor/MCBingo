package io.github.rodrigoaddor.bingo.utils

import io.github.rodrigoaddor.bingo.BingoPlugin
import org.bukkit.scheduler.BukkitRunnable

class CountdownTimer(
    private val plugin: BingoPlugin = BingoPlugin.INSTANCE,
    val time: Int,
    private val delay: Int? = null,
    private val runEverySecond: ((timeRemaining: Int) -> Unit)? = null,
    private val runOnDone: (() -> Unit)? = null,
) : BukkitRunnable() {
    var timeRemaining: Int

    init {
        assert(time > 0) { "Time must be greater than 0" }
        timeRemaining = time
    }

    fun start() {
        runTaskTimer(plugin, ((delay ?: 0) * 20).toLong(), 20)
    }

    override fun run() {
        if (timeRemaining > 0) {
            runEverySecond?.invoke(timeRemaining)
            timeRemaining--
        } else {
            runOnDone?.invoke()
            cancel()
        }
    }
}