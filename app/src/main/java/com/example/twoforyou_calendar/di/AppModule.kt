package com.example.twoforyou_calendar.di

import android.content.Context
import androidx.room.Room
import com.example.twoforyou_calendar.data.local.ScheduleDao
import com.example.twoforyou_calendar.data.local.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesSchedule(scheduleDatabase: ScheduleDatabase): ScheduleDao =
        scheduleDatabase.ScheduleDao()

    @Singleton
    @Provides
    fun providesScheduleDatabase(@ApplicationContext context : Context) : ScheduleDatabase =
        Room.databaseBuilder(
            context,
            ScheduleDatabase::class.java,
            "schedule_database"
        )
            .fallbackToDestructiveMigration()
            .build()

}