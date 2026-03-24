import Subscription.Subscription
import Post.Post

object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Subscription] = FileIO.readSubscriptions("subscriptions.json")
    
    val allPosts: List[Post] = subscriptions.flatMap { case (subredditName, url) =>
      println(s"Fetching posts from: $subredditName")
      val json = FileIO.downloadFeed(url)
      FileIO.parsePosts(subredditName, json)
    }
    
    val output = allPosts
      .map { post => Formatters.formatPost(post) }
      .mkString("\n---------------------------------------------------------------------\n")

    println(output)
  }
}
