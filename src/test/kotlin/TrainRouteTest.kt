import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.query
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.http4k.format.Jackson.asJsonObject

object TrainRouteTest : Spek({

    describe("train route") {
        val mockGetTrains = mockk<GetTrains>()
        every { mockGetTrains(any(), any()) } returns listOf(
                Train(
                        scheduledTimeOfDeparture = "scheduledTimeOfDeparture",
                        estimatedTimeOfDeparture = "estimatedTimeOfDeparture",
                        isCancelled = false,
                        isCancelledAtDestination = false
                )
        )

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

        it("should return an array of trains") {
            val firstTrain = response.bodyString().asJsonObject()[0]
            assertThat(firstTrain["scheduledTimeOfDeparture"].asText()).isEqualTo("scheduledTimeOfDeparture")
            assertThat(firstTrain["estimatedTimeOfDeparture"].asText()).isEqualTo("estimatedTimeOfDeparture")
            assertThat(firstTrain["isCancelled"].asBoolean()).isFalse()
            assertThat(firstTrain["isCancelledAtDestination"].asBoolean()).isFalse()
            assertThat(firstTrain["status"].asText()).isEqualTo("DELAYED_OVER_THRESHOLD")
        }
    }
})