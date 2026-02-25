package com.yummie.util;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    private final ImageKit imageKit;

    public String uploadImage(MultipartFile image, String folder, String fileName) {
        try {
            FileCreateRequest fileCreateRequest = new FileCreateRequest(image.getBytes(), fileName);
            fileCreateRequest.setUseUniqueFileName(false);
            fileCreateRequest.setFolder(folder);
            Result result = imageKit.upload(fileCreateRequest);
            return result.getUrl();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image");
        }
    }
}
