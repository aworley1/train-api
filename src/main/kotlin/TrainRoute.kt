import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto

val trainLens = Body.auto<List<Train>>().toLens()

fun createTrainRoute(getTrains: GetTrains): HttpHandler {
    return {
        val departureStation = it.query("departureStation")!!
        val destinationStation = it.query("destinationStation")!!
        Response(Status.OK).with(trainLens of getTrains(departureStation, destinationStation))
    }
}