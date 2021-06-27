package com.example.bookingflats.features.flats.screens.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bookingflats.R
import com.example.bookingflats.basemodule.base.platform.BaseFragment
import com.example.bookingflats.basemodule.utils.MessageUtils.showDialog
import com.example.bookingflats.basemodule.utils.convertLongToTime
import com.example.bookingflats.basemodule.utils.limitRange
import com.example.bookingflats.basemodule.utils.viewbinding.viewBinding
import com.example.bookingflats.databinding.FragmentPreviewBinding
import com.example.bookingflats.features.flats.module.domain.FilterOption
import com.google.android.material.datepicker.MaterialDatePicker

class PreviewFragment : BaseFragment<PreviewViewModel>() {
    private val binding by viewBinding(FragmentPreviewBinding::bind)
    var startDate: Long? = null
    var endDate: Long? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edCalendar.setOnClickListener {
            val datePickerDialog =
                MaterialDatePicker.Builder.dateRangePicker()
            datePickerDialog.setCalendarConstraints(limitRange().build())
            val picker = datePickerDialog.build()
            picker.show(childFragmentManager, picker.toString())

            picker.addOnPositiveButtonClickListener {
                startDate = it.first
                endDate = it.second

                val time = convertLongToTime(startDate!!) + " - " + convertLongToTime(endDate!!)
                binding.edCalendar.setText(time)

            }
        }

        binding.buttonApply.setOnClickListener {
            if (startDate != null) {
                findNavController().navigate(
                    PreviewFragmentDirections.actionNavPreviewFragmentToNavFlatsFragment(
                        FilterOption(
                            startDate = startDate!!,
                            endDate = endDate!!,
                            numberOfBedrooms =
                            if (binding.etNumberOfRooms.text.toString().isBlank())
                                null
                            else binding.etNumberOfRooms.text.toString().toInt()
                        )
                    )
                )
            } else
                showDialog("Please choose start date and end date")


        }
    }


}