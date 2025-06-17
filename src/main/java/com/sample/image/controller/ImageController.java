package com.sample.image.controller;

import com.sample.image.dto.ImageDTO;
import com.sample.image.exception.ApplicationException;
import com.sample.image.exception.ProductFetchException;
import com.sample.image.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/images/")
public class ImageController {

    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping("{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable("id") Long id) throws ApplicationException {
        logger.info("Received request to get image with id: {}", id);
        ImageDTO imageDTO = imageService.getById(id);
        logger.info("Image with id {} retrieved successfully", id);
        return ResponseEntity.ok().body(imageDTO);
    }


    @PostMapping("upload")
    public ResponseEntity<List<ImageDTO>> saveImageByProductId(@RequestParam List<MultipartFile> files,
                                                               @RequestParam("productId") Long productId) throws SQLException, IOException, ProductFetchException, ApplicationException {
        logger.info("Received request to upload images for product with id: {}", productId);
        List<ImageDTO> imageDTOS = imageService.save(files,productId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(imageDTOS.stream().map(ImageDTO::getId).toArray())
                .toUri();
        return ResponseEntity.created(location).body(imageDTOS);
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Resource> downloadImageById(@PathVariable Long id) throws SQLException, ApplicationException {
        logger.info("Received request to download image with id: {}", id);
        ImageDTO imageDTO = imageService.getById(id);
        ByteArrayResource resource = new ByteArrayResource(imageDTO.getImagePicture().getBytes(1, (int) imageDTO.getImagePicture().length()));
        logger.info("Image with id {} downloaded successfully", id);
       return ResponseEntity.ok().
               contentType(MediaType.parseMediaType(imageDTO.getImageFileType())).
               header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= \"" +imageDTO.getImageName() + "\"").body(resource);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateImageById(@PathVariable Long id, @RequestBody MultipartFile file) throws RuntimeException, ApplicationException {
        logger.info("Received request to update image with id: {}", id);
        imageService.updateById(file,id);
        logger.info("Received updated image for id: {}", id);
        return ResponseEntity.ok().body("image updated successfully");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteImageById(@PathVariable Long id) throws ApplicationException {
        logger.info("Received request to delete image with id: {}", id);
        imageService.deleteById(id);
        return ResponseEntity.ok().body("image deleted successfully");
    }

    @GetMapping("search")
    public ResponseEntity<List<ImageDTO>> getImagesByProductId(@RequestParam("productId") Long productId) throws ApplicationException {
        logger.info("Received request to get images for product with id: {}", productId);
        List<ImageDTO> imageDTOS = imageService.getByProductId(productId);
        logger.info("Images for product with id {} retrieved successfully", productId);
        return ResponseEntity.ok().body(imageDTOS);
    }

}
