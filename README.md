# VRaptor action cache

The main goal is to enable the cache of a html generated by a response of your controller's method. 

## Installing

Add to your pom:

```
<dependency>
	<groupId>br.com.caelum.vraptor</groupId>
	<artifactId>vraptor-action-cache</artifactId>
	<version>4.0.0-SNAPSHOT</version>
</dependency>

```

## Examples

```
  public class HomeController {
    @Cached(key="homeindex",duration=180,idleTime=60)
    public void index(){
      System.out.println("Passando por aqui...");
    }
  }
  
```

After the first request, the html generated by this method will be cached. The time that the
html will be in cache, is 180 seconds. If nobody tries to access this url in 60 seconds, the page will
be dropped. This is why we have the idleTime :).