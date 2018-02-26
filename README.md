# spray-jwt
JWT library to use with spray-json and akka-http.

## Install
Add spray-jwt as dependency to your `build.sbt`:

```sbtshell
libraryDependencies ++= Seq(
  "com.github.janjaali" %% "spray-jwt" % "1.0.0"
)
```

To encode a JsValue to a JWT token:
```scala
import org.janjaali.sprayjwt.Jwt
import org.janjaali.sprayjwt.algorithms.HS256

val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
val jwtOpt = Jwt.encode(payload, "super_fancy_secret", HS256)
```

And vice versa to decode JWT token as a JsValue:
```scala
import org.janjaali.sprayjwt.Jwt
import org.janjaali.sprayjwt.algorithms.HS256

val token = "..."
val jsValueOpt = Jwt.decode(token, "super_fancy_secret")
```

## Supported algorithms
- HS256
- HS384
- HS512

- RS256
- RS384
- RS512
