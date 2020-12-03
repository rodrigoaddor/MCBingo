package io.github.rodrigoaddor.bingo.data

import com.destroystokyo.paper.Title
import io.github.rodrigoaddor.bingo.BingoConfig
import io.github.rodrigoaddor.bingo.utils.ChatUtils
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.SelectorComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.scoreboard.Team

object GameData {
    var items: List<Material> = emptyList()
    var started: Boolean = false
    var found: Map<Team, List<Pair<Player, Material>>> = emptyMap()
    private val listeners: MutableList<Runnable> = mutableListOf()

    fun generateItems() {
        val allItems: MutableSet<Material> = mutableSetOf()
        for (recipe in Bukkit.recipeIterator()) {
            allItems.add(recipe.result.type)
            when (recipe) {
                is ShapedRecipe -> allItems += recipe.ingredientMap.values.filterNotNull().map { it.type }
                is ShapelessRecipe -> allItems += recipe.ingredientList.filterNotNull().map { it.type }
                is FurnaceRecipe -> allItems += recipe.input.type
            }
        }

        items = allItems.filter { !it.isAir }.shuffled().take(BingoConfig.itemAmount)
    }

    fun addItem(player: Player, item: Material) {
        if (item in items) {
            val team = player.scoreboard.getEntryTeam(player.name)
            if (team != null && found[team]?.map { it.second }?.contains(item) != true) {
                found = found + Pair(team, (found[team] ?: emptyList()) + Pair(player, item))
                for (listener in listeners) listener.run()
                if (!checkVictory(team)) {
                    Bukkit.broadcast(ChatUtils.prefix.apply {
                        addExtra(SelectorComponent(player.name))
                        addExtra(TextComponent(" found "))
                        addExtra(TranslatableComponent(item.let {
                            if (it.isBlock) {
                                it.translationKey.replace("item.", "block.")
                            } else {
                                it.translationKey
                            }
                        }))
                    })
                }
            }
        }
    }

    private fun checkVictory(team: Team): Boolean {
        found[team]?.run {
            if (size == items.size && map { it.second }.containsAll(items)) {
                val victory = TextComponent("Victory").apply { color = ChatColor.GOLD; isBold = true }
                val defeat = TextComponent("Defeat").apply { color = ChatColor.DARK_RED; isBold = true }
                val subtitle = TextComponent(team.displayName).apply {
                    color = team.color.asBungee()
                    addExtra(TextComponent(" won the game!"))
                }
                Bukkit.getOnlinePlayers().forEach {
                    it.sendTitle(Title(if (team.hasEntry(it.name)) victory else defeat, subtitle, 10, 80, 10))
                }
            }
        }
        return false
    }

    fun addListener(listener: Runnable) {
        listeners += listener
    }

    fun removeListener(listener: Runnable) {
        listeners -= listener
    }
}