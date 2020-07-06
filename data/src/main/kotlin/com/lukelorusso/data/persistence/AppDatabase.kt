package com.lukelorusso.data.persistence

import com.lukelorusso.data.persistence.dao.ColorDAO

class AppDatabase {

    fun color() = ColorDAO()

}
