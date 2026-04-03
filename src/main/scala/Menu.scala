import Subscription.Subscription
import Post.Post
import scala.io.StdIn.readLine

object Menu {
    def mostrarSuscripciones(subscriptions: List[Subscription]): Unit = {
        println("=== SUSCRIPCIONES ===")

        subscriptions.zipWithIndex.foreach {
            case (s, i) =>
            println(s"${i + 1}. ${s._1}")
        }
    }


    def mostrarPosts(posts: List[Post]): Unit = {
        println("=== POSTS ===")

        posts.zipWithIndex.foreach {
            case (p, i) =>
            println(s"${i + 1}. ${p._2}")
        }
    }


    def elegir[A](lista: List[A], opcion: Int): Option[A] = lista.lift(opcion - 1)

    
    def leerLinea(tipo: Option[String]): Option[Int] = {
        tipo match {
            case Some("post") =>
            print("Elegí un post: ")

            case Some("sub") =>
            print("Elegí una suscripción: ")

            case _ =>
            println("Ingresá un número")
        }

        readLine().toIntOption
        }
}