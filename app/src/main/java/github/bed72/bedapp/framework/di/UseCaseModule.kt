package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.CheckFavoriteUseCase
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.core.usecase.RemoveFavoriteUseCase
import github.bed72.core.usecase.AddFavoriteUseCaseImpl
import github.bed72.core.usecase.CheckFavoriteUseCaseImpl
import github.bed72.core.usecase.GetCharactersUseCaseImpl
import github.bed72.core.usecase.RemoveFavoriteUseCaseImpl
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.core.usecase.GetCharacterCategoriesUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGetCharactersUseCase(useCase: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    fun bindCharacterCategoriesUseCase(
        useCase: GetCharacterCategoriesUseCaseImpl
    ): GetCharacterCategoriesUseCase

    @Binds
    fun bindAddFavoriteUseCase(useCase: AddFavoriteUseCaseImpl): AddFavoriteUseCase

    @Binds
    fun bindCheckFavoriteUseCase(useCase: CheckFavoriteUseCaseImpl): CheckFavoriteUseCase

    @Binds
    fun bindRemoveFavoriteUseCase(useCase: RemoveFavoriteUseCaseImpl): RemoveFavoriteUseCase
}