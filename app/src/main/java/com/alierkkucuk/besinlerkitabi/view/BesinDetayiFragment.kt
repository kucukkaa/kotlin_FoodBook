package com.alierkkucuk.besinlerkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alierkkucuk.besinlerkitabi.R
import com.alierkkucuk.besinlerkitabi.databinding.BesinRecyclerRowBinding
import com.alierkkucuk.besinlerkitabi.databinding.FragmentBesinDetayiBinding
import com.alierkkucuk.besinlerkitabi.model.Besin
import com.alierkkucuk.besinlerkitabi.util.gorselIndir
import com.alierkkucuk.besinlerkitabi.util.placeHolderYap
import com.alierkkucuk.besinlerkitabi.viewmodel.BesinDetayiViewModel
import kotlinx.android.synthetic.main.fragment_besin_detayi.*


class BesinDetayiFragment : Fragment() {

    private lateinit var viewModel: BesinDetayiViewModel
    private var besinId = 0
    private lateinit var dataBinding: FragmentBesinDetayiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_besin_detayi, container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            besinId = BesinDetayiFragmentArgs.fromBundle(it).besinId
        }


        viewModel=ViewModelProviders.of(this).get(BesinDetayiViewModel::class.java)
        viewModel.roomVerisiniAl(besinId)

        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.besinLiveData.observe(viewLifecycleOwner, Observer { besin->
            besin?.let{
                dataBinding.secilenBesin = it

            }

        })
    }


}