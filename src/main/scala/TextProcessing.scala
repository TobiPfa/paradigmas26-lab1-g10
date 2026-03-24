import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TextProcessing {
  // Pure function to format a date from UTC to yyyy-MM-dd HH:mm:ss
  def formatDateFromUTC(utc: Long): String = {
    val instant = Instant.ofEpochSecond(utc)
    val formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(ZoneId.systemDefault())
    formatter.format(instant)
  }
}
