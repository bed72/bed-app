package github.bed72.bedapp.framework.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import github.bed72.bedapp.framework.network.response.ComicResponse
import github.bed72.bedapp.framework.network.response.CharacterResponse
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.bedapp.framework.network.response.EventResponse

interface MarvelApi {
    @GET("characters")
    suspend fun getCharacters(
        @QueryMap
        queries: Map<String, String>
    ) : DataWrapperResponse<CharacterResponse>

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path("characterId")
        characterId: Int
    ) : DataWrapperResponse<ComicResponse>

    @GET("characters/{characterId}/events")
    suspend fun getEvents(
        @Path("characterId")
        characterId: Int
    ) : DataWrapperResponse<EventResponse>
}