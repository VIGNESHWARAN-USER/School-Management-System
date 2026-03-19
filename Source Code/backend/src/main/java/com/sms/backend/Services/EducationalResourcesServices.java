package com.sms.backend.Services;

import com.sms.backend.Entities.Resource;
import com.sms.backend.Entities.Assignment;
import com.sms.backend.Entities.Submission;
import com.sms.backend.Repositories.ResourceRepository;
import com.sms.backend.Repositories.AssignmentRepository;
import com.sms.backend.Repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class EducationalResourcesServices {
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    SubmissionRepository submissionRepository;
    // 1️⃣ Add Resource
    public String addResource(Resource resource)
    {
        resourceRepository.save(resource);
        return "Resource Added Successfully";
    }
    // 2️⃣ Add Assignment
    public String addAssignment(Assignment assignment)
    {
        assignmentRepository.save(assignment);
        return "Assignment Added Successfully";
    }
    // 3️⃣ Submit Assignment
    public String submitAssignment(Submission submission)
    {
        submissionRepository.save(submission);
        return "Assignment Submitted Successfully";
    }
}