package com.sms.service;

//Author : Shobana V 
/*
*  This class handles Fee Structure related operations
*/
import java.util.List;

import com.sms.dao.FeeStructureDAO;
import com.sms.dao.ResourceDAO;
import com.sms.entities.FeeStructure;

public class FeeStructureService {

    // DAO objects to interact with database
    private final FeeStructureDAO feeStructureDAO = new FeeStructureDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();

    // Add a new Fee Structure
    public String addFeeStructure(FeeStructure feeStructure) {

        // Validate if classroom exists before adding fee structure
        if (!resourceDAO.isClassRoomExists(feeStructure.getClassRoomId())) {
            return "Validation Error: ClassRoom ID " + feeStructure.getClassRoomId() + " does not exist!";
        }

        boolean success = feeStructureDAO.addFeeStructure(feeStructure);
        return success ? "Fee Structure created successfully!" : "Failed to create Fee Structure.";
    }

    // Update existing Fee Structure
    public String updateFeeStructure(FeeStructure feeStructure) {
    	
        if (!resourceDAO.isClassRoomExists(feeStructure.getClassRoomId())) {
            return "Validation Error: ClassRoom ID " + feeStructure.getClassRoomId() + " does not exist!";
        }

        boolean success = feeStructureDAO.updateFeeStructure(feeStructure);
        return success ? "Fee Structure updated successfully!" : "Failed to update Fee Structure or not found.";
    }

    // Delete Fee Structure using ID
    public String deleteFeeStructure(long id) {
        boolean success = feeStructureDAO.deleteFeeStructure(id);
        return success ? "Fee Structure deleted successfully!" : "Failed to delete Fee Structure. Check ID.";
    }

    // Get all Fee Structures
    public List<FeeStructure> getAllFeeStructures() { // Collections used
        return feeStructureDAO.getAllFeeStructures();
    }
}