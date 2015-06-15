import java.lang.reflect.Constructor

import com.google.inject.{Guice, AbstractModule}
import controllers.{AngularMailTemplates, JsonViewTemplates}
import models.users.BasicUser
import play.api.GlobalSettings
import securesocial.controllers.{MailTemplates, ViewTemplates}
import securesocial.core.RuntimeEnvironment
import securesocial.core.authenticator.{HttpHeaderAuthenticatorBuilder, CookieAuthenticatorBuilder}
import securesocial.core.services.{AuthenticatorService, UserService}
import services.{SimpleUUIDGenerator, UUIDGenerator}
import slick.{SlickAuthenticatorStore, SlickUserService}

/**
 * Set up the Guice injector and provide the mechanism for return objects from the dependency graph.
 */
object Global extends GlobalSettings {

  /**
   * Bind types such that whenever UUIDGenerator is required, an instance of SimpleUUIDGenerator will be used.
   */
  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[UUIDGenerator]).to(classOf[SimpleUUIDGenerator])
    }
  })

  /**
   * Controllers must be resolved through the application context. There is a special method of GlobalSettings
   * that we can override to resolve a given controller. This resolution is required by the Play router.
   */
//  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  object MyRuntimeEnvironment extends RuntimeEnvironment.Default[BasicUser] {
    override val userService: UserService[BasicUser] = new SlickUserService
    override lazy val authenticatorService: AuthenticatorService[BasicUser] = new AuthenticatorService[BasicUser](
      new CookieAuthenticatorBuilder[BasicUser](new SlickAuthenticatorStore, idGenerator),
      new HttpHeaderAuthenticatorBuilder[BasicUser](new SlickAuthenticatorStore, idGenerator)
    )
    override lazy val viewTemplates: ViewTemplates = JsonViewTemplates
    override lazy val mailTemplates: MailTemplates = AngularMailTemplates
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    val instance  = controllerClass.getConstructors.find { c =>
      val params = c.getParameterTypes
      params.length == 1 && params(0) == classOf[RuntimeEnvironment[BasicUser]]
    }.map {
      _.asInstanceOf[Constructor[A]].newInstance(MyRuntimeEnvironment)
    }
    instance.getOrElse(injector.getInstance(controllerClass))
  }
}
