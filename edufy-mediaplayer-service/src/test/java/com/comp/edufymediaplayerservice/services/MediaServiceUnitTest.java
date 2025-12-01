package com.comp.edufymediaplayerservice.services;

import com.comp.edufymediaplayerservice.dto.MediaDTO;
import com.comp.edufymediaplayerservice.entities.*;
import com.comp.edufymediaplayerservice.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void does_getMediaGenreById_return_expected_genre() {
    }

}
