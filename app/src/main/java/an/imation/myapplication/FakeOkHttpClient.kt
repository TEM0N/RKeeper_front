package an.imation.myapplication

import okhttp3.*
import okio.Timeout
import java.io.IOException


class FakeOkHttpClient(private val response: Response? = null, private val error: IOException? = null) : OkHttpClient() {
    override fun newCall(request: Request): Call {
        return FakeCall(response, error)
    }
}

class FakeCall(private val response: Response?, private val error: IOException?) : Call {
    override fun enqueue(responseCallback: Callback) {
        if (error != null) {
            responseCallback.onFailure(this, error)
        } else {
            responseCallback.onResponse(this, response!!)
        }
    }

    override fun execute(): Response {
        throw UnsupportedOperationException("This is a fake client")
    }

    override fun isExecuted(): Boolean = false
    override fun cancel() {}
    override fun clone(): Call {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean = false
    override fun request(): Request = Request.Builder().url("http://fake.url").build()
    override fun timeout(): Timeout = Timeout.NONE // Или установите нужным образом
}