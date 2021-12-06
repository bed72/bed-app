package github.bed72.core.data.repository

import androidx.paging.PagingSource
import github.bed72.core.domain.model.Character

interface CharactersRepository {
    fun getCharacters(query: String): PagingSource<Int, Character>
}