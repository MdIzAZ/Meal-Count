package com.music.mp3.spotify.mealcount.di

import android.content.Context
import androidx.room.Room
import com.music.mp3.spotify.mealcount.data.local.MealDB
import com.music.mp3.spotify.mealcount.data.repo.MealCountRepoImp
import com.music.mp3.spotify.mealcount.domain.repo.MealCountRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MealDB {

        return Room.databaseBuilder(context, MealDB::class.java, "meal_db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideMealCountRepo(
        db: MealDB
    ): MealCountRepo {
        return MealCountRepoImp(db)
    }









}

























