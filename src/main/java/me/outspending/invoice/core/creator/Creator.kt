package me.outspending.invoice.core.creator

/**
 * This interface is used for building objects inside InvoiceCore. This is used widely in the plugin to build objects.
 *
 * @since 0.0.1
 * @author Outspending
 */
interface Creator<T> {
    /**
     * This method is executed when the object is wanting to be built. This is usually the last method called in the chain.
     *
     * @since 0.0.1
     * @author Outspending
     * @sample ItemCreator
     * @return The built object
     */
    fun build(): T
}