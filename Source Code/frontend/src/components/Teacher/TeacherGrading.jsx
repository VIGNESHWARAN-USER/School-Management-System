import React, { useState, useEffect } from 'react';
import Sidebar from '../Sidebar';
import api from '../api';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineAcademicCap, HiOutlineCheckCircle, HiOutlineUserGroup, 
    HiOutlineCalculator, HiOutlineSave, HiOutlineBookOpen 
} from 'react-icons/hi';

const TeacherGrading = () => {
    const [exams, setExams] = useState([]);
    const [students, setStudents] = useState([]);
    const [loadingExams, setLoadingExams] = useState(true);
    const [isSaving, setIsSaving] = useState(false);
    const userData = localStorage.getItem("userData");
    const user = userData ? JSON.parse(userData) : null;
    // Form State
    const [formData, setFormData] = useState({
        studentId: '',
        studentName: '',
        examId: '',
        subject: '',
        marksObtained: '',
        totalMarks: '100', // Default total marks
        remarks: ''
    });

    useEffect(() => {
        fetchExams();
    }, []);

    const fetchExams = async () => {
        try {
            // Fetch scheduled exams to populate the dropdown
            const res = await api.get('/api/exams-management/schedules');
            const students = await api.get(`/api/fetchAllStudents/${user.classId}`);
            setExams(res.data);
        } catch (err) {
            toast.error("Failed to load exams list");
        } finally {
            setLoadingExams(false);
        }
    };

    const handleExamChange = (e) => {
        const selectedExamId = e.target.value;
        const selectedExam = exams.find(ex => ex.id.toString() === selectedExamId);
        
        if (selectedExam) {
            setFormData({
                ...formData,
                examId: selectedExamId,
                subject: selectedExam.subject
            });
        }
    };

    const handleSaveGrade = async (e) => {
        e.preventDefault();

        // US2 AC2: Validation - Error message if marks are missing
        if (!formData.marksObtained || formData.marksObtained === '') {
            toast.error("Error: Please enter the marks obtained by the student.");
            return;
        }

        if (!formData.studentId || !formData.examId) {
            toast.error("Error: Student ID and Exam selection are required.");
            return;
        }

        setIsSaving(true);
        try {
            // US2 AC1: Store marks and trigger backend grade calculation
            await api.post('/api/exams-management/grades', {
                ...formData,
                marksObtained: parseFloat(formData.marksObtained),
                totalMarks: parseFloat(formData.totalMarks)
            });
            
            toast.success(`Grades for ${formData.studentName || 'Student'} saved successfully!`);
            
            // Clear specific fields for next entry
            setFormData({
                ...formData,
                studentId: '',
                studentName: '',
                marksObtained: '',
                remarks: ''
            });
        } catch (err) {
            toast.error(err.response?.data || "Failed to save marks");
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                <div className="mb-8">
                    <h1 className="text-2xl font-bold text-gray-800">Student Grading</h1>
                    <p className="text-sm text-gray-500">Enter and record examination marks for students</p>
                </div>

                <div className="max-w-4xl mx-auto">
                    <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                        <div className="bg-blue-600 p-6 text-white">
                            <div className="flex items-center gap-3">
                                <HiOutlineAcademicCap size={32} />
                                <div>
                                    <h2 className="text-xl font-bold">Grade Entry Form</h2>
                                    <p className="text-blue-100 text-sm">Fill in the details to record performance</p>
                                </div>
                            </div>
                        </div>

                        <form onSubmit={handleSaveGrade} className="p-8">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                                {/* Exam Selection */}
                                <div className="space-y-1">
                                    <label className="text-sm font-bold text-gray-600 flex items-center gap-2">
                                        <HiOutlineBookOpen className="text-blue-500" /> Select Exam
                                    </label>
                                    <select 
                                        required
                                        className="w-full p-3 border border-gray-300 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.examId}
                                        onChange={handleExamChange}
                                    >
                                        <option value="">-- Select Scheduled Exam --</option>
                                        {exams.map(exam => (
                                            <option key={exam.id} value={exam.id}>
                                                {exam.subject} ({exam.className}) - {new Date(exam.examDate).toLocaleDateString()}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                {/* Student ID */}
                                <div className="space-y-1">
                                    <label className="text-sm font-bold text-gray-600 flex items-center gap-2">
                                        <HiOutlineUserGroup className="text-blue-500" /> Student ID
                                    </label>
                                    <input 
                                        type="text"
                                        placeholder="e.g. STU1001"
                                        required
                                        className="w-full p-3 border border-gray-300 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.studentId}
                                        onChange={(e) => setFormData({...formData, studentId: e.target.value})}
                                    />
                                </div>

                                {/* Student Name */}
                                <div className="space-y-1">
                                    <label className="text-sm font-bold text-gray-600">Student Name</label>
                                    <input 
                                        type="text"
                                        placeholder="Enter full name"
                                        className="w-full p-3 border border-gray-300 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.studentName}
                                        onChange={(e) => setFormData({...formData, studentName: e.target.value})}
                                    />
                                </div>

                                {/* Marks Entry */}
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="space-y-1">
                                        <label className="text-sm font-bold text-gray-600 flex items-center gap-2">
                                            <HiOutlineCalculator className="text-blue-500" /> Marks Obtained
                                        </label>
                                        <input 
                                            type="number"
                                            placeholder="0"
                                            className="w-full p-3 border border-gray-300 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 transition"
                                            value={formData.marksObtained}
                                            onChange={(e) => setFormData({...formData, marksObtained: e.target.value})}
                                        />
                                    </div>
                                    <div className="space-y-1">
                                        <label className="text-sm font-bold text-gray-600">Total Marks</label>
                                        <input 
                                            type="number"
                                            className="w-full p-3 border border-gray-100 bg-gray-50 rounded-xl outline-none"
                                            value={formData.totalMarks}
                                            readOnly
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className="mb-8">
                                <label className="text-sm font-bold text-gray-600 block mb-1">Teacher Remarks</label>
                                <textarea 
                                    rows="3"
                                    placeholder="Add any feedback regarding performance..."
                                    className="w-full p-3 border border-gray-300 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 transition"
                                    value={formData.remarks}
                                    onChange={(e) => setFormData({...formData, remarks: e.target.value})}
                                ></textarea>
                            </div>

                            <div className="flex justify-end">
                                <button 
                                    type="submit"
                                    disabled={isSaving}
                                    className="flex items-center gap-2 bg-blue-600 text-white px-8 py-3 rounded-xl font-bold hover:bg-blue-700 active:scale-95 transition-all shadow-lg disabled:bg-blue-300"
                                >
                                    {isSaving ? "Saving..." : (
                                        <>
                                            <HiOutlineSave size={20} />
                                            Save Marks & Publish
                                        </>
                                    )}
                                </button>
                            </div>
                        </form>
                    </div>

                    {/* Helper Info */}
                    <div className="mt-6 flex items-start gap-3 p-4 bg-blue-50 rounded-xl border border-blue-100 text-blue-700 text-sm">
                        <HiOutlineCheckCircle size={20} className="shrink-0" />
                        <p>
                            <strong>Tip:</strong> Saving marks will automatically calculate the letter grade (A, B, C) and send a notification to the parent's portal.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TeacherGrading;