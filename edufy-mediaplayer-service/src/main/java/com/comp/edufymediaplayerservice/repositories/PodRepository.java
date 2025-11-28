package com.comp.edufymediaplayerservice.repositories;

import com.comp.edufymediaplayerservice.entities.Album;
import com.comp.edufymediaplayerservice.entities.Music;
import com.comp.edufymediaplayerservice.entities.Pod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PodRepository extends JpaRepository<Pod, Long> {


    @Query("SELECT p FROM Pod p JOIN p.artist a WHERE a.id = :artistId")
    List<Pod> findByArtistId(@Param("artistId") Long artistId);


    @Query("SELECT p FROM Pod p JOIN p.genre g WHERE g.id = :genreId")
    List<Pod> findByGenreId(@Param("genreId") Long genreId);

    List<Pod> findAllByGenreNameIgnoreCase(String genreName);

    List<Pod> findByAlbumOrderByAlbumOrderAsc(Album album);


    @Query("SELECT p FROM Pod p WHERE p.album.id = :albumId ORDER BY p.albumOrder ASC")
    List<Pod> findByAlbumId(@Param("albumId") Long albumId);


    List<Pod> findByTitleContainingIgnoreCase(String title);


    List<Pod> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);


    List<Pod> findByReleaseDateAfter(LocalDate date);


    List<Pod> findAllByOrderByReleaseDateDesc();


    @Query("SELECT DISTINCT p FROM Pod p JOIN p.genre g WHERE g.id IN :genreIds")
    List<Pod> findByGenreIds(@Param("genreIds") List<Long> genreIds);


    @Query("SELECT DISTINCT p FROM Pod p JOIN p.artist a JOIN p.genre g WHERE a.id = :artistId AND g.id = :genreId")
    List<Pod> findByArtistIdAndGenreId(@Param("artistId") Long artistId, @Param("genreId") Long genreId);
}