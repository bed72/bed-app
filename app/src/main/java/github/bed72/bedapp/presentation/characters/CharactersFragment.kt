package github.bed72.bedapp.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import github.bed72.bedapp.databinding.FragmentCharactersBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.base.BaseFragment
import github.bed72.bedapp.presentation.characters.adapters.CharactersAdapter
import github.bed72.bedapp.presentation.characters.adapters.CharactersLoadStateAdapter
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.core.domain.model.Character
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : BaseFragment<FragmentCharactersBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var charactersAdapter: CharactersAdapter

    override fun getViewBinding() = FragmentCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadState()
        handleCharactersPagingData()
    }

    private fun handleCharactersPagingData() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.charactersPagingData("").collect { pagingData ->
                    charactersAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun initCharactersAdapter() {
        charactersAdapter = CharactersAdapter(imageLoader) { character, view ->
            handleNavigation(view, character)
        }

        with(binding.recyclerCharacters) {
            scrollToPosition(0) // Set initial position
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter(
                    // Passed lambda function
                    charactersAdapter::retry
                )
            )
        }
    }

    private fun handleNavigation(view: View, character: Character) {
        val extras = FragmentNavigatorExtras(
            view to character.name
        )

        val directions = CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(
            character.name,
            DetailViewArg(
                characterId = character.id,
                name = character.name,
                imageUrl = character.imageUrl
            )
        )

        findNavController().navigate(directions, extras)
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                 binding.flipperCharacters.displayedChild = when (loadState.refresh) {
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }
                     is LoadState.NotLoading -> {
                         setShimmerVisibility(false)
                         FLIPPER_CHILD_CHARACTERS
                     }
                     is LoadState.Error -> {
                         setShimmerVisibility(false)

                         binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                             charactersAdapter.retry()
                         }
                         FLIPPER_CHILD_ERROR
                     }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            isVisible = visibility

            if (visibility) startShimmer() else startShimmer()
        }
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}