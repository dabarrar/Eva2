package com.example.eva2.DB

import  androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class Lista (
    @PrimaryKey(autoGenerate = true) val id:Int= 0,
    var lista:String,
    var comprado:Boolean

)
