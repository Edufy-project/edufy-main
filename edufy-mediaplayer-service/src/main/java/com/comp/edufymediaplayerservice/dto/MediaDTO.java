package com.comp.edufymediaplayerservice.dto;

import com.comp.edufymediaplayerservice.entities.Album;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MediaDTO {

    private Long id;
    private String title;
    private String type; //TODO "MUSIC", "POD", "VIDEO"
    private LocalDate releaseDate;
    private String streamUrl;
    private Integer albumOrder;
    private LocalDateTime createdAt;
    private String albumTitle;
    private String artistName;
    private String genreName;
    private Long playCount;

    public MediaDTO() {}
    public MediaDTO(Long id, String title, String type, LocalDate releaseDate, String streamUrl, Integer albumOrder, LocalDateTime createdAt, String albumTitle, String artistName, String genreName, Long playCount) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.releaseDate = releaseDate;
        this.streamUrl = streamUrl;
        this.albumOrder = albumOrder;
        this.createdAt = createdAt;
        this.albumTitle = albumTitle;
        this.artistName = artistName;
        this.genreName = genreName;
        this.playCount = playCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public Integer getAlbumOrder() {
        return albumOrder;
    }

    public void setAlbumOrder(Integer albumOrder) {
        this.albumOrder = albumOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }
}
