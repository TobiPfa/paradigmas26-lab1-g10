import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.{Try, Using}
import Subscription.Subscription
import Post.Post

object FileIO {
  implicit val formats: Formats = DefaultFormats
  

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(subscriptionsFile: String): List[Subscription] = {

    try{
     Using(Source.fromFile(subscriptionsFile)){source => source.mkString}.map(text => parse(text).extract[List[Map[String,String]]].map{
       sub => (sub("name"),sub("url"))
     }).getOrElse(List.empty[Subscription])

    // parse the json file and convert its content into a list of hashmaps
    //val json = parse(content)
    //val jsonMap = json.extract[List[Map[String, String]]]
    
    // map it to a List[Subscription]
    //jsonMap.map { sub =>
    //  (sub("name"), sub("url"))
    //}
    //}catch{
    //  case e : Exception =>
    //    println(s"Error reading subscriptions from ${subscriptionsFile}: ${e.getMessage}")
    //    List.empty
    }
  }
  
  // Pure function to parse post of a subreddit
    def parsePosts(subredditName: String, posts: String): List[Post] = {
    val json = parse(posts)
    val children = (json \ "data" \ "children").children

    // Filter empty posts (title or body with spaces or empty characters only)
    children.filter{ child => 
      val data       = child \ "data"
      val title      = (data \ "title").extract[String]
      val selftext   = (data \ "selftext").extract[String]
      title.trim.nonEmpty && selftext.trim.nonEmpty
    }

    .map { child =>
      val data       = child \ "data"
      val title      = (data \ "title").extract[String]
      val selftext   = (data \ "selftext").extract[String]
      val score   = (data \ "score").extract[Integer]
      val createdUtc = (data \ "created_utc").extract[Double].toLong
      val formattedDate = TextProcessing.formatDateFromUTC(createdUtc)
      val url = (data \ "url").extract[String]
      (subredditName, title, score, selftext, formattedDate,url)
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): Option[String] = {
          Using(Source.fromURL(url)){source => source.mkString}.toOption
  }
}
