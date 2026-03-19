package com.sms.backend.Controllers;

import com.sms.backend.Entities.Resource;
import com.sms.backend.Entities.Assignment;
import com.sms.backend.Entities.Submission;
import com.sms.backend.Services.ResourceAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/resource")
public class EducationalResources {
    @Autowired
    ResourceAssignmentService resourceAssignmentService;
    // 1️⃣ Add Resource
    @PostMapping("/addResource")
    public String addResource(@RequestBody Resource resource) {
        return resourceAssignmentService.addResource(resource);
    }
    // 2️⃣ Add Assignment
    @PostMapping("/addAssignment")
    public String addAssignment(@RequestBody Assignment assignment)
    {
        return resourceAssignmentService.addAssignment(assignment);
    }
    // 3️⃣ Submit Assignment
    @PostMapping("/submitAssignment")
    public String submitAssignment(@RequestBody Submission submission)
    {
        return resourceAssignmentService.submitAssignment(submission);
    }
}