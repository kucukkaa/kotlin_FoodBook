package com.alierkkucuk.besinlerkitabi.service

import com.alierkkucuk.besinlerkitabi.model.Besin
import io.reactivex.Single
import retrofit2.http.GET

public interface BesinAPI {
    //https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    fun getBesin() : Single<List<Besin>>
}