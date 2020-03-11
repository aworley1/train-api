import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken
import com.thalesgroup.rtti._2017_10_01.ldb.Ldb
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val accessToken = System.getenv("NATIONAL_RAIL_TOKEN")
    createTrainRoute(
            createGetTrains(
                    ldbService = Ldb().ldbServiceSoap,
                    accessToken = AccessToken().apply { tokenValue = accessToken }
            )
    ).asServer(Jetty(8080)).start()
}