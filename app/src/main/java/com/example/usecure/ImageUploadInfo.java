package com.example.usecure;

public class ImageUploadInfo {
    public String imageName;

    public String imageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String imageName, String imageURL) {

        this.imageName = imageName;
        this.imageURL= imageURL;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

}
