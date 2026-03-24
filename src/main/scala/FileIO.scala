import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.util.Using
import TextProcessing._

import Subscription.Subscription
import Post.Post

object FileIO {
  implicit val formats: Formats = DefaultFormats

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(subscriptionsFile: String): List[Subscription] = {
    val content = Using(Source.fromFile(subscriptionsFile)) { source =>
      source.mkString
    }.get

    // parse the json file and convert its content into a list of hashmaps
    val json = parse(content)
    val jsonMap = json.extract[List[Map[String, String]]]
    
    // map it to a List[Subscription]
    jsonMap.map { sub =>
      (sub("name"), sub("url"))
    }
  }
  
  // Pure function to parse post of a subreddit
  def parsePosts(subredditName: String, posts: String): List[Post] = {
    val json = parse(posts)
    val children = (json \ "data" \ "children").children
    
    children.map { child =>
      val data       = child \ "data"
      val title      = (data \ "title").extract[String]
      val selftext   = (data \ "selftext").extract[String]
      val createdUtc = (data \ "created_utc").extract[Double].toLong
      val formattedDate = TextProcessing.formatDateFromUTC(createdUtc)
      (subredditName, title, selftext, formattedDate)
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
