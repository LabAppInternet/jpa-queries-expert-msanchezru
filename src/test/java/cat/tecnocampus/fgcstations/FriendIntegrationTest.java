package cat.tecnocampus.fgcstations;

import cat.tecnocampus.fgcstations.application.DTOs.UserFriendsDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private static HttpHeaders headers;

    @BeforeAll()
    public static void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    @Test
    void getFriendsForUser_HappyPath() throws Exception {
        ResponseEntity<UserFriendsDTO> response = restTemplate.getForEntity("/api/users/tina/friends", UserFriendsDTO.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getFriends()).containsExactlyInAnyOrder("maria", "pepe", "pepa");
    }

    @Test
    void getFriendsForUser_UserDoesNotExist() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/unknown/friends", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllFriends_HappyPath() throws Exception {
        ResponseEntity<UserFriendsDTO[]> response = restTemplate.getForEntity("/api/users/friends", UserFriendsDTO[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(Arrays.stream(response.getBody()).map(UserFriendsDTO::getFriends).map(List::size)).containsExactly(1,3);
    }

    @Test
    @DirtiesContext
    void addFriends_HappyPath() throws Exception {
        String requestJson =
                """
                {
                  "username": "joanra",
                  "friends": [
                    "manel",
                    "anna",
                    "clara",
                    "maria"
                  ]
                }""";

        HttpEntity<String> request = new HttpEntity<>(requestJson,headers);
        ResponseEntity<Void> response = restTemplate.exchange("/api/users/friends", HttpMethod.POST, request, Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<UserFriendsDTO> responseGet = restTemplate.getForEntity("/api/users/joanra/friends", UserFriendsDTO.class);
        Assertions.assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGet.getBody().getFriends()).containsExactlyInAnyOrder("pepa", "manel", "anna", "clara", "maria");
    }

    @Test
    void addFriends_EmptyListOfFriends() throws Exception {

        String requestJson = "{\"username\": \"Joanra\", \"friends\": []}";
        HttpEntity<String> request = new HttpEntity<>(requestJson,headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/friends", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        //Assertions.assertThat(response.getBody()).contains("must not be empty");
        Assertions.assertThat(response.getBody()).contains("no debe estar vacío");
    }

    @Test
    void addFriends_UserDoesNotExist() throws Exception {
        String requestJson = "{\"username\": \"xoanra\", \"friends\": [\"hola\"]}";
        HttpEntity<String> request = new HttpEntity<>(requestJson,headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/friends", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getTopFriends_HappyPath() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/topFriends", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getFriendsOfUser() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/friendsOf/pepa", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
