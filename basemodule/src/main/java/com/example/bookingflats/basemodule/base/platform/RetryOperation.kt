package com.example.bookingflats.basemodule.base.platform

import com.example.bookingflats.basemodule.utils.Loading

class RetryOperation(val showLoading: Loading, val function: suspend () -> Any)