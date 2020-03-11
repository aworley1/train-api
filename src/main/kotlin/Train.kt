import TrainStatus.CANCELLED
import TrainStatus.DELAYED_OVER_THRESHOLD
import TrainStatus.DELAYED_UNDER_THRESHOLD
import TrainStatus.ON_TIME
import TrainStatus.UNKNOWN
import java.time.Duration
import java.time.LocalTime

const val DELAY_THRESHOLD = 2

data class Train(
        val scheduledTimeOfDeparture: String?,
        val estimatedTimeOfDeparture: String?,
        val isCancelled: Boolean,
        val isCancelledAtDestination: Boolean
) {
    fun getStatus(): TrainStatus {
        return when {
            isCancelled -> CANCELLED
            isCancelledAtDestination -> CANCELLED
            estimatedTimeOfDeparture.equals("On time", true) -> ON_TIME
            !estimatedTimeOfDeparture.isTime() -> DELAYED_OVER_THRESHOLD
            hasDelayUnderThreshold() -> DELAYED_UNDER_THRESHOLD
            hasDelayOverThreshold() -> DELAYED_OVER_THRESHOLD
            else -> UNKNOWN
        }
    }

    private fun calculateDelay(): Int? {
        val scheduledTimeOfDepartureTime = scheduledTimeOfDeparture?.toTime()
        val estimatedTimeOfDepartureTime = estimatedTimeOfDeparture?.toTime()

        if (scheduledTimeOfDepartureTime == null || estimatedTimeOfDepartureTime == null) {
            return null
        }

        return Duration.between(scheduledTimeOfDepartureTime, estimatedTimeOfDepartureTime)
                .toMinutes()
                .toInt()

    }

    private fun hasDelayUnderThreshold(): Boolean {
        val delay = calculateDelay()
        return when {
            delay == null -> false
            delay <= DELAY_THRESHOLD -> true
            else -> false
        }
    }

    private fun hasDelayOverThreshold(): Boolean {
        val delay = calculateDelay()
        return when {
            delay == null -> false
            delay > DELAY_THRESHOLD -> true
            else -> false
        }
    }
}

enum class TrainStatus {
    ON_TIME,
    CANCELLED,
    DELAYED_UNDER_THRESHOLD,
    DELAYED_OVER_THRESHOLD,
    UNKNOWN
}

private fun String?.isTime(): Boolean {
    return if (this?.toTime() != null) true else false
}

private fun String.toTime(): LocalTime? {
    return try {
        LocalTime.parse(this)
    } catch (e: Exception) {
        return null
    }
}

