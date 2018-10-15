package com.example.pedro.feedsense.repository

import com.example.pedro.feedsense.models.ReactionModel
import com.example.pedro.feedsense.models.SessionModel
import com.example.pedro.feedsense.models.User
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

interface FeedsenseService {

    @POST("sessions")
    fun createSession(@Body sessionModel: SessionModel): Observable<String>

    @PUT("sessions/{sessionId}/{guestId}")
    fun joinSession(@Path("sessionId") sessionId: String,
                    @Path("guestId") guestId: String): Observable<Any>

    @POST("comments")
    fun reactToSession(@Body reaction: ReactionModel): Completable

    @GET("comments/{sessionId}")
    fun fetchComments(@Path("sessionId") sessionId: String): Observable<ReactionModel>

    @POST("users")
    fun registerUser(@Body user: User): Observable<User>
}