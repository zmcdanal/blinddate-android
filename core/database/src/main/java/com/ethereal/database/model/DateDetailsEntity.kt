package com.ethereal.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ethereal.database.utils.csvToList
import com.ethereal.database.utils.toCsv
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.MapData


@Entity(
    tableName = "date_details"
)

data class DateDetailsEntity(
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "cuisine")
    val cuisine: String,

    @ColumnInfo(name = "keywords")
    val keywordsCsv: String,

    @ColumnInfo(name = "priceLevel")
    val priceLevel: Int,

    @ColumnInfo(name = "fastFood")
    val fastFood: Boolean,
)

fun DateDetailsEntity.asExternalModel() = DateDetails(
    date = date,
    cuisine = cuisine,
    keywords = keywordsCsv.csvToList(),
    priceLevel = priceLevel,
    fastFood = fastFood,
    mapData = MapData(
        userLocation = null,
        radiusMiles = 5
    )
)

fun DateDetails.toEntity(): DateDetailsEntity =
    DateDetailsEntity(
        id = 0,
        date = date,
        cuisine = cuisine,
        keywordsCsv = keywords.toCsv(),
        priceLevel = priceLevel,
        fastFood = fastFood
    )