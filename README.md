## How to run this service with NR agent

### Download NR java agent

download 8.13 NR version.

### Update config

update the config in `start-with-nr.sh` file

### Run the service

```shell
./start-with-nr.sh
```

### Trigger the load test by gatling

```scala

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

// start script
//sbt "gatling:testOnly LocalTest"
class LocalTest extends Simulation {
  // Define test wide settings
  private val testDuration = 10.minutes

  val apiConfig = http
    .baseUrl(
      "http://localhost:8083"
    )
    .userAgentHeader("testable")
    .acceptEncodingHeader("gzip")

  val helloEndpoint = scenario("load test")
    .exec(
      http("endpoint request")
//        .get("/hello/lei1")
//        .get("/user/1")
//        .get("/helloWorld")
        .check(status.is(200)
)
    )


  setUp(
    helloEndpoint.inject(
      rampUsersPerSec(30).to(30).during(testDuration)
    ),
  ).protocols(apiConfig)
}

```
