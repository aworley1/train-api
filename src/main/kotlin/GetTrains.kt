import com.thalesgroup.rtti._2007_10_10.ldb.commontypes.FilterType
import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken
import com.thalesgroup.rtti._2017_10_01.ldb.GetBoardRequestParams
import com.thalesgroup.rtti._2017_10_01.ldb.LDBServiceSoap
import com.thalesgroup.rtti._2017_10_01.ldb.types.ServiceItemWithCallingPoints

typealias GetTrains = (
        departureStation: String,
        destinationStation: String
) -> List<Train>

fun createGetTrains(ldbService: LDBServiceSoap, accessToken: AccessToken): GetTrains {
    return { departureStation, destinationStation ->
        val requestParams = GetBoardRequestParams().apply {
            crs = departureStation
            filterCrs = destinationStation
            filterType = FilterType.TO

        }
        val response = ldbService.getDepBoardWithDetails(requestParams, accessToken)

        response.getStationBoardResult
                .trainServices
                .service
                .map { mapTrain(it) }
    }
}

private fun mapTrain(service: ServiceItemWithCallingPoints) = Train(
        scheduledTimeOfDeparture = service.std,
        estimatedTimeOfDeparture = service.etd,
        isCancelled = service.isIsCancelled ?: false,
        isCancelledAtDestination = service.isFilterLocationCancelled ?: false
)

