package com.lukelorusso.domain.usecase.base

import io.reactivex.rxjava3.core.Scheduler

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 */
data class UseCaseScheduler(val run: Scheduler, val post: Scheduler)
