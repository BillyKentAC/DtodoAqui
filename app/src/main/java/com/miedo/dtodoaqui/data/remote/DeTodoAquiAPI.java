package com.miedo.dtodoaqui.data.remote;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeTodoAquiAPI {

    // Login
    @POST("/api/sign_in")
    public Call<ResponseBody> loginUser(@Body RequestBody body);

    // Registro usuario
    @POST("/api/users")
    public Call<ResponseBody> registerUser(@Body RequestBody body);

    // Buscar establecimientos
    @GET("/api/search")
    public Call<ResponseBody> searchEstablishments(@Query("keyword") String keyword, @Query("location") String location, @Query("categories") String category);

    //  Obtener profile
    @GET("/api/my_profile")
    public Call<ResponseBody> getProfile(@Header("Authorization") String bearer);

    // Crear profile
    @POST("/api/my_profile")
    public Call<ResponseBody> registerProfile(@Header("Authorization") String bearer, @Body RequestBody body);

    // Actualizar profile
    @PUT("/api/my_profile")
    public Call<ResponseBody> updateProfile(@Header("Authorization") String bearer, @Body RequestBody body);

    // Obtener categorias
    @GET("/api/categories")
    public Call<ResponseBody> getCategories();

    // Obtener locations
    @GET("/api/location")
    public Call<ResponseBody> getLocations();

    @GET("/api/listings/{id}")
    public Call<ResponseBody> getEstablishment(@Path("id") int id);

    @POST("/api/listings")
    public Call<ResponseBody> registerEstablishment(@Body RequestBody body);

    @GET("/api/listings/{id}/reviews")
    public Call<ResponseBody> getEstablishmentReviews(@Path("id") int id);

    @POST("/api/reviews")
    public Call<ResponseBody> postReview(@Body RequestBody body);

    @POST("/api/ratings")
    public Call<ResponseBody> postRating(@Body RequestBody body);

    @POST("/api/reports")
    public Call<ResponseBody> postReport(@Body RequestBody body);

    @POST("/api/claims")
    public Call<ResponseBody> postClaim(@Body RequestBody body);

    @GET("/api/users/{id}")
    public Call<ResponseBody> getUserWithId(@Path("id") int id);

    // Subir imagen pal profile
    @POST("/api/upload_image")
    public Call<ResponseBody> uploadImage(@Header("Authorization") String bearer, @Body RequestBody body);

    @GET("/api/reviews/{id}/images")
    public Call<ResponseBody> getImagesFromReview(@Path("id") int id);

    @GET("/api/profile/{id}/detail")
    Call<ResponseBody> getProfileDetailById(@Path("id") int id);

    @GET("/api/listings/{id}/ratings")
    public Call<ResponseBody> getListingRatings(@Path("id") int id);

    @PUT("/api/ratings/{id}")
    public Call<ResponseBody> putRating(@Path("id") int id, @Body RequestBody body);
}
