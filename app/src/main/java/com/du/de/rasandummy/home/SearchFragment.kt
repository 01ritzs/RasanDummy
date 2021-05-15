package com.du.de.rasandummy.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.du.de.rasandummy.R
import com.du.de.rasandummy.db.Product
import com.du.de.rasandummy.util.AppData
import com.du.de.rasandummy.util.ProductUtil

class SearchFragment : Fragment(), OnProductSelectListener {

    private lateinit var mListener: OnProductSelectListener
    private var products: MutableList<Product>? = null
    private var productsAdapter: ProductsAdapter? = null
    private lateinit var rvProducts: RecyclerView

    companion object {
        const val TAG = "SearchFragment"

        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvProducts = view.findViewById(R.id.rvProducts)
        products = AppData.getInstance().products
        initRecyclerView()
    }

    private fun initRecyclerView() {
        products?.let {
            rvProducts.setHasFixedSize(true)
            rvProducts.layoutManager = GridLayoutManager(this.activity, 2)
            productsAdapter = ProductsAdapter(it, this)
            rvProducts.adapter = productsAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProductSelectListener) {
            mListener = context as OnProductSelectListener
        } else {
            throw RuntimeException(context.toString()
                    + " must implement ItemClickListener")
        }
    }

    override fun onSelected(product: Product) {
        mListener.onSelected(product)
    }

    fun searchWith(searchText: String) {
        productsAdapter?.setList(ProductUtil.filter(products, searchText))
    }
}