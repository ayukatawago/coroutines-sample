package project

import retrofit2.Call

fun <T> Call<T>.responseBodyBlocking(): T {
    val response = this.execute()
    check(response.isSuccessful) {
        "failed with ${response.code()}: ${response.message()}\n${response.errorBody()}?.string()}"
    }
    return response.body()!!
}