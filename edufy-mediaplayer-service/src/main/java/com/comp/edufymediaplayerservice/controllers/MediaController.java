package com.comp.edufymediaplayerservice.controllers;

import com.comp.edufymediaplayerservice.dto.MediaDTO;
import com.comp.edufymediaplayerservice.services.MediaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("edufy/api/mediaplayer/")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService){
        this.mediaService = mediaService;
    }

    @GetMapping("getgenre/{mediaType}/{mediaId}")
    public String getMediaGenreById(@PathVariable String mediaType, @PathVariable Long mediaId) {
        return mediaService.getMediaGenreById(mediaType, mediaId);
    }

    @GetMapping("getmedia/all/{mediaType}")
    public List<MediaDTO> getAllMediaByType(@PathVariable String mediaType) {
      return mediaService.getAllMediaByType(mediaType);
    }

    @GetMapping("getmedia/{mediaType}/{genre}")
    public Object getMediaByGenre(@PathVariable String mediaType, @PathVariable String genre) {
        return mediaService.getAllMediaByGenre(mediaType, genre);
    }

    @GetMapping("media/{mediaName}")
    public Object getMediaByName(@PathVariable String mediaName){
        return mediaService.getMediaByName(mediaName);
    }

    @GetMapping("album/{albumName}")
    public Object getAlbumByName(@PathVariable String albumName){
        return mediaService.getAlbumByName(albumName);
    }

    @GetMapping("artist/{artistName}")
    public Object getArtistByName(@PathVariable String artistName){
        return mediaService.getArtistByName(artistName);
    }

    @GetMapping("valid-mediatypes")
    public List<String> getValidMediaTypes() {
        return mediaService.getValidMediaTypes();
    }

    @GetMapping("likemedia/{mediaType}/{mediaId}")
    public void likeMediaByName(@PathVariable String mediaType, @PathVariable Long mediaId){
        mediaService.likeMedia(mediaType, mediaId);
    }

}
