package com.alierkkucuk.besinlerkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.alierkkucuk.besinlerkitabi.model.Besin
import com.alierkkucuk.besinlerkitabi.service.BesinAPIService
import com.alierkkucuk.besinlerkitabi.service.BesinDatabase
import com.alierkkucuk.besinlerkitabi.util.OzelSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application) : BaseViewModel(application) {
    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()
    private var guncellemeZamani = 10*60*1000*1000*1000L

    private val besinAPIService = BesinAPIService()
    private val disposable = CompositeDisposable()
    private val ozelSharedPreferences = OzelSharedPreferences(getApplication())

    fun refreshData() {

        val kaydedilmeZamani = ozelSharedPreferences.zamaniAl()

        if(kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani){
            verileriSqLitetanAl()
        }else{
            verileriInternettenAl()
        }
    }

    fun refreshFromInternet(){
        verileriInternettenAl()
    }

    private fun verileriSqLitetanAl(){
        besinYukleniyor.value = true

        launch {
            var besinListesi = BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(), "Besinleri Roomdan Aldık", Toast.LENGTH_LONG).show()
        }
    }

    private fun verileriInternettenAl(){
        besinYukleniyor.value=true

        disposable.add(
            besinAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Besin>>(){
                    override fun onSuccess(t: List<Besin>) {
                        sqliteSakla(t)
                        Toast.makeText(getApplication(), "Besinleri Internetten Aldık", Toast.LENGTH_LONG).show()
                    }
                    override fun onError(e: Throwable) {
                        besinHataMesaji.value = true
                        besinYukleniyor.value = false
                        e.printStackTrace()
                    }
                }
                )
        )

    }

    private fun besinleriGoster(besinlerListesi : List<Besin>){
        besinler.value = besinlerListesi
        besinHataMesaji.value = false
        besinYukleniyor.value = false
    }

    private fun sqliteSakla(besinlerListesi : List<Besin>){
        launch {

            val dao = BesinDatabase(getApplication()).besinDao()

            dao.deleteAllBesin()
            val uuidListesi =  dao.insertAll(*besinlerListesi.toTypedArray())

            var i = 0
            while(i < besinlerListesi.size){
                besinlerListesi[i].uuid = uuidListesi[i].toInt()
                i++
            }

            besinleriGoster(besinlerListesi)
        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }

}