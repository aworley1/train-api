import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.mockk
import org.http4k.core.Method
import org.http4k.core.Request
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TrainRouteTest : Spek({

    describe("train route") {
        val mockGetTrains = mockk<GetTrains>()
        val route = createTrainRoute(mockGetTrains)

        val response = route(Request(Method.GET, "/"))

        it("should return an empty array") {
            assertThat(response.bodyString()).isEqualTo("[]")
        }
    }
})