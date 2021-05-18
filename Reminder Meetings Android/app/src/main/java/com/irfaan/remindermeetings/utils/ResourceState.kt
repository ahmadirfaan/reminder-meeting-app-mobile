package com.finalproject.utils

class ResourceState(val status : ResourceStatus, val data : Any?, val message :String?) {
    companion object {
        fun success(data : Any?) : ResourceState = ResourceState(status = ResourceStatus.SUCCESS, data = data, message = null)
        fun loading() : ResourceState = ResourceState(status = ResourceStatus.LOADING, data = null, message = null)
        fun failured(message: String?) : ResourceState = ResourceState(status = ResourceStatus.FAILURE, data = null, message = message)
    }
}