package com.sample.image.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long imageId;

    @Column(name = "filename")
    private String imageFileName;

    @Column(name = "filetype")
    private String imagefileType;

    @Lob
    @Column(name = "picture")
    private Blob imagePicture;

    @Column(name = "url")
    private String imageDownloadURL;

    @Column(name = "product_id", nullable = false)
    private Long productId;


    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImagefileType() {
        return imagefileType;
    }

    public void setImagefileType(String imagefileType) {
        this.imagefileType = imagefileType;
    }

    public Blob getImagePicture() {
        return imagePicture;
    }

    public void setImagePicture(Blob imagePicture) {
        this.imagePicture = imagePicture;
    }

    public String getImageDownloadURL() {
        return imageDownloadURL;
    }

    public void setImageDownloadURL(String imageDownloadURL) {
        this.imageDownloadURL = imageDownloadURL;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

//    @ManyToOne
//    @JoinColumn(name = "product_id",referencedColumnName = "productId")
//    @JsonBackReference
//    private Product product;
