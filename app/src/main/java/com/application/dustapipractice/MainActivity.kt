package com.application.dustapipractice

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityManagerCompat
import com.application.dustapipractice.data.Repository
import com.application.dustapipractice.data.models.airquality.Grade
import com.application.dustapipractice.data.models.airquality.MeasuredValue
import com.application.dustapipractice.data.models.mornitoringstation.MornitoringStation
import com.application.dustapipractice.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null
    private val scope = MainScope()

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initVariables()
        requestLocationPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

        if (!locationPermissionGranted) {
            finish()
        } else {
            fetchAirQualityData()
        }
    }

    private fun initVariables() {
        // 마지막으로 확인된 위치 정보 얻기
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }


    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData() {
        // fetchData
        cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->

            scope.launch {
                val mornitoringStation =
                    Repository.getNearbyMornitoringStation(location.latitude, location.longitude)


                val measuredValue =
                    Repository.getLatestAirQualityData(mornitoringStation!!.stationName!!)


                displayAirQualityData(mornitoringStation,measuredValue!!)

            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun displayAirQualityData(
        mornitoringStation: MornitoringStation,
        measuredValue: MeasuredValue
    ) {

        binding.measuringStationNameTextView.text = mornitoringStation.stationName
        binding.measuringStationAddressTextView.text = mornitoringStation.addr

        (measuredValue.khaiGrade ?: Grade.UNKNOWN).let { grade ->
            binding.root.setBackgroundResource(grade.colorResID)
            binding.totalGradeLabelTextView.text = grade.label
            binding.totalGradeEmojiTextView.text = grade.emoji

        }

        with(measuredValue) {
            binding.fineDustInformationTextView.text =
                "미세먼지: ${pm10Value} ㎍/㎥ ${(pm10Grade ?: Grade.UNKNOWN).emoji}"

            binding.ultraFineDustInformationTextView.text =
                "초미세먼지: ${pm25Value} ㎍/㎥ ${(pm25Grade ?: Grade.UNKNOWN).emoji}"



            with(binding.so2Item) {
                labelTextView.text = "아황산가스"
                gradeTextView.text = (so2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$so2Value ppm"
            }
            with(binding.coItem) {
                labelTextView.text = "일산화탄소"
                gradeTextView.text = (coGrade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$coValue ppm"
            }
            with(binding.o3Item) {
                labelTextView.text = "오존"
                gradeTextView.text = (o3Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$o3Value ppm"
            }
            with(binding.no2Item) {
                labelTextView.text = "이산화질소"
                gradeTextView.text = (no2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$no2Value ppm"
            }
        }


    }


    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
    }
}