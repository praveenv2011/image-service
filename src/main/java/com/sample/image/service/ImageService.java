package com.sample.image.service;

import com.sample.image.dto.ImageDTO;
import com.sample.image.dto.ProductDTO;
import com.sample.image.exception.ApplicationException;
import com.sample.image.exception.ImageNotFoundException;
import com.sample.image.exception.ImageDataAccessException;
import com.sample.image.exception.ProductFetchException;
import com.sample.image.model.Image;
import com.sample.image.repository.ImageRepository;
import com.sample.image.httprequest.HttpRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ImageService {

    @Value("${image.service.url}")
    public String URL;

    private final ImageRepository imageRepository;

    private ModelMapper modelMapper;

    private HttpRequest request;

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public ImageService(ImageRepository imageRepository, ModelMapper modelMapper, HttpRequest request) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
        this.request = request;
    }

    public ImageDTO getById(Long id) throws ApplicationException {
          Optional<Image> image;
          try {
              image = imageRepository.findById(id);
          }catch (DataAccessResourceFailureException e){
                logger.error("Database connection error while fetching image with id {}", id, e);
                throw new ImageDataAccessException("Database connection error while fetching image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }catch (Exception e) {
                logger.error("Unexpected error while fetching image with id {}", id, e);
                throw new ApplicationException("Unexpected error while fetching image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          if(image.isEmpty()){
                throw new ImageNotFoundException("Image with id " + id + " not found", HttpStatus.NOT_FOUND);
          }

          logger.info("Image with id {} received successfully", id);
          Image imageFromDB = image.get();

          return modelMapper.map(imageFromDB,ImageDTO.class);
      }

      @Transactional
      public void deleteById(Long id) throws ApplicationException {
          ImageDTO image = getById(id);

          try {
              imageRepository.deleteById(id);
          }catch (DataAccessResourceFailureException e){
              logger.error("Database connection error while deleting image with id {}", id, e);
              throw new ImageDataAccessException("Database connection error while deleting image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }catch (Exception e) {
              logger.error("Unexpected error while deleting image with id {}", id, e);
              throw new ApplicationException("Unexpected error while deleting image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }

          logger.info("Image with id {} deleted successfully", id);
      }

      @Transactional
      public List<ImageDTO> save(List<MultipartFile> files, Long productId) throws ProductFetchException, ApplicationException {
          ProductDTO productDTO = request.getProductById(productId);
          List<ImageDTO> imageDTOS = new ArrayList<>();

          for(MultipartFile file:files) {

              Image image = new Image();
              image.setImageFileName(file.getOriginalFilename());
              image.setImagefileType(file.getContentType());

              try {
                  image.setImagePicture(new SerialBlob(file.getBytes()));
              }catch (SQLException | IOException e) {
                  logger.error("Error while converting file to Blob for product with id {}", productId, e);
                  throw new ImageDataAccessException("Error while converting file to Blob for product with id " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
              }catch (Exception e) {
                  logger.error("Unexpected error while processing file for product with id {}", productId, e);
                  throw new ApplicationException("Unexpected error while processing file for product with id " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
              }

              image.setProductId(productId);

              String buildDownloadUrl = URL;

                  Image savedImage;
              try {
                   savedImage = imageRepository.save(image);
              }catch (DataAccessResourceFailureException e) {
                  logger.error("Database connection error while saving image for product with id {}", productId, e);
                  throw new ImageDataAccessException("Database connection error while saving image for product with id {}" + productId, HttpStatus.INTERNAL_SERVER_ERROR);
              }catch (Exception e) {
                  logger.error("Unexpected error while saving image for product with id {}", productId, e);
                  throw new ApplicationException("Unexpected error while saving image for product with id " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
              }

              savedImage.setImageDownloadURL(buildDownloadUrl + savedImage.getImageId());

              try {
                  imageRepository.save(savedImage);
              }catch (DataAccessResourceFailureException e) {
                  logger.error("Database connection error while updating image download URL for product with id {}", productId, e);
                  throw new ImageDataAccessException("Database connection error while updating image download URL for product with id " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
              }catch (Exception e) {
                  logger.error("Unexpected error while updating image download URL for product with id {}", productId, e);
                  throw new ApplicationException("Unexpected error while updating image download URL for product with id " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
              }

              logger.info("Image with id {} saved successfully for product with id {}", savedImage.getImageId(), productId);

              ImageDTO imageDTO = new ImageDTO();
              imageDTO.setImageId(savedImage.getImageId());
              imageDTO.setImageName(savedImage.getImageFileName());
              imageDTO.setDownloadURL(savedImage.getImageDownloadURL());

              imageDTOS.add(imageDTO);


          }
          return imageDTOS;

      }

      @Transactional
      public ImageDTO updateById(MultipartFile file, Long id) throws ApplicationException {

          ImageDTO image = getById(id);
          Image imageFromDB = modelMapper.map(image, Image.class);

          imageFromDB.setImageFileName(file.getOriginalFilename());
          try {
              imageFromDB.setImagePicture(new SerialBlob(file.getBytes()));
          } catch (SQLException | IOException e) {
              logger.error("Error while converting file to Blob for image with id {}", id, e);
              throw new ImageDataAccessException("Error while converting file to Blob for image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }catch (Exception e) {
              logger.error("Unexpected error while processing file for image with id {}", id, e);
              throw new ApplicationException("Unexpected error while processing file for image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          imageFromDB.setImagefileType(file.getContentType());

          Image savedImage;
          try {
              savedImage = imageRepository.save(imageFromDB);
          } catch (DataAccessResourceFailureException e) {
              logger.error("Database connection error while updating image with id {}", id, e);
              throw new ImageDataAccessException("Database connection error while updating image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }catch ( Exception e) {
              logger.error("Unexpected error while updating image with id {}", id, e);
              throw new ApplicationException("Unexpected error while updating image with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          logger.info("Image with id {}updated successfully", id);

          return modelMapper.map(savedImage, ImageDTO.class);
      }

      public List<ImageDTO> getByProductId(Long id) throws ApplicationException {
          List<Image> images;
        try {
            images= imageRepository.findByProductId(id);
        }catch (DataAccessResourceFailureException e) {
            logger.error("Database connection error while fetching images for product with id {}", id, e);
            throw new ImageDataAccessException("Database connection error while fetching images for product with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            logger.error("Unexpected error while fetching images for product with id {}", id, e);
            throw new ApplicationException("Unexpected error while fetching images for product with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(images.isEmpty()){
            throw new ImageNotFoundException("No images found for product with id " + id, HttpStatus.NOT_FOUND);
        }
        logger.info("Images for product with id {} received successfully", id);
        List<ImageDTO> imageDTOS = images.stream().map(image -> modelMapper.map(image,ImageDTO.class)).toList();

        return imageDTOS;
      }

}
