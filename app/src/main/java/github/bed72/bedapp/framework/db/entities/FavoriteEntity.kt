package github.bed72.bedapp.framework.db.entities

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import github.bed72.core.domain.model.Character
import github.bed72.core.data.constants.DbConstants

@Entity(tableName = DbConstants.FAVORITES_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey()
    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_ID)
    val id: Int,

    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_NAME)
    val name: String,

    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_IMAGE_URL)
    val imageUrl: String
)

fun List<FavoriteEntity>.toCharactersModel() = map { Character(it.id, it.name, it.imageUrl) }

