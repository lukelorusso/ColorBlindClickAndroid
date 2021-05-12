package com.lukelorusso.presentation.ui.base

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * Interface representing a View that will use to load data.
 */
interface ADataView<in Data> {

    fun render(data: Data)

}
