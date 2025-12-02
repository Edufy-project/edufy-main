package com.example.edufy_recommendation_service.services;

import com.example.edufy_recommendation_service.DTO.MediaReferenceDTO;
import com.example.edufy_recommendation_service.DTO.RecommendationDTO;
import com.example.edufy_recommendation_service.DTO.UserFeedbackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Henrik
@ExtendWith(MockitoExtension.class)
class RecommendServiceUnitTest {

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient userServiceClient;

    @Mock
    private RestClient mediaServiceClient;

    private RecommendService recommendService;

    private String userServiceUrl = "http://http://localhost:9093";
    private String mediaServiceUrl = "http://localhost:9091";
    private String testToken = "test-token";

    @BeforeEach
    void setUp() {
        when(restClientBuilder.baseUrl(userServiceUrl)).thenReturn(restClientBuilder);
        when(restClientBuilder.baseUrl(mediaServiceUrl)).thenReturn(restClientBuilder);
        when(restClientBuilder.build())
                .thenReturn(userServiceClient)
                .thenReturn(mediaServiceClient);
        recommendService = new RecommendService(restClientBuilder, userServiceUrl, mediaServiceUrl);
    }

    @Test
    void calculateRecommendWeightShouldReturnCorrectValues() {
        MediaReferenceDTO media = new MediaReferenceDTO("music", 1L);

        List<UserFeedbackDTO> userLikes = Arrays.asList(
                new UserFeedbackDTO(1L, 1L, 1L, "music", "THUMBS_UP", LocalDateTime.now()));
        List<UserFeedbackDTO> userDislikes = Arrays.asList(
                new UserFeedbackDTO(1L, 2L, 2L, "music", "THUMBS_DOWN", LocalDateTime.now()));
        List<UserFeedbackDTO> noFeedback = List.of();

        int likedMediaWeight = recommendService.calculateRecommendWeight(media, userLikes, noFeedback);
        assertEquals(3, likedMediaWeight);

        MediaReferenceDTO dislikedMedia = new MediaReferenceDTO("music", 2L);
        int dislikesMediaWeight = recommendService.calculateRecommendWeight(dislikedMedia, noFeedback, userDislikes);
        assertEquals(-3, dislikesMediaWeight);

        MediaReferenceDTO listenedMedia = new MediaReferenceDTO("music", 3L);
        int listenedMediaWeight = recommendService.calculateRecommendWeight(listenedMedia, noFeedback, noFeedback);
        assertEquals(1, listenedMediaWeight);
    }

    @Test
    void getMediaGenresFromHistoryShouldSortByFeedbackAndExcludeDisliked() {
        RecommendService recommendedService = spy(recommendService);

        doReturn("Rock").when(recommendedService).getMediaGenre(eq("music"), eq(1L), anyString());
        doReturn("Pop").when(recommendedService).getMediaGenre(eq("music"), eq(2L), anyString());
        doReturn("Hip-hop").when(recommendedService).getMediaGenre(eq("music"), eq(3L), anyString());

        List<MediaReferenceDTO> userHistory = Arrays.asList(
                new MediaReferenceDTO("music", 1L),
                new MediaReferenceDTO("music", 2L),
                new MediaReferenceDTO("music", 3L)
        );

        List<UserFeedbackDTO> userLikes = Arrays.asList(
                new UserFeedbackDTO(1L, 1L, 1L, "music", "THUMBS_UP", LocalDateTime.now()));
        List<UserFeedbackDTO> userDislikes = Arrays.asList(
                new UserFeedbackDTO(1L, 2L, 2L, "music", "THUMBS_DOWN", LocalDateTime.now()));
        List<String> result = recommendedService.getMediaGenresFromHistory(
                userHistory, userLikes, userDislikes, testToken);

        assertEquals(2, result.size());
        assertEquals("Rock", result.get(0));
        assertEquals("Hip-hop", result.get(1));
    }

    @Test
    void getMediaGenresFromHistoryShouldThrowWhenHistoryNull() {
        List<UserFeedbackDTO> userLikes = Collections.emptyList();
        List<UserFeedbackDTO> userDislikes = Collections.emptyList();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            recommendService.getMediaGenresFromHistory(null, userLikes, userDislikes, testToken);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
    }

