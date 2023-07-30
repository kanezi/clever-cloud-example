package com.kanezi.clevercloudexample;

import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@Value
@NonFinal
@Log4j2
public class AvatarService {

    S3Service s3Service;
    AvatarRepository avatarRepository;

    @Cacheable("avatar")
    public List<Avatar> getAvatars() {
        return avatarRepository
                .findAll()
                .stream()
                .peek(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread()
                              .interrupt();
                    }
                })
                .toList();
    }

    @CacheEvict(value = "avatar", allEntries = true)
    public Avatar insertAvatar(String name, MultipartFile image) throws IOException {
        URL imageUrl = s3Service.saveFile(image);
        log.info("inserting avatar: {} / {}", name, image.toString());
        return avatarRepository.save(new Avatar(name, imageUrl.toString()));
    }

    @CacheEvict(value = "avatar", allEntries = true)
    public Avatar insertAvatar(String name, File image) throws IOException {
        URL imageUrl = s3Service.saveFile(image);
        return avatarRepository.save(new Avatar(name, imageUrl.toString()));
    }

    @CacheEvict(value = "avatar", allEntries = true)
    public void deleteAvatar(String name) {
        s3Service.deleteFile(name);
        avatarRepository.deleteById(name);
    }
}
