import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken
import com.thalesgroup.rtti._2017_10_01.ldb.Ldb
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val accessToken = System.getenv("NATIONAL_RAIL_TOKEN")
    ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive).then(
            createTrainRoute(
                    createGetTrains(
                            ldbService = Ldb().ldbServiceSoap,
                            accessToken = AccessToken().apply { tokenValue = accessToken }
                    )
            )
    ).asServer(Jetty(8080)).start()
}