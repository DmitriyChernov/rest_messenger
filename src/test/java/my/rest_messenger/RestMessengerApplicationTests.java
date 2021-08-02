package my.rest_messenger;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestMessengerApplicationTests {

	@LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void assertThatContextIsUp() {
		given().basePath("/").get("").then().statusCode(200);
	}

	@Test
	public void assertThatGetUsersIsWorking() {
		given().basePath("/user").get("").
				then().log().body().statusCode(200);
	}

	@Test
	public void assertThatGetUsersReturnsCorrectUser() {
		given().basePath("/user").get("").
				then().body("username", hasItems("Vasya", "Petya", "Adolf"));
	}
}