    @Test
    void getUserMediaHistoryShouldMatchExpectedValue() {
        RecommendService recommendedService = spy(recommendService);
        RestClient.RequestHeadersUriSpec requestHeaderUri = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeader = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec response = mock(RestClient.ResponseSpec.class);

        when(userServiceClient.get()).thenReturn(requestHeaderUri);
        when(requestHeaderUri.uri("/edufy/api/users/usermediahistory/1")).thenReturn(requestHeader);
        when(requestHeader.header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken)).thenReturn(requestHeader);
        when(requestHeader.retrieve()).thenReturn(response);

        List<MediaReferenceDTO> expectedHistory = Arrays.asList(
                new MediaReferenceDTO("music", 1L),
                new MediaReferenceDTO("podcast", 2L)
        );

        when(response.body(any(ParameterizedTypeReference.class))).thenReturn(expectedHistory);

        List<MediaReferenceDTO> result = recommendedService.getUserMediaHistory(1L, testToken);

        assertEquals(expectedHistory, result);
    }

    @Test
    void getMediaGenreSuccess() {
        RecommendService recommendedService = spy(recommendService);
        RestClient.RequestHeadersUriSpec requestHeaderUri = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeader = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec response = mock(RestClient.ResponseSpec.class);

        when(mediaServiceClient.get()).thenReturn(requestHeaderUri);
        when(requestHeaderUri.uri("/edufy/api/mediaplayer/getgenre/music/1")).thenReturn(requestHeader);
        when(requestHeader.header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken)).thenReturn(requestHeader);
        when(requestHeader.retrieve()).thenReturn(response);
        when(response.body(String.class)).thenReturn("Rock");

        String result = recommendedService.getMediaGenre("music", 1L, testToken);

        assertEquals("Rock", result);
    }

    @Test
    void getValidMediaTypesShouldReturnExpectedValue() {
        RecommendService recommendedService = spy(recommendService);

        RestClient.RequestHeadersUriSpec requestHeaderUri = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeader = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec response = mock(RestClient.ResponseSpec.class);

        when(mediaServiceClient.get()).thenReturn(requestHeaderUri);
        when(requestHeaderUri.uri("/edufy/api/mediaplayer/valid-mediatypes")).thenReturn(requestHeader);
        when(requestHeader.header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken)).thenReturn(requestHeader);
        when(requestHeader.retrieve()).thenReturn(response);

        List<String> expectedTypes = Arrays.asList("music", "pod", "video");
        when(response.body(any(ParameterizedTypeReference.class))).thenReturn(expectedTypes);

        List<String> result = recommendedService.getValidMediaTypes(testToken);

        assertEquals(expectedTypes, result);
    }

    @Test
    void getUserLikesByIdShouldReturnExpectedValue() {
        RecommendService recommendedService = spy(recommendService);

        RestClient.RequestHeadersUriSpec requestHeaderUri = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeader = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec response = mock(RestClient.ResponseSpec.class);

        when(userServiceClient.get()).thenReturn(requestHeaderUri);
        when(requestHeaderUri.uri("/edufy/api/users/user/1/feedback/likes")).thenReturn(requestHeader);
        when(requestHeader.header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken)).thenReturn(requestHeader);
        when(requestHeader.retrieve()).thenReturn(response);

        List<UserFeedbackDTO> expectedLikes = Arrays.asList(new UserFeedbackDTO(1L, 1L, 1L, "music", "THUMBS_UP", LocalDateTime.now()));
        when(response.body(any(ParameterizedTypeReference.class))).thenReturn(expectedLikes);

        List<UserFeedbackDTO> result = recommendedService.getUserLikesById(1L, testToken);
        assertEquals(expectedLikes, result);
    }

    @Test
    void getUserDislikesByIdShouldReturnExpectedValue() {
        RecommendService recommendedService = spy(recommendService);

        RestClient.RequestHeadersUriSpec requestHeaderUri = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeader = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec response = mock(RestClient.ResponseSpec.class);

        when(userServiceClient.get()).thenReturn(requestHeaderUri);
        when(requestHeaderUri.uri("/edufy/api/users/user/1/feedback/dislikes")).thenReturn(requestHeader);
        when(requestHeader.header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken)).thenReturn(requestHeader);
        when(requestHeader.retrieve()).thenReturn(response);

        List<UserFeedbackDTO> expectedDislikes = Arrays.asList(new UserFeedbackDTO(1L, 1L, 1L, "music", "THUMBS_DOWN", LocalDateTime.now()));
        when(response.body(any(ParameterizedTypeReference.class))).thenReturn(expectedDislikes);

        List<UserFeedbackDTO> result = recommendedService.getUserDislikesById(1L, testToken);
        assertEquals(expectedDislikes, result);
    }

    @Test
    void getRecommendedMediaListByUserIdShouldReturnExpectedTwoMediaEntities() {
        RecommendService recommendedService = spy(recommendService);

        List<MediaReferenceDTO> userHistory = Arrays.asList(new MediaReferenceDTO("music", 1L));
        List<UserFeedbackDTO> userLikes = Arrays.asList(
                new UserFeedbackDTO(1L, 1L, 1L, "music", "THUMBS_UP", LocalDateTime.now()));
        List<UserFeedbackDTO> mockDislikes = Collections.emptyList();
        List<String> genres = Arrays.asList("Rock");
        List<String> validTypes = Arrays.asList("music", "pod");
        List<RecommendationDTO> recommendedList = Arrays.asList(
                new RecommendationDTO(2L, "Rock", "Test 1"),
                new RecommendationDTO(3L, "Rock", "Test 2")
        );

        doReturn(userHistory).when(recommendedService).getUserMediaHistory(1L, testToken);
        doReturn(userLikes).when(recommendedService).getUserLikesById(1L, testToken);
        doReturn(mockDislikes).when(recommendedService).getUserDislikesById(1L, testToken);
        doReturn(genres).when(recommendedService).getMediaGenresFromHistory(userHistory, userLikes, mockDislikes, testToken);
        doReturn(validTypes).when(recommendedService).getValidMediaTypes(testToken);
        doReturn(recommendedList).when(recommendedService).getMediaByPreferredGenres("music", 1L, genres, 10, testToken);

        List<RecommendationDTO> result = recommendedService.getRecommendedMediaListByUserId("music", 1L, testToken);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getRecommendedMediaListByUserIdShouldThrowWhenInvalidMediaType() {
        RecommendService spyService = spy(recommendService);

        List<MediaReferenceDTO> userHistory = Arrays.asList(new MediaReferenceDTO("music", 1L));
        List<UserFeedbackDTO> userLikes = Collections.emptyList();
        List<UserFeedbackDTO> userDislikes = Collections.emptyList();
        List<String> genres = Arrays.asList("Rock");
        List<String> validMediaTypes = Arrays.asList("music", "pod");

        doReturn(userHistory).when(spyService).getUserMediaHistory(1L, testToken);
        doReturn(userLikes).when(spyService).getUserLikesById(1L, testToken);
        doReturn(userDislikes).when(spyService).getUserDislikesById(1L, testToken);
        doReturn(genres).when(spyService).getMediaGenresFromHistory(userHistory, userLikes, userDislikes, testToken);
        doReturn(validMediaTypes).when(spyService).getValidMediaTypes(testToken);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            spyService.getRecommendedMediaListByUserId("asdasd", 1L, testToken);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Invalid"));
    }

    @Test
    void getMediaByPreferredGenresShouldReturnThreeMediaEntities() {
        RecommendService recommendedService = spy(recommendService);

        List<MediaReferenceDTO> userHistory = Arrays.asList(new MediaReferenceDTO("music", 1L));
        List<UserFeedbackDTO> userDislikes = Collections.emptyList();
        List<RecommendationDTO> mediaList = Arrays.asList(
                new RecommendationDTO(2L, "Rock", "Test 1"),
                new RecommendationDTO(3L, "Rock", "Test 2"));
        List<RecommendationDTO> allMedia = Arrays.asList(
                new RecommendationDTO(4L, "Hip-hop", "Test 3"));

        doReturn(userHistory).when(recommendedService).getUserMediaHistory(1L, testToken);
        doReturn(userDislikes).when(recommendedService).getUserDislikesById(1L, testToken);

        RestClient.RequestHeadersUriSpec requestHeaderUri = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeader = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec response = mock(RestClient.ResponseSpec.class);

        when(mediaServiceClient.get()).thenReturn(requestHeaderUri);
        when(requestHeaderUri.uri(anyString())).thenReturn(requestHeader);
        when(requestHeader.header(anyString(), anyString())).thenReturn(requestHeader);
        when(requestHeader.retrieve()).thenReturn(response);
        when(response.body(any(ParameterizedTypeReference.class)))
                .thenReturn(mediaList)
                .thenReturn(allMedia);

        List<RecommendationDTO> result = recommendedService.getMediaByPreferredGenres("music", 1L, Arrays.asList("Rock"), 3, testToken);

        assertNotNull(result);
        assertTrue(result.size() <= 3);
    }

}