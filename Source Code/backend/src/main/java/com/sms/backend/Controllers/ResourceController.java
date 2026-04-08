package com.sms.backend.Controllers;

import com.sms.backend.DTO.ResourceDTO;
import com.sms.backend.Entities.EducationalResource;
import com.sms.backend.Services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*") // Adjust for your React frontend
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    // SMS_U06: Teacher uploads resources
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("teacherId") String teacherId) {
        try {
            EducationalResource savedResource = resourceService.uploadResource(file, title, description, category, teacherId);
            return ResponseEntity.ok(savedResource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    // SMS_U07: Student/Teacher lists resources
    @GetMapping("{classId}")
    public List<ResourceDTO> getAllResources(@PathVariable Long classId) {
        return resourceService.getAllResourcesByClassId(classId);
    }

    // Download/View Resource
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadResource(@PathVariable Long id) {
        try {
            EducationalResource resourceDetails = resourceService.getResourceById(id);
            Path path = Paths.get(resourceDetails.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resourceDetails.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resourceDetails.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}