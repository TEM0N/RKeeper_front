package an.imation.myapplication

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Timeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

class FakeOkHttpClient(private val response: Response? = null, private val error: IOException? = null) : OkHttpClient() {
    override fun newCall(request: Request): Call {
        return object : Call {
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
            override fun isCanceled(): Boolean = false
            override fun cancel() {}
            override fun clone(): Call {
                return this // Вернем сам себя, чтобы избежать NotImplemented
            }

            override fun request(): Request = Request.Builder().url("http://10.0.2.2:8080/auth/register").build()
            override fun timeout(): Timeout = Timeout()
        }
    }
}

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    private lateinit var activity: RegisterActivity

    @Before
    fun setUp() {
        ActivityScenario.launch(RegisterActivity::class.java).onActivity { act ->
            this.activity = act
        }
    }

    @Test
    fun testSuccessfulRegistration() {

        val token = "some_token"
        val jsonResponse = "{\"message\": \"Registration successful\", \"token\": \"$token\"}"
        val responseBody = ResponseBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonResponse)

        val response = Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url("http://10.0.2.2:8080/auth/register").build())
            .body(responseBody)
            .build()

        val fakeClient = FakeOkHttpClient(response)

        activity = RegisterActivity(fakeClient)

        activity.email.setText("test@mail.ru")
        activity.password.setText("password123")
        activity.repeatPassword.setText("password123")

        activity.sendPostRequest()
        assertEquals(token, activity.authToken)
    }

    @Test
    fun testNetworkErrorHandling() {
        val errorMessage = "Network error"
        val fakeClient = FakeOkHttpClient(error = IOException(errorMessage))

        activity = RegisterActivity(fakeClient)

        activity.email.setText("test@mail.ru")
        activity.password.setText("password123")
        activity.repeatPassword.setText("password123")

        activity.sendPostRequest()
        assertEquals("some_token", activity.authToken)
    }
}