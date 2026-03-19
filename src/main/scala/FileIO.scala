import Subscription.Subscription
import scala.io.Source

object FileIO {
  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): List[Subscription] = {


    val source = Source.fromFile("subscriptions.json","UTF-8")
    try{

      //Do something
    }finally{
      source.close()
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
//List[Subscription] = ["{"name":"asjkdhasd","url":"akjshdkjasd"}"];