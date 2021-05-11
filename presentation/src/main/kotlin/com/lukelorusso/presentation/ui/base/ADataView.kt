package com.lukelorusso.presentation.ui.base

/**
 * Interface representing a View that will use to load data.
 */
interface ADataView<in Data> {

    fun render(data: Data)

}
