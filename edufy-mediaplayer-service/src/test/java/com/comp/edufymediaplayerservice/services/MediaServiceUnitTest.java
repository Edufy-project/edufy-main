package com.comp.edufymediaplayerservice.services;

import com.comp.edufymediaplayerservice.dto.MediaDTO;
import com.comp.edufymediaplayerservice.entities.*;
import com.comp.edufymediaplayerservice.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MediaServiceUnitTest  {

    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private MusicRepository musicRepository;
    private PodRepository podRepository;
    private VideoRepository videoRepository;

    private MediaService mediaService;

    @BeforeEach
    void setUp(){

        albumRepository = mock(AlbumRepository.class);
        artistRepository = mock(ArtistRepository.class);
        musicRepository = mock(MusicRepository.class);
        podRepository = mock(PodRepository.class);
        videoRepository = mock(VideoRepository.class);

        mediaService = new MediaService(
                albumRepository,
                artistRepository,
                musicRepository,
                podRepository,
                videoRepository
        );
    }


    @Test
    void does_GetAllMediaByType_return_list_from_repo_of_type_music() {

        Album album = new Album(); album.setTitle("Album A");
        Artist artist = new Artist(); artist.setName("Artist A");
        Genre genre = new Genre(); genre.setName("Jazz");
        Music music = new Music();

        music.setId(1L);
        music.setTitle("Song A");
        music.setType("music");
        music.setReleaseDate(LocalDate.now());
        music.setStreamUrl("http://music");
        music.setAlbumOrder(1);
        music.setCreatedAt(LocalDate.now().atStartOfDay());
        music.setAlbum(album);
        music.setArtist(artist);
        music.setGenre(genre);
        music.setPlayCount(0L);

        when(musicRepository.findAll()).thenReturn(List.of(music));

        List<MediaDTO> result = mediaService.getAllMediaByType("music");

        assertEquals(1, result.size());
        assertEquals("Song A", result.getFirst().getTitle());
        assertEquals("Album A", result.getFirst().getAlbumTitle());
        assertEquals("Artist A", result.getFirst().getArtistName());
        assertEquals("Jazz", result.getFirst().getGenreName());
    }

    @Test
    void does_GetAllMediaByType_return_list_from_repo_of_selected_type_video() {

        Artist artist = new Artist(); artist.setName("Artist B");
        Genre genre = new Genre(); genre.setName("Programing");
        Video video = new Video();

        video.setId(1L);
        video.setTitle("Video B");
        video.setType("video");
        video.setReleaseDate(LocalDate.now());
        video.setStreamUrl("http://video");
        video.setAlbumOrder(1);
        video.setCreatedAt(LocalDate.now().atStartOfDay());
        video.setAlbum(null);
        video.setArtist(artist);
        video.setGenre(genre);
        video.setPlayCount(0L);

        when(videoRepository.findAll()).thenReturn(List.of(video));

        List<MediaDTO> result = mediaService.getAllMediaByType("video");

        assertEquals(1, result.size());
        assertEquals("Video B", result.getFirst().getTitle());
        assertEquals(null, result.getFirst().getAlbumTitle());
        assertEquals("Artist B", result.getFirst().getArtistName());
        assertEquals("Programing", result.getFirst().getGenreName());
    }

    @Test
    void does_GetAllMediaByType_return_list_from_repo_of_selected_type_pod() {

        Artist artist = new Artist(); artist.setName("Artist C");
        Genre genre = new Genre(); genre.setName("Programing");
        Pod pod = new Pod();

        pod.setId(1L);
        pod.setTitle("Pod C");
        pod.setType("pod");
        pod.setReleaseDate(LocalDate.now());
        pod.setStreamUrl("http://pod");
        pod.setAlbumOrder(1);
        pod.setCreatedAt(LocalDate.now().atStartOfDay());
        pod.setAlbum(null);
        pod.setArtist(artist);
        pod.setGenre(genre);
        pod.setPlayCount(0L);

        when(podRepository.findAll()).thenReturn(List.of(pod));

        List<MediaDTO> result = mediaService.getAllMediaByType("pod");

        assertEquals(1, result.size());
        assertEquals("Pod C", result.getFirst().getTitle());
        assertEquals(null, result.getFirst().getAlbumTitle());
        assertEquals("Artist C", result.getFirst().getArtistName());
        assertEquals("Programing", result.getFirst().getGenreName());
    }

    @Test
    void can_GetAllMediaByType_return_empty_list() {

        when(musicRepository.findAll()).thenReturn(List.of());
        List<MediaDTO> result = mediaService.getAllMediaByType("music");
        assertEquals(0, result.size());
    }


    @Test
    void does_getMediaGenreById_return_genre_from_correct_repo_music() {
        Genre genre = new Genre(); genre.setName("Jazz");
        Music music = new Music(); music.setId(1L); music.setGenre(genre);

        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));

        String result = mediaService.getMediaGenreById("music", 1L);

        assertEquals("Jazz", result);
    }

    @Test
    void does_getMediaGenreById_return_genre_from_correct_repo_video() {
        Genre genre = new Genre();
        genre.setName("Programing");
        Video video = new Video();
        video.setId(1L);
        video.setGenre(genre);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        String result = mediaService.getMediaGenreById("video", 1L);

        assertEquals("Programing", result);
    }

    @Test
    void does_getMediaGenreById_return_genre_from_correct_repo_pod() {
        Genre genre = new Genre();
        genre.setName("Programing");
        Pod pod = new Pod();
        pod.setId(1L);
        pod.setGenre(genre);

        when(podRepository.findById(1L)).thenReturn(Optional.of(pod));

        String result = mediaService.getMediaGenreById("pod", 1L);

        assertEquals("Programing", result);
    }

    @Test
    void does_getAllMediaByGenre_return_from_correct_repo_music() {
        Genre genre = new Genre(); genre.setName("Jazz");
        Album album = new Album(); album.setTitle("Album A");
        Artist artist = new Artist(); artist.setName("Artist A");
        Music music = new Music();

        music.setId(1L);
        music.setTitle("Song A");
        music.setType("music");
        music.setAlbum(album);
        music.setArtist(artist);
        music.setGenre(genre);
        music.setPlayCount(0L);

        when(musicRepository.findAllByGenreNameIgnoreCase("Jazz"))
                .thenReturn(List.of(music));

        List<MediaDTO> result = mediaService.getAllMediaByGenre("music", "Jazz");

        assertEquals(1, result.size());
        MediaDTO dto = result.getFirst();
        assertEquals("Song A", dto.getTitle());
        assertEquals("Album A", dto.getAlbumTitle());
        assertEquals("Artist A", dto.getArtistName());
        assertEquals("Jazz", dto.getGenreName());
    }

    @Test
    void does_getAllMediaByGenre_return_from_correct_repo_video() {
        Genre genre = new Genre(); genre.setName("Programing");
        Artist artist = new Artist(); artist.setName("Artist B");
        Video video = new Video();

        video.setId(1L);
        video.setTitle("Video B");
        video.setType("video");
        video.setAlbum(null);
        video.setArtist(artist);
        video.setGenre(genre);
        video.setPlayCount(0L);

        when(videoRepository.findAllByGenreNameIgnoreCase("Programing"))
                .thenReturn(List.of(video));

        List<MediaDTO> result = mediaService.getAllMediaByGenre("video", "Programing");

        assertEquals(1, result.size());
        MediaDTO dto = result.getFirst();
        assertEquals("Video B", dto.getTitle());
        assertEquals(null, dto.getAlbumTitle());
        assertEquals("Artist B", dto.getArtistName());
        assertEquals("Programing", dto.getGenreName());
    }

    @Test
    void does_getAllMediaByGenre_return_from_correct_repo_pod() {
        Genre genre = new Genre(); genre.setName("Programing");
        Artist artist = new Artist(); artist.setName("Artist C");
        Pod pod = new Pod();

        pod.setId(1L);
        pod.setTitle("Pod C");
        pod.setType("pod");
        pod.setAlbum(null);
        pod.setArtist(artist);
        pod.setGenre(genre);
        pod.setPlayCount(0L);

        when(podRepository.findAllByGenreNameIgnoreCase("Programing"))
                .thenReturn(List.of(pod));

        List<MediaDTO> result = mediaService.getAllMediaByGenre("pod", "Programing");

        assertEquals(1, result.size());
        MediaDTO dto = result.getFirst();
        assertEquals("Pod C", dto.getTitle());
        assertEquals(null, dto.getAlbumTitle());
        assertEquals("Artist C", dto.getArtistName());
        assertEquals("Programing", dto.getGenreName());
    }


    @Test
    void returns_empty_list_when_no_media_of_genre_available_music() {
        when(musicRepository.findAllByGenreNameIgnoreCase("Rock"))
                .thenReturn(List.of());

        List<MediaDTO> result = mediaService.getAllMediaByGenre("music", "Rock");
        assertTrue(result.isEmpty());
    }

    @Test
    void returns_empty_list_when_no_media_of_genre_available_video() {
        when(videoRepository.findAllByGenreNameIgnoreCase("Programing"))
                .thenReturn(List.of());

        List<MediaDTO> result = mediaService.getAllMediaByGenre("video", "Programing");
        assertTrue(result.isEmpty());
    }

    @Test
    void returns_empty_list_when_no_media_of_genre_available_pod() {
        when(podRepository.findAllByGenreNameIgnoreCase("Programing"))
                .thenReturn(List.of());

        List<MediaDTO> result = mediaService.getAllMediaByGenre("pod", "Programing");
        assertTrue(result.isEmpty());
    }

    @Test
    void throwsExceptionForInvalidType() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getAllMediaByGenre("invalid", "Jazz"));

        assertTrue(exception.getMessage().contains("Invalid type"));
    }


    @Test
    void getMediaByName_can_return_by_name_music() {
        Music music = new Music(); music.setTitle("Music");
        when(musicRepository.findByTitleContainingIgnoreCase("Music"))
                .thenReturn(List.of(music));
        when(podRepository.findByTitleContainingIgnoreCase("Music"))
                .thenReturn(List.of());
        when(videoRepository.findByTitleContainingIgnoreCase("Music"))
                .thenReturn(List.of());

        Object result = mediaService.getMediaByName("Music");

        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.getFirst() instanceof Music);
    }

    @Test
    void getMediaByName_can_return_by_name_video() {
        Video video = new Video(); video.setTitle("Video");
        when(musicRepository.findByTitleContainingIgnoreCase("Video"))
                .thenReturn(List.of());
        when(podRepository.findByTitleContainingIgnoreCase("Video"))
                .thenReturn(List.of());
        when(videoRepository.findByTitleContainingIgnoreCase("Video"))
                .thenReturn(List.of(video));

        Object result = mediaService.getMediaByName("Video");

        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.getFirst() instanceof Video);
    }

    @Test
    void getMediaByName_can_return_by_name_pod() {
        Pod pod = new Pod(); pod.setTitle("Pod");
        when(musicRepository.findByTitleContainingIgnoreCase("Pod"))
                .thenReturn(List.of());
        when(podRepository.findByTitleContainingIgnoreCase("Pod"))
                .thenReturn(List.of(pod));
        when(videoRepository.findByTitleContainingIgnoreCase("Pod"))
                .thenReturn(List.of());

        Object result = mediaService.getMediaByName("Pod");

        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.getFirst() instanceof Pod);
    }

    @Test
    void getMediaByName_throws_if_media_does_not_exist() {
        when(musicRepository.findByTitleContainingIgnoreCase("Unknown")).thenReturn(List.of());
        when(podRepository.findByTitleContainingIgnoreCase("Unknown")).thenReturn(List.of());
        when(videoRepository.findByTitleContainingIgnoreCase("Unknown")).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getMediaByName("Unknown"));

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void getMediaByName_throws_if_title_contains_spaces() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getMediaByName("My Song"));
        assertTrue(exception.getMessage().contains("Url can't contain spaces."));
    }


    @Test
    void returnsAlbumIfFound() {
        Album album = new Album(); album.setTitle("Album");

        when(albumRepository.findByTitleIgnoreCase("Album"))
                .thenReturn(Optional.of(album));

        Object result = mediaService.getAlbumByName("Album");

        assertTrue(result instanceof Album);
        assertEquals("Album", ((Album) result).getTitle());
    }

    @Test
    void getAlbumByName_throws_when_title_contains_spaces() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getAlbumByName("My Album"));

        assertTrue(exception.getMessage().contains("Url can't contain spaces."));
    }

    @Test
    void getAlbumByName_normalizes_title_casing() {
        Album album = new Album(); album.setTitle("My Album");

        when(albumRepository.findByTitleIgnoreCase("my album"))
                .thenReturn(Optional.of(album));

        Object result = mediaService.getAlbumByName("My_Album");

        assertTrue(result instanceof Album);
        assertEquals("My Album", ((Album) result).getTitle());
    }


    @Test
    void getAlbumByName_throws_if_name_not_found() {
        when(albumRepository.findByTitleIgnoreCase("Unknown"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getAlbumByName("Unknown"));

        assertTrue(exception.getMessage().contains("was not found"));
    }



    @Test
    void getArtistByName_returns_available_artist() {
        Artist artist = new Artist(); artist.setName("Artist");
        when(artistRepository.findByNameIgnoreCase("Artist"))
                .thenReturn(Optional.of(artist));

        Object result = mediaService.getArtistByName("Artist");

        assertTrue(result instanceof Artist);
        assertEquals("Artist", ((Artist) result).getName());
    }

    @Test
    void getArtistByName_normalizes_name_casing() {
        Artist artist = new Artist(); artist.setName("My Artist");
        when(artistRepository.findByNameIgnoreCase("my artist"))
                .thenReturn(Optional.of(artist));

        Object result = mediaService.getArtistByName("My_Artist");

        assertTrue(result instanceof Artist);
        assertEquals("My Artist", ((Artist) result).getName());
    }

    @Test
    void getArtistByName_throws_if_name_contains_spaces() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getArtistByName("My Artist"));
        assertTrue(exception.getMessage().contains("Url can't contain spaces."));
    }

    @Test
    void getArtistByName_throws_if_name_not_found() {
        when(artistRepository.findByNameIgnoreCase("Unknown"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.getArtistByName("Unknown"));

        assertTrue(exception.getMessage().contains("No artist with the name"));
    }


    @Test
    void likeMedia_increases_thumbsUp_of_music() {
        Music music = new Music();
        music.setId(1L);
        music.setThumbsUp(0);

        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));

        mediaService.likeMedia("music", 1L);

        assertEquals(1L, music.getThumbsUp());
        verify(musicRepository).save(music);
    }


    @Test
    void likeMedia_increases_thumbsUp_of_video() {
        Video video = new Video();
        video.setId(1L);
        video.setThumbsUp(0);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        mediaService.likeMedia("video", 1L);

        assertEquals(1, video.getThumbsUp());
        verify(videoRepository).save(video);
    }

    @Test
    void likeMedia_increases_thumbsUp_of_pod() {
        Pod pod = new Pod();
        pod.setId(1L);
        pod.setThumbsUp(0);

        when(podRepository.findById(1L)).thenReturn(Optional.of(pod));

        mediaService.likeMedia("pod", 1L);

        assertEquals(1L, pod.getThumbsUp());
        verify(podRepository).save(pod);
    }

    @Test
    void likeMedia_throws_when_media_unavailable() {
        when(musicRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mediaService.likeMedia("music", 1L));

        assertTrue(exception.getMessage().contains("Music with id 1 does not exist"));
    }

    @Test
    void likeMedia_throws_when_type_unavailable() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> mediaService.likeMedia("invalid", 1L));

        assertTrue(ex.getMessage().contains("Invalid type"));
    }


    @Test
    void getValidMediaTypes_returns_expected_list() {
        List<String> result = mediaService.getValidMediaTypes();

        assertEquals(3, result.size());
        assertEquals(List.of("music", "video", "pod"), result);
    }

}
