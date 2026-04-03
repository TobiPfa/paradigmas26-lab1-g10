import Subscription.Subscription
import Post.Post
import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]): Unit = {
    val subscriptions: List[Subscription] = FileIO.readSubscriptions("subscriptions.json")
    
    val allPosts: List[Post] = subscriptions.flatMap { case (subredditName, url) =>
      println(s"Fetching posts from: $subredditName")
      val json = FileIO.downloadFeed(url)
      FileIO.parsePosts(subredditName, json)
    }
    
    val postsBySubreddit = allPosts.groupBy(_._1)

    println("1. Leer post especifico")
    println("2. Ver estadisticas")
    val input = Menu.leerLinea(Some("extra"))
    input match {
      case Some(1) => 
          Menu.mostrarSuscripciones(subscriptions)
          val resultado =
            for {
              numSub  <- Menu.leerLinea(Some("sub"))
              sub     <- Menu.elegir(subscriptions, numSub)
              posts   <- postsBySubreddit.get(sub._1)
              _        = Menu.mostrarPosts(posts)
              numPost <- Menu.leerLinea(Some("post"))
              post    <- Menu.elegir(posts, numPost)
            } yield post

          resultado match {
            case Some(post) =>
              println("\n---------------------------------------------------------------------\n")
              println(Formatters.formatPost(post))
              println("\n---------------------------------------------------------------------\n")

            case None =>
              println("Entrada inválida o selección incorrecta")
          }
      case Some(2) =>
        postsBySubreddit.foreach { case (subreddit, posts) =>
          printReport(subreddit, posts)
        }
      
      case _ =>
        println("Entrada invalida")
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
