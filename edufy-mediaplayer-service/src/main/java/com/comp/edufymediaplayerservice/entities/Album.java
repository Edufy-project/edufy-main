package com.comp.edufymediaplayerservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @JsonIgnoreProperties("album")
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Music> musicList = new ArrayList<>();

    @JsonIgnoreProperties("album")
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Pod> podList = new ArrayList<>();

    @JsonIgnoreProperties("album")
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Video> videoList = new ArrayList<>();

    public Album() {
    }

    public Album(String title, LocalDate releaseDate) {
        this.title = title;
        this.releaseDate = releaseDate;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public List<Pod> getPodList() {
        return podList;
    }

    public void setPodList(List<Pod> podList) {
        this.podList = podList;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    // Helper methods
    public void addMusic(Music music) {
        musicList.add(music);
        music.setAlbum(this);
    }

    public void removeMusic(Music music) {
        musicList.remove(music);
        music.setAlbum(null);
    }

    public void addPod(Pod pod) {
        podList.add(pod);
        pod.setAlbum(this);
    }

    public void removePod(Pod pod) {
        podList.remove(pod);
        pod.setAlbum(null);
    }

    public void addVideo(Video video) {
        videoList.add(video);
        video.setAlbum(this);
    }

    public void removeVideo(Video video) {
        videoList.remove(video);
        video.setAlbum(null);
    }
}