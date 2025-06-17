package com.sample.image.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Blob;

public class ImageDTO {

    private Long id;

    private String imageName;

    @JsonIgnore
    private String imageFileType;

    private String downloadURL;

    @JsonIgnore
    private Blob imagePicture;

    @JsonIgnore
    private Long productId;


    public Long getId() {
        return id;
    }

    public void setImageId(Long imageId) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageFileType() {
        return imageFileType;
    }

    public void setImageFileType(String imageFileType) {
        this.imageFileType = imageFileType;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public Blob getImagePicture() {
        return imagePicture;
    }

    public void setImagePicture(Blob imagePicture) {
        this.imagePicture = imagePicture;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
