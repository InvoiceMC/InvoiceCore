package me.outspending.invoice.core.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.outspending.invoice.core.*
import me.outspending.invoice.core.data.player.PlayerData
import me.outspending.invoice.core.data.player.getData
import me.outspending.invoice.core.data.player.getDataNullable
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

private val updatableLines: Array<Int> = arrayOf(3, 4, 5, 8, 9, 10)

private val scoreboardTitle: Component = "<main><b>ɪɴᴠᴏɪᴄᴇ</b>".parse()
private val scoreboardString: Array<String> =
    arrayOf(
        "         <gray>ꜱᴇᴀꜱᴏɴ 1",
        "",
        "<main><bold>%player%",
        " <second><b>|</b> <gray>ᴘʀᴇꜱᴛɪɢᴇ: <dark_gray>[%prestige%<dark_gray>]",
        " <second><b>|</b> <gray>ʟᴇᴠᴇʟ: <white>%level%",
        " <second><b>|</b> <dark_gray>- %progress%",
        "",
        "<main><bold>ʙᴀʟᴀɴᴄᴇꜱ",
        " <second><b>|</b> <gray>ʙᴀʟᴀɴᴄᴇ: <green>$%balance%",
        " <second><b>|</b> <gray>ɢᴏʟᴅ: <yellow>⛁%gold%",
        " <second><b>|</b> <gray>ᴍɪɴᴇᴅ: ⛏%blocks%",
        "",
        "<white><u>Invoice</u>.minehut.gg     "
    )

class ScoreboardHandler {
    private val scoreboards: HashMap<Player, FastBoard> = HashMap()

    companion object {
        private val instance = ScoreboardHandler()

        fun create(player: Player) = instance.createScoreboard(player)
        fun delete(player: Player) = instance.deleteScoreboard(player)
    }

    init {
        runTaskTimer(40L, 40L, true) {
            for (player in scoreboards.keys) {
                val scoreboard = scoreboards[player] ?: continue

                updateScoreboard(player, scoreboard, false)
            }
        }
    }

    fun createScoreboard(player: Player) {
        val scoreboard = FastBoard(player)

        scoreboards[player] = scoreboard
        updateScoreboard(player, scoreboard, true)
    }

    fun deleteScoreboard(player: Player) {
        val scoreboard = scoreboards[player] ?: return

        scoreboard.delete()
        scoreboards.remove(player)
    }

    private fun parseLine(player: Player, line: String, playerData: PlayerData): String {
        return line
            .replace("%player%", player.name)
            .replace("%level%", player.level.toString())
            .replace(
                "%progress%",
                "${progressBar(player.exp, 100, 5, "■")} <dark_gray>[<gray>${(player.exp * 100.0).fix()}%<dark_gray>]"
            )
            .replace("%balance%", playerData.balance.format())
            .replace("%gold%", playerData.gold.format())
            .replace("%blocks%", playerData.blocksBroken.format())
            .replace("%prestige%", "<#c97be3>★${playerData.prestige}")
    }

    private fun updateScoreboard(player: Player, fastboard: FastBoard, updateAll: Boolean = false) {
        val data = player.getDataNullable() ?: PlayerData(player.uniqueId)

        if (updateAll) {
            fastboard.updateTitle(scoreboardTitle)
            fastboard.updateLines(
                scoreboardString.map { parseLine(player, it, data).parse() }
            )
        } else {
            for (line in updatableLines) {
                fastboard.updateLine(line, parseLine(player, scoreboardString[line], data).parse())
            }
        }
    }
}