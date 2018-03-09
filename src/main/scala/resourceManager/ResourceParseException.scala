package resourceManager

/**
  * Created by kenneth on 09.03.18.
  */
final case class ResourceParseException(private val message: String = "",
                                        private val cause: Throwable = None.orNull)
                                        extends Exception {


}
