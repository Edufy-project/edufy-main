package com.comp.edufymediaplayerservice.repositories;

import com.comp.edufymediaplayerservice.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByTitleIgnoreCase(String title);
    List<Album> findByTitleContainingIgnoreCase(String title);

}
