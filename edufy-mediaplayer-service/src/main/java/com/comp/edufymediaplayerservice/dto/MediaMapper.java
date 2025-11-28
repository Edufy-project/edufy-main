package com.comp.edufymediaplayerservice.dto;

import com.comp.edufymediaplayerservice.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class MediaMapper {

    // Helper-metod för gemensamma fält
    private static MediaDTO mapCommonFields(Long id,String title, java.time.LocalDate releaseDate, String streamUrl,
                                            Integer albumOrder, java.time.LocalDateTime createdAt, String albumTitle,
                                            Artist artist, Genre genre, String type ) {
        MediaDTO dto = new MediaDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setReleaseDate(releaseDate);
        dto.setStreamUrl(streamUrl);
        dto.setAlbumOrder(albumOrder);
        dto.setCreatedAt(createdAt);
        dto.setAlbumTitle(albumTitle);
        dto.setType(type);
        dto.setArtistName(artist.getName());
        dto.setGenreName(genre.getName());

        return dto;
    }

    // Music -> MediaDTO
    public static MediaDTO toMediaDTO(Music music) {
        return mapCommonFields(
                music.getId(),
                music.getTitle(),
                music.getReleaseDate(),
                music.getStreamUrl(),
                music.getAlbumOrder(),
                music.getCreatedAt(),
                music.getAlbum() != null ? music.getAlbum().getTitle() : null,
                music.getArtist(),
                music.getGenre(),
                "MUSIC"
        );
    }

    // Pod -> MediaDTO
    public static MediaDTO toMediaDTO(Pod pod) {
        return mapCommonFields(
                pod.getId(),
                pod.getTitle(),
                pod.getReleaseDate(),
                pod.getStreamUrl(),
                pod.getAlbumOrder(),
                pod.getCreatedAt(),
                null,
                pod.getArtist(),
                pod.getGenre(),
                "POD"
        );
    }

    // Video -> MediaDTO
    public static MediaDTO toMediaDTO(Video video) {
        return mapCommonFields(
                video.getId(),
                video.getTitle(),
                video.getReleaseDate(),
                video.getStreamUrl(),
                video.getAlbumOrder(),
                video.getCreatedAt(),
                null,
                video.getArtist(),
                video.getGenre(),
                "VIDEO"
        );
    }

    // List<Music> -> List<MediaDTO>
    public static List<MediaDTO> toMediaDTOListFromMusic(List<Music> musicList) {
        return musicList.stream()
                .map(MediaMapper::toMediaDTO)
                .collect(Collectors.toList());
    }

    // List<Pod> -> List<MediaDTO>
    public static List<MediaDTO> toMediaDTOListFromPod(List<Pod> podList) {
        return podList.stream()
                .map(MediaMapper::toMediaDTO)
                .collect(Collectors.toList());
    }

    // List<Video> -> List<MediaDTO>
    public static List<MediaDTO> toMediaDTOListFromVideo(List<Video> videoList) {
        return videoList.stream()
                .map(MediaMapper::toMediaDTO)
                .collect(Collectors.toList());
    }
}
