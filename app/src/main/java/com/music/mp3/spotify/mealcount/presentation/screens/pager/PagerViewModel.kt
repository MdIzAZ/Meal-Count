package com.music.mp3.spotify.mealcount.presentation.screens.pager

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.mp3.spotify.mealcount.domain.mappers.getCombineMapOfWholeDayCount
import com.music.mp3.spotify.mealcount.domain.mappers.toListOfWholeDayCount
import com.music.mp3.spotify.mealcount.domain.mappers.toRoomWithCountEntity
import com.music.mp3.spotify.mealcount.domain.repo.MealCountRepo
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MealTime
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class PagerViewModel @Inject constructor(
    private val mealCountRepo: MealCountRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PagerScreenState())
    val state = _state.asStateFlow()


    fun onEvent(event: PagerScreenEvents) {
        when (event) {

            is PagerScreenEvents.AddMealCounts -> saveToDb(event.date)

            is PagerScreenEvents.Increase -> {

                val roomNo = event.roomNo
                val time = event.menuTime
                val mealType = event.mealType

                increaseMealCountValue(roomNo, mealType, time)

            }

            is PagerScreenEvents.Decrease -> {

                val roomNo = event.roomNo
                val time = event.menuTime
                val mealType = event.mealType

                when (time) {
                    MealTime.DAY -> decreaseMealCountValue(roomNo, mealType, time)
                    MealTime.NIGHT -> decreaseMealCountValue(roomNo, mealType, time)
                }
            }

            is PagerScreenEvents.HalalListChange -> {
                when (event.menuTime) {
                    MealTime.DAY -> {
                        val oldHalals = _state.value.dHalal
                        when (event.changeTYPE) {
                            COUNTER_TYPE.INC -> {
                                _state.update { it.copy(dHalal = oldHalals + event.names) }
                            }

                            COUNTER_TYPE.DEC -> {
                                _state.update { it.copy(dHalal = oldHalals - event.names) }
                            }
                        }
                    }

                    MealTime.NIGHT -> {
                        val oldHalals = _state.value.nHalal
                        when (event.changeTYPE) {
                            COUNTER_TYPE.INC -> {
                                _state.update { it.copy(nHalal = oldHalals + event.names) }
                            }

                            COUNTER_TYPE.DEC -> {
                                _state.update { it.copy(nHalal = oldHalals - event.names) }
                            }
                        }
                    }
                }
            }
        }
    }


    fun loadDataFromDb(date: String, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mealCountRepo.getAllMealCounts(date).collectLatest {

                    val wholeDayCounts = it.toListOfWholeDayCount()
                    val morning = wholeDayCounts.associate { wholeDay ->

                        val mealWithCounts = MealWithCounts(
                            veg = wholeDay.dVeg.toInt(),
                            nonVeg = wholeDay.dNonVeg.toInt(),
                            halal = wholeDay.dHalal.toInt()
                        )

                        wholeDay.roomNo to mealWithCounts
                    }

                    val night = wholeDayCounts.associate { wholeDay ->

                        val mealWithCounts = MealWithCounts(
                            veg = wholeDay.nVeg.toInt(),
                            nonVeg = wholeDay.nNonVeg.toInt(),
                            halal = wholeDay.nHalal.toInt()
                        )

                        wholeDay.roomNo to mealWithCounts
                    }

                    val dayHalals = wholeDayCounts.map {whole->
                        whole.dayHalals.map {name->
                            Halal(name, whole.roomNo)
                        }
                    }.flatten()

                    val nightHalals = wholeDayCounts.map {whole->
                        whole.nightHalals.map {name->
                            Halal(name, whole.roomNo)
                        }
                    }.flatten()

                    _state.emit(
                        PagerScreenState(
                            morning = morning,
                            night = night,
                            dHalal =dayHalals,
                            nHalal = nightHalals
                        )
                    )

                    onComplete()
                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }

    fun resetState() {
        _state.value = PagerScreenState()
    }

    private fun  saveToDb(date: String) {

        val combineMap = getCombineMapOfWholeDayCount(
            date,
            morning = _state.value.morning,
            night = _state.value.night,
            dayHalalList = _state.value.dHalal,
            nightHalalList = _state.value.nHalal
        )


        combineMap.entries.forEach {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val wholeDayCount = it.value

                    val mealWithCounts = wholeDayCount.toRoomWithCountEntity()

                    mealCountRepo.insertMealCount(mealWithCounts)
                    Log.d("izaz", "Saved to db")

//                    createPdf()
                } catch (e: Exception) {
                    Log.d("izaz", "DB")
                    Log.d("izaz", e.message ?: " DB: Unknown error")
                }
            }
        }

    }

    private fun increaseMealCountValue(roomNo: Int, mealType: MealType, time: MealTime) {

        val oldMap = if (time == MealTime.DAY) _state.value.morning.toMutableMap()
        else _state.value.night.toMutableMap()

        val mealWithCounts = oldMap[roomNo] ?: MealWithCounts()
        when (mealType) {
            MealType.HALAL -> {
                var h = mealWithCounts.halal
                h++
                val newMealWithCounts = mealWithCounts.copy(halal = h)
                oldMap[roomNo] = newMealWithCounts
                _state.update {
                    if (time == MealTime.DAY) {
                        it.copy(morning = oldMap)
                    } else it.copy(night = oldMap)
                }
            }

            MealType.VEG -> {
                var h = mealWithCounts.veg
                h++
                val newMealWithCounts = mealWithCounts.copy(veg = h)
                oldMap[roomNo] = newMealWithCounts
                _state.update {
                    if (time == MealTime.DAY) {
                        it.copy(morning = oldMap)
                    } else it.copy(night = oldMap)
                }
            }

            MealType.NON_VEG -> {
                var h = mealWithCounts.nonVeg
                h++
                val newMealWithCounts = mealWithCounts.copy(nonVeg = h)
                oldMap[roomNo] = newMealWithCounts
                _state.update {
                    if (time == MealTime.DAY) {
                        it.copy(morning = oldMap)
                    } else it.copy(night = oldMap)
                }
            }
        }
    }

    private fun decreaseMealCountValue(roomNo: Int, mealType: MealType, time: MealTime) {

        val oldMap = if (time == MealTime.DAY) _state.value.morning.toMutableMap()
        else _state.value.night.toMutableMap()

        val mealWithCounts = oldMap[roomNo] ?: MealWithCounts()
        when (mealType) {
            MealType.HALAL -> {
                var h = mealWithCounts.halal
                h = max(h - 1, 0)
                val newMealWithCounts = mealWithCounts.copy(halal = h)
                oldMap[roomNo] = newMealWithCounts
                _state.update {
                    if (time == MealTime.DAY) {
                        it.copy(morning = oldMap)
                    } else it.copy(night = oldMap)
                }
            }

            MealType.VEG -> {
                var h = mealWithCounts.veg
                h = max(h - 1, 0)
                val newMealWithCounts = mealWithCounts.copy(veg = h)
                oldMap[roomNo] = newMealWithCounts
                _state.update {
                    if (time == MealTime.DAY) {
                        it.copy(morning = oldMap)
                    } else it.copy(night = oldMap)
                }
            }

            MealType.NON_VEG -> {
                var h = mealWithCounts.nonVeg
                h = max(h - 1, 0)
                val newMealWithCounts = mealWithCounts.copy(nonVeg = h)
                oldMap[roomNo] = newMealWithCounts
                _state.update {
                    if (time == MealTime.DAY) {
                        it.copy(morning = oldMap)
                    } else it.copy(night = oldMap)
                }
            }
        }
    }

    private fun createPdf() {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 3).create()
        val page = document.startPage(pageInfo)

        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = Color.BLACK
        paint.textSize = 16f
        paint.isAntiAlias = true

        // Table specifications
        val startX = 20f
        val startY = 100f
        val rowHeight = 40f

        // Column widths
        val roomNoWidth = 100f
        val mealGroupWidth = 400f // Morning & Night together
        val subColWidth = mealGroupWidth / 6 // (Veg, Non-Veg, Halal) for each

        // Sample data
        val roomNumbers = arrayOf("101", "102", "103", "104")

        // Draw major headers (Morning & Night)
        canvas.drawText("Morning", startX + roomNoWidth + (subColWidth * 1.5f), startY + 20, paint)
        canvas.drawText("Night", startX + roomNoWidth + (subColWidth * 4.5f), startY + 20, paint)

        //Room No
        canvas.drawText("Room No", startX + 10, startY + 20, paint)

        // Draw sub-headers (Veg, Non-Veg, Halal)
        paint.textSize = 12f
        paint.isFakeBoldText = false
        val subHeaders = arrayOf("Veg", "Non-Veg", "Halal", "Veg", "Non-Veg", "Halal")

        var currentX = startX + roomNoWidth

        for (header in subHeaders) {
            canvas.drawText(header, currentX + 10, startY + rowHeight, paint)
            currentX += subColWidth
        }

        // Draw rows for Room No
        var currentY = startY + rowHeight + 20
        for (room in _state.value.morning.keys) {
            canvas.drawText(room.toString(), startX + 10, currentY + 25, paint)
            currentY += rowHeight
        }

        // Draw table borders
        paint.strokeWidth = 2f
        currentY = startY

        //top most line
        canvas.drawLine(startX, startY, startX + roomNoWidth + mealGroupWidth, startY, paint)

        //line below main headers
        canvas.drawLine(
            startX + roomNoWidth,
            startY + 26,
            startX + roomNoWidth + mealGroupWidth,
            startY + 26,
            paint
        )

        // Horizontal lines
        for (i in 0.._state.value.morning.size) {
            val lineY = startY + 20 + (i + 1) * rowHeight
            canvas.drawLine(startX, lineY, startX + roomNoWidth + mealGroupWidth, lineY, paint)
        }


        /*-------------Vertical Lines---------------*/
        currentX = startX
        //left most vertical line
        canvas.drawLine(
            currentX,
            startY,
            currentX,
            20 + startY + (roomNumbers.size + 2) * rowHeight,
            paint
        )
        currentX += roomNoWidth
        //next to left most
        canvas.drawLine(
            currentX,
            startY,
            currentX,
            20 + startY + (roomNumbers.size + 2) * rowHeight,
            paint
        )
        currentX += subColWidth
        for (i in 1..5) {
            canvas.drawLine(
                currentX,
                startY + 26,
                currentX,
                20 + startY + (roomNumbers.size + 2) * rowHeight,
                paint
            )
            currentX += subColWidth
        }
        //right most vertical line
        canvas.drawLine(
            currentX,
            startY,
            currentX,
            20 + startY + (roomNumbers.size + 2) * rowHeight,
            paint
        )
        /*--------------------------------------------*/

        document.finishPage(page)

        try {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "meal_count")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOCUMENTS
                )
            }

            val contentUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val uri = context.contentResolver.insert(contentUri, contentValues)

            uri?.let {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    document.writeTo(outputStream)
                }
            }


        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            document.close()
        }


    }

}