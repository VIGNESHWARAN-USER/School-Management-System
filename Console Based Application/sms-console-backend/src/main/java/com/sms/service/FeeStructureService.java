package com.sms.service;

import java.util.List;

import com.sms.dao.FeeStructureDAO;
import com.sms.dao.ResourceDAO;
import com.sms.entities.FeeStructure;

public class FeeStructureService {

    private final FeeStructureDAO feeStructureDAO = new FeeStructureDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();

    public String addFeeStructure(FeeStructure feeStructure) {
        if (!resourceDAO.isClassRoomExists(feeStructure.getClassRoomId())) {
            return "Validation Error: ClassRoom ID " + feeStructure.getClassRoomId() + " does not exist!";
        }
        boolean success = feeStructureDAO.addFeeStructure(feeStructure);
        return success ? "Fee Structure created successfully!" : "Failed to create Fee Structure.";
    }

    public String updateFeeStructure(FeeStructure feeStructure) {
        if (!resourceDAO.isClassRoomExists(feeStructure.getClassRoomId())) {
            return "Validation Error: ClassRoom ID " + feeStructure.getClassRoomId() + " does not exist!";
        }
        boolean success = feeStructureDAO.updateFeeStructure(feeStructure);
        return success ? "Fee Structure updated successfully!" : "Failed to update Fee Structure or not found.";
    }

    public String deleteFeeStructure(long id) {
        boolean success = feeStructureDAO.deleteFeeStructure(id);
        return success ? "Fee Structure deleted successfully!" : "Failed to delete Fee Structure. Check ID.";
    }

    public List<FeeStructure> getAllFeeStructures() {
        return feeStructureDAO.getAllFeeStructures();
    }
}
