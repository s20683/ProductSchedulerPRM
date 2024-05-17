package com.example.przeterminarz.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.przeterminarz.model.database.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ProductDb : RoomDatabase(){
    abstract val product : ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDb? = null

        fun open(context: Context): ProductDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDb::class.java,
                    "product.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}