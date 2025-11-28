package com.comp.edufymediaplayerservice.repositories;

import com.comp.edufymediaplayerservice.entities.Album;
import com.comp.edufymediaplayerservice.entities.Music;
import com.comp.edufymediaplayerservice.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {


    @Query("SELECT v FROM Video v JOIN v.artist a WHERE a.id = :artistId")
    List<Video> findByArtistId(@Param("artistId") Long artistId);


    @Query("SELECT v FROM Video v JOIN v.genre g WHERE g.id = :genreId")
    List<Video> findByGenreId(@Param("genreId") Long genreId);

    List<Video> findAllByGenreNameIgnoreCase(String genreName);

    List<Video> findByAlbumOrderByAlbumOrderAsc(Album album);


    @Query("SELECT v FROM Video v WHERE v.album.id = :albumId ORDER BY v.albumOrder ASC")
    List<Video> findByAlbumId(@Param("albumId") Long albumId);


    List<Video> findByTitleContainingIgnoreCase(String title);


    List<Video> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);


    List<Video> findByReleaseDateAfter(LocalDate date);


    List<Video> findAllByOrderByReleaseDateDesc();


    @Query("SELECT DISTINCT v FROM Video v JOIN v.genre g WHERE g.id IN :genreIds")
    List<Video> findByGenreIds(@Param("genreIds") List<Long> genreIds);


    @Query("SELECT DISTINCT v FROM Video v JOIN v.artist a JOIN v.genre g WHERE a.id = :artistId AND g.id = :genreId")
    List<Video> findByArtistIdAndGenreId(@Param("artistId") Long artistId, @Param("genreId") Long genreId);
}