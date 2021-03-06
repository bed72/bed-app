package github.bed72.bedapp.presentation.detail.redux

import github.bed72.bedapp.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlin.coroutines.CoroutineContext
import androidx.lifecycle.MutableLiveData
import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event
import github.bed72.bedapp.presentation.extensions.watchStatus
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.bedapp.presentation.detail.entities.DetailChildViewEntity
import github.bed72.bedapp.presentation.detail.entities.DetailParentViewEntity
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData.Actions.Load
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData.States.Error
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData.States.Empty
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData.States.Loading
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData.States.Success
import github.bed72.core.usecase.GetCharacterCategoriesUseCase.GetCharacterCategoriesParams

class LoadLiveData(
    private val coroutineContext: CoroutineContext,
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) {
    private val action = MutableLiveData<Actions>()
    /**
    * switchMap -> transforma uma entrada X em uma saída Y
    * toda vez que recebe um novo valor este e disparado
    **/
    val state: LiveData<States> = action.switchMap { action ->
        /**
         * Criamos um LiveData
         **/
        liveData(coroutineContext) {
            when(action) {
                is Load ->
                    getCharacterCategoriesUseCase(
                        GetCharacterCategoriesParams(action.characterId)
                    ).watchStatus(
                        error = { emit(Error) },
                        loading = { emit(Loading) },
                        success = { data -> emit(handleViewEntity(data)) }
                    )
            }
        }
    }

    fun load(characterId: Int) {
        action.value = Load(characterId)
    }

    private fun handleViewEntity(data: Pair<List<Comic>, List<Event>>): States {
        val parentList = verifyData(data.first, data.second)

        return if (parentList.isNotEmpty()) Success(parentList) else Empty
    }

    private fun verifyData(
        comics: List<Comic>,
        events: List<Event>
    ): MutableList<DetailParentViewEntity> {
        val detailParent = mutableListOf<DetailParentViewEntity>()

        if (comics.isNotEmpty())
            comics.map { comic  ->
                DetailChildViewEntity(comic.id, comic.imageUrl)
            }.also { details ->
                detailParent.add(DetailParentViewEntity(R.string.details_comics_category, details))
            }

        if (events.isNotEmpty())
            events.map { event  ->
                DetailChildViewEntity(event.id, event.imageUrl)
            }.also { details ->
                detailParent.add(DetailParentViewEntity(R.string.details_events_category, details))
            }

        return detailParent
    }

    sealed class Actions {
        data class Load(val characterId: Int): Actions()
    }

    sealed class States {
        object Empty: States()
        object Error : States()
        object Loading : States()
        data class Success(val detailParentList: List<DetailParentViewEntity>) : States()
    }
}