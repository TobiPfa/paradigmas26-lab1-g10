import Subscription.Subscription
import Post.Post

object Main {
  def main(args: Array[String]): Unit = {
    val subscriptions: List[Subscription] = FileIO.readSubscriptions("subscriptions.json")
    
    val allPosts: List[Post] = subscriptions.flatMap { case (subredditName, url) =>
      println(s"Fetching posts from: $subredditName")
      val json = FileIO.downloadFeed(url)
      FileIO.parsePosts(subredditName, json)
    }
    
    val postsBySubreddit = allPosts.groupBy(_._1)
    postsBySubreddit.foreach { case (subreddit, posts) =>
      printReport(subreddit, posts)
    }
  }

  def printReport(subreddit: String, posts: List[Post]): Unit = {
    val totalScore = posts.foldLeft(0)((acc, post) => acc + post._3)
    val frequencies = Frequencies.wordFrequency(posts).take(10)
    val topPosts = posts.take(5)

    println(s"=== $subreddit ===")
    println(s"Total score: $totalScore")
    
    println("\nTop words:")
    frequencies.foreach { case (word, count) =>
      println(s"  $word: $count")
    }

    println("\nTop 5 posts:")
    topPosts.foreach { case (_, title, _, _, date, url) =>
      println(s"  - $title")
      println(s"    Date: $date")
      println(s"    URL:  $url")
    }

    println()  // blank line between subreddits
  }
}
