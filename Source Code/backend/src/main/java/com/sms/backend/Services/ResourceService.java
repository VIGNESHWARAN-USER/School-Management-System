package com.sms.backend.Services;

import com.sms.backend.DTO.ResourceDTO;
import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Entities.EducationalResource;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Enum.ResourceCategory;
import com.sms.backend.Repositories.ClassRoomRepository;
import com.sms.backend.Repositories.ResourceRepository;
import com.sms.backend.Repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository repository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;

    // Define upload directory in application.properties (e.g., upload.path=uploads/)
    @Value("${upload.path}")
    private String uploadPath;

    public EducationalResource uploadResource(MultipartFile file, String title, String description,
                                              String category, String teacherId) throws IOException {

        // 1. Create directory if it doesn't exist
        File directory = new File(uploadPath);
        if (!directory.exists()) directory.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath + fileName);
        Files.write(path, file.getBytes());

        EducationalResource resource = new EducationalResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setCategory(ResourceCategory.valueOf(category.toUpperCase()));
        resource.setFileName(fileName);
        resource.setFileType(file.getContentType());
        resource.setFilePath(path.toString());
        resource.setUploadDate(LocalDateTime.now());

        Teacher teacher = teacherRepository.findById(Long.valueOf(teacherId)).orElse(null);
        assert teacher != null;
        ClassRoom classRoom = teacher.getClassRoom();
        resource.setClassRoom(classRoom);
        List<EducationalResource> resources = classRoom.getResources();
        resources.add(resource);
        classRoom.setResources(resources);
        return repository.save(resource);
    }

    public List<ResourceDTO> getAllResourcesByClassId(Long classId) {
        List<EducationalResource> educationalResources =  classRoomRepository.findByClassId(classId).getResources();

        return educationalResources.stream().map(data ->{
            ResourceDTO dto = new ResourceDTO();

            dto.setClassId(String.valueOf(data.getClassRoom().getClassId()));
            dto.setId(data.getId());
            dto.setCategory(String.valueOf(data.getCategory()));
            dto.setDescription(data.getDescription());
            dto.setUploadDate(data.getUploadDate());
            dto.setFileName(data.getFileName());
            dto.setFilePath(data.getFilePath());
            dto.setFileType(data.getFileType());
            dto.setTitle(data.getTitle());

            return dto;
        }).toList();
    }


    public EducationalResource getResourceById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
    }
}