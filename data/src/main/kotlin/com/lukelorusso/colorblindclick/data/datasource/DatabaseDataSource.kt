package com.lukelorusso.colorblindclick.data.datasource

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface DatabaseDataSource {
    //region Color
    suspend fun getColorList(): List<ColorEntity>

    suspend fun saveColorList(list: List<ColorEntity>)

    suspend fun deleteColor(element: ColorEntity)

    suspend fun clearColorList()
    //endregion
}
