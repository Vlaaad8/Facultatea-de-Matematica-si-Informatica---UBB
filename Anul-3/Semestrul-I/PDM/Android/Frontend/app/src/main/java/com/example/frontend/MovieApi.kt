import com.example.frontend.Movie
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MovieApi {

    @Multipart
    @POST("movies") // Asigură-te că ruta e corectă (ex: /movies sau /api/movies)
    fun addMovie(
        @Part photo: MultipartBody.Part?, // Poza propriu-zisă (poate fi null)
        @Part("name") name: RequestBody,
        @Part("premierDate") premierDate: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("running") running: RequestBody, // Trimitem "1" sau "0"
        @Part("owner_id") ownerId: RequestBody
    ): Call<Movie> // Returnează obiectul Movie salvat

    @Multipart
    @POST("movies")
    suspend fun addMovieMultipart(
        @Part photo: MultipartBody.Part?, // Poza (poate fi null)
        @Part("name") name: RequestBody,
        @Part("premierDate") premierDate: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("running") running: RequestBody,
        @Part("owner_id") ownerId: RequestBody
    ): Response<Movie>
}