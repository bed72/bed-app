package github.bed72.core.data.repository.favorites

import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Character

interface FavoriteRepository {
    suspend fun getAllFavorite(): Flow<List<Character>>

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)
}