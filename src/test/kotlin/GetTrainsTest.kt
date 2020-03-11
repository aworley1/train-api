import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.thalesgroup.rtti._2007_10_10.ldb.commontypes.FilterType
import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken
import com.thalesgroup.rtti._2017_10_01.ldb.GetBoardRequestParams
import com.thalesgroup.rtti._2017_10_01.ldb.LDBServiceSoap
import com.thalesgroup.rtti._2017_10_01.ldb.StationBoardWithDetailsResponseType
import com.thalesgroup.rtti._2017_10_01.ldb.types.ArrayOfServiceItemsWithCallingPoints
import com.thalesgroup.rtti._2017_10_01.ldb.types.ServiceItemWithCallingPoints
import com.thalesgroup.rtti._2017_10_01.ldb.types.StationBoardWithDetails
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object GetTrainsTest : Spek({
    describe("Get Trains") {
        val capturedAccessToken = slot<AccessToken>()
        val capturedParameters = slot<GetBoardRequestParams>()
        val accessToken = AccessToken().apply { tokenValue = "test-token-value" }
        val ldbServiceSoap = mockk<LDBServiceSoap>()

        every {
            ldbServiceSoap.getDepBoardWithDetails(
                    capture(capturedParameters),
                    capture(capturedAccessToken)
            )
        } answers {
            mockResponse(null)
        }

        val getTrains = createGetTrains(ldbServiceSoap, accessToken)

        val trains = getTrains("HFN", "HHY")

        it("should call LdbService with correct access token") {
            assertThat(capturedAccessToken.captured.tokenValue).isEqualTo("test-token-value")
        }

        it("should call LdbService with correct departure station") {
            assertThat(capturedParameters.captured.crs).isEqualTo("HFN")
        }

        it("should call LdbService with correct destination station") {
            assertThat(capturedParameters.captured.filterCrs).isEqualTo("HHY")
            assertThat(capturedParameters.captured.filterType).isEqualTo(FilterType.TO)
        }

        it("should return scheduledTimeOfDeparture") {
            assertThat(trains[0].scheduledTimeOfDeparture).isEqualTo("14:13")
        }

        it("should return estimatedTimeOfDeparture") {
            assertThat(trains[0].estimatedTimeOfDeparture).isEqualTo("On time")
        }

        it("should return isCancelled as false when null") {
            assertThat(trains[0].isCancelled).isFalse()
        }

        it("should return isCancelledAtDestination as false when null") {
            assertThat(trains[0].isCancelledAtDestination).isFalse()
        }
    }

    describe("Get Trains for a cancelled train") {
        val capturedParameters = slot<GetBoardRequestParams>()
        val ldbServiceSoap = mockk<LDBServiceSoap>()

        every {
            ldbServiceSoap.getDepBoardWithDetails(
                    capture(capturedParameters),
                    any()
            )
        } answers {
            mockResponse(true)
        }

        val getTrains = createGetTrains(ldbServiceSoap, AccessToken())

        val trains = getTrains("HFN", "HHY")

        it("should return isCancelled as true") {
            assertThat(trains[0].isCancelled).isTrue()
        }

        it("should return isCancelledAtDestination as true") {
            assertThat(trains[0].isCancelledAtDestination).isTrue()
        }
    }
})

fun mockResponse(isCancelled: Boolean?) = StationBoardWithDetailsResponseType().apply {
    getStationBoardResult = StationBoardWithDetails().apply {
        trainServices = ArrayOfServiceItemsWithCallingPoints().apply {
            service += ServiceItemWithCallingPoints().apply {
                std = "14:13"
                etd = "On time"
                isIsCancelled = isCancelled
                isFilterLocationCancelled = isCancelled
            }
        }
    }
}