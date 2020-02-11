package com.android.weatherapp.repo

import retrofit2.Response
import java.io.IOException

/**
 * Created by    :   Syed
 * Created on    :   06,February,2020.
 * Modified By   :
 * Modified On   :   06,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful && response.code() == 200 ){
            return response.body()!!
        }else{
            //@todo handle api exception
            throw ApiException(response.code().toString())
        }
    }

}

class ApiException(message: String): IOException(message)