package com.example.stepcounter.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.stepcounter.data.local.room.steps.DaysDatabase
//import com.example.stepcounter.utils.StepsCounter
import com.example.stepcounter.utils.StepsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StepModule {
    @Provides
    fun provideDayRoomDatabase(@ApplicationContext context: Context): DaysDatabase {
        return Room.databaseBuilder(
            context,
            DaysDatabase::class.java, "Days"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
//    @Provides
//    fun provideStepCounter(@ApplicationContext context: Context):StepsCounter{
//        return StepsCounter(context)
//    }

    @Provides
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "chanel_id")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat {
        val notificationManager =
            NotificationManagerCompat.from(context)
        val chanel = NotificationChannel(
            "chanel_id",
            "chanelName",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(chanel)
        return notificationManager
    }


    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("healthDatastorePreferences")
        }
    }

    @Provides
    @Singleton
    fun provideHealthManager(
        dataStore: DataStore<Preferences>,
    ): StepsManager = StepsManager(dataStore)
}