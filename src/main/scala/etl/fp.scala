package etl

object fp {

  sealed trait Monad[F[_]] {
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(f andThen pure)

    def pure[A](a: A): F[A]
  }

  object Monad {
    def apply[F[_] : Monad]: Monad[F] = implicitly[Monad[F]]

    implicit class MonadOps[A, F[_]](val fa: F[A]) extends AnyVal {
      def flatMap[B](f: A => F[B])(implicit F: Monad[F]): F[B] = F.flatMap(fa)(f)

      def map[B](f: A => B)(implicit F: Monad[F]): F[B] = F.map(fa)(f)
    }

  }

}
