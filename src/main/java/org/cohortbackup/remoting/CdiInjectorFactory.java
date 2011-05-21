package org.cohortbackup.remoting;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContext;

import org.jboss.resteasy.cdi.CdiConstructorInjector;
import org.jboss.resteasy.cdi.NoopPropertyInjector;
import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.core.ValueInjector;
import org.jboss.resteasy.spi.ConstructorInjector;
import org.jboss.resteasy.spi.InjectorFactory;
import org.jboss.resteasy.spi.MethodInjector;
import org.jboss.resteasy.spi.PropertyInjector;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jozef Hartinger
 */
public class CdiInjectorFactory implements InjectorFactory
{
   private static final Logger log = LoggerFactory.getLogger(CdiInjectorFactory.class);
   private PropertyInjector noopPropertyInjector = new NoopPropertyInjector();
   private InjectorFactory delegate;
   private BeanManager manager;
   private Map<Class<?>, Class<?>> sessionBeanInterface;
   private ResteasyCdiExtension extension;

   public CdiInjectorFactory()
   {
      delegate = ResteasyProviderFactory.getInstance().getInjectorFactory();
      manager = lookupBeanManager();
      extension = lookupResteasyCdiExtension();
      sessionBeanInterface = extension.getSessionBeanInterface();
      ResteasyProviderFactory.getInstance().addStringConverter(UUIDStringConverter.class);
   }


   public ConstructorInjector createConstructor(Constructor constructor)
   {
      Class<?> clazz = constructor.getDeclaringClass();

      if (!manager.getBeans(constructor.getDeclaringClass()).isEmpty())
      {
         log.debug("Using CdiConstructorInjector for class {}.", clazz);
         return new CdiConstructorInjector(clazz, manager);
      }

      if (sessionBeanInterface.containsKey((constructor.getDeclaringClass())))
      {
         Class<?> intfc = sessionBeanInterface.get(clazz);
         log.debug("Using interface {} for lookup of Session Bean {}.", intfc, clazz);
         return new CdiConstructorInjector(intfc, manager);
      }

      log.debug("No CDI beans found for {}. Using default ConstructorInjector.", clazz);
      return delegate.createConstructor(constructor);
   }

   public MethodInjector createMethodInjector(Class root, Method method)
   {
      return delegate.createMethodInjector(root, method);
   }

   public PropertyInjector createPropertyInjector(Class resourceClass)
   {
      // JAX-RS property injection is performed twice. Firstly by the JaxrsInjectionTarget
      // wrapper and then again by RESTEasy. To eliminate this, we return a noop PropertyInjector.
      return noopPropertyInjector;
   }

   public ValueInjector createParameterExtractor(Class injectTargetClass, AccessibleObject injectTarget, Class type, Type genericType, Annotation[] annotations)
   {
      return delegate.createParameterExtractor(injectTargetClass, injectTarget, type, genericType, annotations);
   }

   public ValueInjector createParameterExtractor(Class injectTargetClass, AccessibleObject injectTarget, Class type,
                                                 Type genericType, Annotation[] annotations, boolean useDefault)
   {
      return delegate.createParameterExtractor(injectTargetClass, injectTarget, type, genericType, annotations, useDefault);
   }

   /**
    * Do a lookup for BeanManager instance. JNDI and ServletContext is searched.
    *
    * @return BeanManager instance
    */
   protected BeanManager lookupBeanManager()
   {
      BeanManager beanManager = null;

      // Look for BeanManager in ServletContext
      ServletContext servletContext = ResteasyProviderFactory.getContextData(ServletContext.class);
      beanManager = (BeanManager) servletContext.getAttribute("org.jboss.weld.environment.servlet.javax.enterprise.inject.spi.BeanManager");
      if (beanManager != null)
      {
         log.info("Found BeanManager in ServletContext");
         return beanManager;
      }

      throw new RuntimeException("Unable to lookup BeanManager.");
   }

   /**
    * Lookup ResteasyCdiExtension instance that was instantiated during CDI bootstrap
    *
    * @return ResteasyCdiExtension instance
    */
   private ResteasyCdiExtension lookupResteasyCdiExtension()
   {
      Set<Bean<?>> beans = manager.getBeans(ResteasyCdiExtension.class);
      Bean<?> bean = manager.resolve(beans);
      if (bean == null)
      {
         throw new IllegalStateException("Unable to obtain ResteasyCdiExtension instance.");
      }
      CreationalContext<?> context = manager.createCreationalContext(bean);
      return (ResteasyCdiExtension) manager.getReference(bean, ResteasyCdiExtension.class, context);
   }
}
