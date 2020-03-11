import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.query
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TrainRouteTest : Spek({

    describe("train route") {
        val mockGetTrains = mockk<GetTrains>()
        every { mockGetTrains(any(), any()) } returns emptyList()
        val route = createTrainRoute(mockGetTrains)

        val response = route(
                Request(Method.GET, Uri.of("/")
                        .query("departureStation", "departureStation")
                        .query("destinationStation", "destinationStation")
                )
        )

        it("should call GetTrains with correct stations") {
            verify { mockGetTrains("departureStation", "destinationStation") }
        }

        it("should return an empty array") {
            assertThat(response.bodyString()).isEqualTo("[]")
        }
    }
})