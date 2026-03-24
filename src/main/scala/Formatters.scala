import Post.Post

object Formatters {
  // Pure function to format posts
  def formatPost(post: Post): String = { 
    val (subreddit, title, selftext, formattedDate) = post
    s"Subreddit: $subreddit\nTile: $title\nDate: $formattedDate\nText:\n$selftext"
  }

  // Pure function to format posts from a subscription
  def formatSubscription(url: String, posts: String): String = {
    val header = s"\n${"=" * 80}\nPosts from: $url \n${"=" * 80}"
    val formattedPosts = posts.take(80)
    header + "\n" + formattedPosts
  }
}
