package me.outspending.invoice.core.creator

import me.outspending.invoice.core.parse
import me.outspending.invoice.core.toTiny
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ItemCreator @JvmOverloads constructor(
    private var material: Material,
    private var amount: Int = 1
) : Creator<ItemStack> {
    private var name: Component? = null
    private var type: String? = null
    private var lore: List<Component>? = null
    private var flags: Array<out ItemFlag>? = null

    override fun build(): ItemStack {
        val itemStack = ItemStack(material, amount)
        itemStack.editMeta { meta ->
            name?.let { meta.displayName(it) }
            lore?.let { lore ->
                val mutableLore = lore.toMutableList()
                this.type?.let { type ->
                    mutableLore.apply {
                        add(0, "<dark_gray>${type.toTiny()}".parse())
                        add(1, "".parse())
                    }
                }

                meta.lore(mutableLore)
            }
            flags?.let { meta.addItemFlags(*it) }
        }

        return itemStack
    }
}