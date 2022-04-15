package github.bed72.bedapp.framework.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

import github.bed72.core.usecase.base.CoroutinesDispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class AppCoroutinesTestDispatchers(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
) : CoroutinesDispatchers {
    override fun default(): CoroutineDispatcher = testDispatcher
    override fun io(): CoroutineDispatcher = testDispatcher
    override fun main(): CoroutineDispatcher = testDispatcher
    override fun unconfined(): CoroutineDispatcher = testDispatcher
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesTestModule {

    @Provides
    fun provideTestDispatchers(): CoroutinesDispatchers = AppCoroutinesTestDispatchers()
}