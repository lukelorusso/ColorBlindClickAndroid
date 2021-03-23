package com.lukelorusso.presentation.scenes.base.view

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Interface representing a View that will use to load data.
 */
interface ADataView<in Data> {

    fun render(data: Data)

}
