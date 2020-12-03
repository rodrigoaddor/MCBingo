package io.github.rodrigoaddor.bingo

object BingoConfig {
    init {
        BingoPlugin.INSTANCE.saveDefaultConfig()
    }

    val itemAmount: Int
        get() = BingoPlugin.INSTANCE.config.getInt("items.amount")

    fun reload() = BingoPlugin.INSTANCE.reloadConfig()
}