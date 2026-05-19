package com.lukelorusso.colorblindclick.data.datasource

import com.lukelorusso.domain.model.Color

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface DatabaseDataSource {
    //region Color
    fun getColorList(): List<Color>

    fun saveColorList(list: List<Color>)

    fun clearColorList()
    //endregion
}
