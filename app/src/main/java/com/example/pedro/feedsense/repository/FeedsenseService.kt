package com.example.pedro.feedsense.repository

import com.example.pedro.feedsense.models.SessionModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FeedsenseService {

    @POST("sessions")
    fun createSession(@Body sessionModel: SessionModel): Observable<String>

    @GET("sessions")
    fun listSessions(): Observable<List<SessionModel>>
}