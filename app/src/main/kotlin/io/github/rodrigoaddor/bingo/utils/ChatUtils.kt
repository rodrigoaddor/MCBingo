package io.github.rodrigoaddor.bingo.utils

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ChatUtils {
    val prefix: BaseComponent
        get() = TextComponent("Bingo").apply {
            color = ChatColor.AQUA
            addExtra(TextComponent(" || ").apply {
                color = ChatColor.DARK_GRAY
                isBold = true
            })
        }

    fun message(message: String): BaseComponent {
        return prefix.apply {
            addExtra(message)
        }
    }

    fun message(message: BaseComponent): BaseComponent {
        return prefix.apply {
            addExtra(message)
        }
    }

}