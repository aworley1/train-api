import assertk.assertThat
import assertk.assertions.isEqualTo
import TrainStatus.CANCELLED
import TrainStatus.DELAYED_OVER_THRESHOLD
import TrainStatus.DELAYED_UNDER_THRESHOLD
import TrainStatus.ON_TIME
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TrainTest : Spek({
    describe("Train") {
        it("should determine when train is on time") {
            val train = Train(
                    scheduledTimeOfDeparture = "15:16",
                    estimatedTimeOfDeparture = "On time",
                    isCancelled = false,
                    isCancelledAtDestination = false
            )

            assertThat(train.getStatus()).isEqualTo(ON_TIME)
        }

        it("should determine when train is cancelled") {
            val train = Train(
                    scheduledTimeOfDeparture = "15:16",
                    estimatedTimeOfDeparture = "On time",
                    isCancelled = true,
                    isCancelledAtDestination = false
            )

            assertThat(train.getStatus()).isEqualTo(CANCELLED)
        }

        it("should determine when train is cancelled at destination") {
            val train = Train(
                    scheduledTimeOfDeparture = "15:16",
                    estimatedTimeOfDeparture = "On time",
                    isCancelled = false,
                    isCancelledAtDestination = true
            )

            assertThat(train.getStatus()).isEqualTo(CANCELLED)
        }

        it("should determine when train is delayed by indeterminate amount") {
            val train = Train(
                    scheduledTimeOfDeparture = "15:16",
                    estimatedTimeOfDeparture = "Blobby",
                    isCancelled = false,
                    isCancelledAtDestination = false
            )

            assertThat(train.getStatus()).isEqualTo(DELAYED_OVER_THRESHOLD)
        }

        it("should determine when train is delayed by less than threshold") {
            val train = Train(
                    scheduledTimeOfDeparture = "15:16",
                    estimatedTimeOfDeparture = "15:18",
                    isCancelled = false,
                    isCancelledAtDestination = false
            )

            assertThat(train.getStatus()).isEqualTo(DELAYED_UNDER_THRESHOLD)
        }

        it("should determine when train is delayed by more than threshold") {
            val train = Train(
                    scheduledTimeOfDeparture = "15:16",
                    estimatedTimeOfDeparture = "15:19",
                    isCancelled = false,
                    isCancelledAtDestination = false
            )

            assertThat(train.getStatus()).isEqualTo(DELAYED_OVER_THRESHOLD)
        }
    }
})