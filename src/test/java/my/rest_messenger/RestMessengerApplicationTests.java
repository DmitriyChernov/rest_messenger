package my.rest_messenger;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

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

	@RunWith(Suite.class)
	@Suite.SuiteClasses({
			RestMessengerApplicationTests.class,
			UserTests.class,
			ConversationTests.class,
			MessageTests.class,
			GetHistoryTests.class,
			JoinConversationTests.class,
			GetConversationInfoTests.class
	})
	public class RestEntitiesTests {
		@Test
		public void contextLoads() {
		}
	}
}
