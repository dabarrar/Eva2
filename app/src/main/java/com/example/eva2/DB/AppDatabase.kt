package com.example.eva2.DB

import  android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Lista::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun listaDao(): ListaDao

//patron singleton
    companion object {
        @Volatile
        private var BASE_DATOS : AppDatabase? = null
        fun getInstance(contexto: Context):AppDatabase {
            return BASE_DATOS ?: synchronized(this) {
                Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "ListaCompras.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }


}