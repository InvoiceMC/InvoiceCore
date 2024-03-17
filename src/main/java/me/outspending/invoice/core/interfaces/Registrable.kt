package me.outspending.invoice.core.interfaces

/**
 * This interface is used to define a registrable. This is used to register classes and packages.
 *
 * @since 0.0.1
 * @author Outspending
 */
interface Registrable {
/**
     * This method is used to register classes.
     *
     * @since 0.0.1
     * @param classes The classes to register
     */
    fun register(vararg packages: String)
}