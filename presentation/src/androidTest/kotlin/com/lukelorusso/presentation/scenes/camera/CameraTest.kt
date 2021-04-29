package com.lukelorusso.presentation.scenes.camera

import android.os.Handler
import android.os.Looper
import com.lukelorusso.data.repository.TestColorDataRepository
import com.lukelorusso.domain.usecases.GetColor
import com.lukelorusso.presentation.scenes.base.lifecycle.TestLifecycleOwner
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
 * @author LukeLorusso on 26-03-2021.
 */
class CameraTest : TestLifecycleOwner() {

    // Intents
    private val intentGetColor = PublishSubject.create<GetColor.Param>()

    // Properties
    private lateinit var viewModel: CameraTestViewModel

    @Before
    fun setup() {
        startLifecycle()

        initViewModel()

        subscribeIntents()

        Handler(Looper.getMainLooper()).post {
            observeLifecycle()
        }
    }

    private fun initViewModel() {
        TestColorDataRepository().getRepository().also { repository ->
            viewModel = CameraTestViewModel(repository)
        }
    }

    private fun observeLifecycle() {
        viewModel.observe(this) { data ->
            if (data != null) onSuccess?.invoke(data)
        }
    }

    private fun subscribeIntents() {
        val getColor = intentGetColor.flatMap { viewModel.intentGetColor(it) }

        viewModel.subscribe(getColor)
    }

    @After
    fun after() {
        stopLifecycle()
    }

    /**
     * Given a color hex code,
     * invoke the getColor service and check the hex code of the similar color found matches
     */
    @Test
    fun testGetColor() {
        val colorHex = "#52851E"
        val deviceUdid = "JUnit"
        var similarColorHex: String? = null

        onSuccess = object : (Any) -> Unit {
            override fun invoke(any: Any) {
                (any as? CameraData)?.also { data ->
                    println("• colorModel: ${data.color}")
                    similarColorHex = data.color?.similarColor
                }
            }

        }

        intentGetColor.onNext(GetColor.Param(colorHex, deviceUdid))

        while (similarColorHex == null) {
            continue
        }

        Assert.assertEquals(similarColorHex, "#6B8E23")
    }

}
