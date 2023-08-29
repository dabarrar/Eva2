package com.example.eva2.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ListaDao {

    @Query("SELECT * FROM lista ORDER BY comprado")
    fun findAll(): List<Lista>
    @Query("SELECT COUNT(*) FROM lista ")
    fun contar(): Int
    @Insert
    fun insertar(lista: Lista):Long
    @Update
    fun actualizar(lista: Lista)
    @Delete
    fun eliminar(lista: Lista)

    }