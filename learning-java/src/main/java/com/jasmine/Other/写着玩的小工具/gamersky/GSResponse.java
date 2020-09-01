package com.jasmine.Other.写着玩的小工具.gamersky;

import java.util.Arrays;

public class GSResponse {

    private int errorCode;
    private String errorMessage;
    private Result result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GSResponse{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", result=" + result +
                '}';
    }

    static class Result {
        private int commentsCount;
        private Comments[] comments;

        @Override
        public String toString() {
            return "Result{" +
                    "commentsCount=" + commentsCount +
                    ", comments=" + Arrays.toString(comments) +
                    '}';
        }

        public int getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }

        public Comments[] getComments() {
            return comments;
        }

        public void setComments(Comments[] comments) {
            this.comments = comments;
        }
    }

    static class Comments {
        private int comment_id;
        private ImageInfes[] imageInfes;

        @Override
        public String toString() {
            return "Comments{" +
                    "comment_id=" + comment_id +
                    ", imageInfes=" + Arrays.toString(imageInfes) +
                    '}';
        }

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public ImageInfes[] getImageInfes() {
            return imageInfes;
        }

        public void setImageInfes(ImageInfes[] imageInfes) {
            this.imageInfes = imageInfes;
        }
    }

    static class ImageInfes {
        private String origin;
        private String imageType;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getImageType() {
            return imageType;
        }

        public void setImageType(String imageType) {
            this.imageType = imageType;
        }

        @Override
        public String toString() {
            return "imageInfes{" +
                    "origin='" + origin + '\'' +
                    ", imageType='" + imageType + '\'' +
                    '}';
        }
    }
}
