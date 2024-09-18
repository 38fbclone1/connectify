package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditFileRequest;
import com.connectify.connectify.DTO.response.FileResponse;
import com.connectify.connectify.entity.File;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private S3Client s3Client;

    @Value("${S3.BUCKET_NAME}")
    private String BUCKET_NAME;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ModelMapper mapper;

    public void create (File file) {
        fileRepository.save(file);
    }

    public File setFileUrl (EditFileRequest request) {
        String fileUrl = upload(request.getFile());

        File newFile = new File();
        newFile.setUrl(fileUrl);

        return newFile;
    }

    public String upload(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        try {
            String contentType = multipartFile.getContentType();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .acl("public-read")
                    .contentType(contentType)
                    .contentDisposition("inline")
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
            );

            return String.format("https://%s.s3.amazonaws.com/%s", BUCKET_NAME, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(EError.BAD_REQUEST);
        }
    }

    public List<FileResponse> findByPostId(String postId) {
        List<File> files = fileRepository.findByPostId(postId);

        return files.stream()
                .map(a -> mapper.map(a, FileResponse.class))
                .toList();
    }
}
