package me.outspending.invoice.core

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

/**
 * This class represents a 3D block vector. This is used to grab the min and max of 2 locations.
 *
 * @author Outspending
 * @since 0.0.1
 */
data class BlockVector3D(
    val minX: Int,
    val minY: Int,
    val minZ: Int,
    val maxX: Int,
    val maxY: Int,
    val maxZ: Int
) {
    constructor(blockLocation: Location, vec1: Vector, vec2: Vector) : this(
        minX = min(blockLocation.blockX - vec1.blockX, blockLocation.blockX - vec2.blockX),
        minY = min(blockLocation.blockY - vec1.blockY, blockLocation.blockY - vec2.blockY),
        minZ = min(blockLocation.blockZ - vec1.blockZ, blockLocation.blockZ - vec2.blockZ),
        maxX = max(blockLocation.blockX - vec1.blockX, blockLocation.blockX - vec2.blockX),
        maxY = max(blockLocation.blockY - vec1.blockY, blockLocation.blockY - vec2.blockY),
        maxZ = max(blockLocation.blockZ - vec1.blockZ, blockLocation.blockZ - vec2.blockZ)
    )

    constructor(bottomLocation: Location, topLocation: Location) : this(
        minX = min(bottomLocation.blockX, topLocation.blockX),
        minY = min(bottomLocation.blockY, topLocation.blockY),
        minZ = min(bottomLocation.blockZ, topLocation.blockZ),
        maxX = max(bottomLocation.blockX, topLocation.blockX),
        maxY = max(bottomLocation.blockY, topLocation.blockY),
        maxZ = max(bottomLocation.blockZ, topLocation.blockZ)
    )

    /**
     * Retrieves the minimum location of the block vector in the given world.
     *
     * @param world The world in which the location is retrieved.
     * @return The minimum location as a Bukkit Location object.
     * @since 0.0.1
     * @author Outspending
     */
    fun getMin(world: World) = Location(world, minX.toDouble(), minY.toDouble(), minZ.toDouble())

    /**
     * Retrieves the maximum location of the block vector in the given world.
     *
     * @param world The world in which the location is retrieved.
     * @return The maximum location as a Bukkit Location object.
     * @since 0.0.1
     * @author Outspending
     */
    fun getMax(world: World) = Location(world, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())
}