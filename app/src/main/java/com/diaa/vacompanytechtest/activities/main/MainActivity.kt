package com.diaa.vacompanytechtest.activities.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diaa.vacompanytechtest.R
import com.diaa.vacompanytechtest.adpters.PendingAdapter
import com.diaa.vacompanytechtest.adpters.ResultAdapter
import com.diaa.vacompanytechtest.backgroundService.CalculationBackgroundService
import com.diaa.vacompanytechtest.model.PendingModel
import com.diaa.vacompanytechtest.model.ResultModel
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ResultCallBack, LocationListener {
    private lateinit var pendingAdapter: PendingAdapter
    private lateinit var resAdapter: ResultAdapter
    var locationManager: LocationManager? = null

    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestAppPermissions()
            return
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L,
            10f, this
        )
        isLocationEnabled(locationManager!!)

        init()
    }

    private fun requestAppPermissions() {
        val PERMISSIONS =
            arrayOf(
                 Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.INTERNET
            )
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions((this), PERMISSIONS, 1000)
        }
    }

    private fun hasPermissions(
        context: Context,
        permissions: Array<String>
    ): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(context, it)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun changeOperator(newOperator: Char) {

        if (et_equation.text.isNullOrEmpty())
            Toast.makeText(this, "please enter the numbers", Toast.LENGTH_SHORT).show()
        else if (et_equation.text.last() == ('*') || et_equation.text.last() == ('/') ||
            et_equation.text.last() == ('+') || et_equation.text.last() == ('-')
        ) {

            et_equation.text.replace(
                et_equation.text.lastIndex,
                et_equation.text.lastIndex + 1,
                newOperator.toString()
            )
            et_equation.setSelection(et_equation.text.lastIndex + 1)

            Log.e(TAG, "changeOperator: replace")
        } else {
            Log.e(TAG, "changeOperator: $newOperator")
            et_equation.text = et_equation.text.append(newOperator)
            et_equation.setSelection(et_equation.text.lastIndex + 1)
        }

    }

    private fun init() {


        rv_pending_eq.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_result.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        pendingAdapter = PendingAdapter()
        resAdapter = ResultAdapter()
        rv_pending_eq.adapter = pendingAdapter
        rv_result.adapter = resAdapter
        filterEquation()
        onClick()
    }

    private fun filterEquation() {
        et_equation.filters
    }

    private fun onClick() {
        bt_sub.setOnClickListener { changeOperator('-') }
        bt_add.setOnClickListener { changeOperator('+') }
        bt_mul.setOnClickListener { changeOperator('*') }
        bt_div.setOnClickListener { changeOperator('/') }


        bt_submit.setOnClickListener {
            if (et_equation.text.isNullOrEmpty())
                Toast.makeText(this, "please enter the equation", Toast.LENGTH_SHORT).show()
            else if (et_sec.text.isNullOrEmpty()) {
                Toast.makeText(this, "please enter the seconds", Toast.LENGTH_SHORT).show()
            } else if (et_equation.text.contains('*') || et_equation.text.contains('/') ||
                et_equation.text.contains('+') || et_equation.text.contains('-')
            ) {
                val intent = Intent(this, CalculationBackgroundService::class.java)
                intent.putExtra("equation", et_equation.text.toString())
                intent.putExtra("time", et_sec.text.toString().toInt())
                pendingAdapter.addItem(PendingModel(++index, et_equation.text.toString()))
                intent.putExtra("index", index)
                CalculationBackgroundService.enqueueWork(this, intent, this)
                et_equation.text.clear()
                et_sec.text.clear()

            }

        }


    }

    override fun result(resultModel: ResultModel) {
        runOnUiThread {
            resAdapter.addItem(resultModel)
            pendingAdapter.deleteItem(resultModel.index)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        tv_coordinates.text = location.latitude.toString() + " " + location.altitude.toString()
    }
}