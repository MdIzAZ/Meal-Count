package com.music.mp3.spotify.mealcount.presentation.navigation

import android.app.Activity
import android.icu.util.Calendar
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.music.mp3.spotify.mealcount.presentation.screens.floors_rooms.SelectFloorAndRooms
import com.music.mp3.spotify.mealcount.presentation.screens.floors_rooms.SharedViewModel
import com.music.mp3.spotify.mealcount.presentation.screens.home.HomeScreen
import com.music.mp3.spotify.mealcount.presentation.screens.home.HomeViewModel
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MenuSelectionScreen
import com.music.mp3.spotify.mealcount.presentation.screens.pager.COUNTER_TYPE
import com.music.mp3.spotify.mealcount.presentation.screens.pager.PagerScreen
import com.music.mp3.spotify.mealcount.presentation.screens.pager.PagerScreenEvents
import com.music.mp3.spotify.mealcount.presentation.screens.pager.PagerViewModel
import com.music.mp3.spotify.mealcount.presentation.table_screen.TableScreen
import com.music.mp3.spotify.mealcount.presentation.table_screen.TableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    pagerViewModel: PagerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {


        composable<Routes.HomeScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        ) {

            val context = LocalContext.current

            val homeViewModel = hiltViewModel<HomeViewModel>()
            val previousRecords by homeViewModel.previousRecords.collectAsStateWithLifecycle()


            LaunchedEffect(Unit) {
                homeViewModel.getAllCounts()
            }


            val groupedByDate = previousRecords.groupBy { it.date }


            HomeScreen(
                previousCounts = groupedByDate,
                onBackPress = {
                    (context as Activity).finish()
                },
                onAddNew = {
                    navController.navigate(Routes.MenuSelectionScreen)
                },
                onCardClick = {
                    navController.navigate(Routes.TableScreen(it))
                },
                onDelete = {
                    homeViewModel.deleteCountsOfADay(it)
                }
            )
        }

        composable<Routes.MenuSelectionScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {

            MenuSelectionScreen(
                onBackPress = {
                    navController.navigateUp()
                },
                onNextButtonClick = { morning, night ->
                    navController.navigate(
                        Routes.FloorAndRoomSelectionScreen(
                            morning = morning,
                            night = night
                        )
                    )
                }
            )

        }

        composable<Routes.FloorAndRoomSelectionScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {

            val args = it.toRoute<Routes.FloorAndRoomSelectionScreen>()
            val morning = args.morning
            val night = args.night


            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DAY_OF_MONTH)

            val date = "$year-$month-$day"

            SelectFloorAndRooms(
                onBackPress = {
                    navController.navigateUp()
                },
                onNextButtonClick = { roomsMap ->
                    sharedViewModel.addRoomsAndFloors(roomsMap)
                    pagerViewModel.resetState()
                    navController.navigate(Routes.PagerScreen(date, morning, night))
                }
            )
        }

        composable<Routes.PagerScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {




            val rooms by sharedViewModel.rooms.collectAsStateWithLifecycle()
            val args1 = it.toRoute<Routes.FloorAndRoomSelectionScreen>()
            val morning = args1.morning
            val night = args1.night

            val args2 = it.toRoute<Routes.PagerScreen>()
            val date = args2.date

            val states by pagerViewModel.state.collectAsStateWithLifecycle()



            PagerScreen(
                rooms = rooms,
                morning = morning,
                night = night,
                states = states,
                onNextBtnClick = {
                    pagerViewModel.onEvent(PagerScreenEvents.AddMealCounts(date))
                    sharedViewModel.saveMetaDataToDb(date, morning, night)
                    navController.navigate(Routes.TableScreen(date))

                },
                onCounterValueChane = { roomNo, type, mealType, time ->

                    when (type) {
                        COUNTER_TYPE.INC -> {
                            pagerViewModel.onEvent(
                                PagerScreenEvents.Increase(
                                    roomNo = roomNo,
                                    menuTime = time,
                                    mealType = mealType
                                )
                            )
                        }

                        COUNTER_TYPE.DEC -> {
                            pagerViewModel.onEvent(
                                PagerScreenEvents.Decrease(
                                    roomNo = roomNo,
                                    menuTime = time,
                                    mealType = mealType
                                )
                            )
                        }
                    }

                },
                onBackPress = {
                    navController.navigateUp()
                },
                onHalalListChange = { names, roomNo, time, changeType ->
                    pagerViewModel.onEvent(
                        PagerScreenEvents.HalalListChange(
                            names = names,
                            roomNo = roomNo,
                            menuTime = time,
                            changeTYPE = changeType
                        )
                    )
                }
            )
        }

        composable<Routes.TableScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {

            val args = it.toRoute<Routes.TableScreen>()
            val date = args.date

            val tableViewModel = hiltViewModel<TableViewModel>()
            val counts by tableViewModel.state.collectAsStateWithLifecycle()
            val menus by tableViewModel.menus.collectAsStateWithLifecycle()
            val sumOfMeals by tableViewModel.sumOfCounts.collectAsStateWithLifecycle()
            val isLoading by tableViewModel.isLoading.collectAsStateWithLifecycle()


            LaunchedEffect(date) {
                tableViewModel.getAllCounts(date)
                tableViewModel.getMenuOfDate(date)
                tableViewModel.getSumOfMeals(date)
            }

            val scope = rememberCoroutineScope()


            TableScreen(
                meals = counts,
                menus = menus,
                sumOfMeals = sumOfMeals,
                isLoading = isLoading,
                onHomeIconClick = {
                    navController.popBackStack<Routes.HomeScreen>(inclusive = false)
                },
                onNavIconClick = {
                    navController.popBackStack<Routes.HomeScreen>(inclusive = false)
                },
                onEditIconClick = {
                    sharedViewModel.loadRoomAndFloorsFromDb(date) { day, night ->

                        pagerViewModel.loadDataFromDb(date) {
                            scope.launch {
                                delay(500)
                                withContext(Dispatchers.Main) {
                                    navController.navigate(
                                        Routes.PagerScreen(date, day, night)
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }


    }
}