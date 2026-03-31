import React, { useState, useEffect } from 'react';
import Sidebar from '../Sidebar';
import api from '../api';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineCalendar, HiOutlineClock, HiOutlineBookOpen, 
    HiOutlinePlusCircle, HiOutlineClipboardList, HiOutlineLocationMarker 
} from 'react-icons/hi';

const AdminExamSchedule = () => {
    const [exams, setExams] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [classes, setClasses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Form State
    const [formData, setFormData] = useState({
        subject: '',
        className: '',
        examDate: '',
        startTime: '',
        endTime: '',
        roomNumber: ''
    });

    useEffect(() => {
        fetchExams();
    }, []);

    const fetchExams = async () => {
        try {
            const res = await api.get('/api/exams-management/schedules');
            const subjectsRes = await api.get('/api/subjects');
            const classRes = await api.get('/api/classrooms');
            
            setExams(res.data);
            setSubjects(subjectsRes.data);
            setClasses(classRes.data);
        } catch (err) {
            toast.error("Failed to load exam schedules");
        } finally {
            setLoading(false);
        }
    };

    const handleSchedule = async (e) => {
        e.preventDefault();


        if (!formData.subject.trim() || !formData.examDate) {
            toast.error("Validation Error: Subject and Exam Date are required!");
            return;
        }

        setIsSubmitting(true);
        try {
            await api.post('/api/exams-management/schedule', formData);
            toast.success("Exam scheduled successfully! Notifications sent to parents.");
            
            // Reset form
            setFormData({
                subject: '', className: '', examDate: '',
                startTime: '', endTime: '', roomNumber: ''
            });
            
            fetchExams(); // Refresh list to show new schedule
        } catch (err) {
            toast.error(err.response?.data || "Failed to schedule exam");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                <div className="mb-8">
                    <h1 className="text-2xl font-bold text-gray-800">Exam Management</h1>
                    <p className="text-sm text-gray-500">Create and manage academic examination schedules</p>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    
                    {/* US1 AC1: Scheduling Form */}
                    <div className="lg:col-span-1">
                        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
                            <h2 className="text-lg font-bold text-gray-700 mb-6 flex items-center gap-2">
                                <HiOutlinePlusCircle className="text-blue-600" />
                                Schedule New Exam
                            </h2>
                            
                            <form onSubmit={handleSchedule} className="space-y-4">
                                <div>
                                    <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Subject Name *</label>
                                    <select 
                                        type="text"
                                        placeholder="e.g. Mathematics"
                                        className="w-full p-2.5 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.subject}
                                        onChange={(e) => setFormData({...formData, subject: e.target.value})}
                                    >
                                        <option value="">Select Subject</option>
                                        {subjects.map((subj) => (
                                            <option key={subj.subjectId} value={subj.subjectId}>{subj.subjectCode} - {subj.subjectName}</option>
                                        ))}
                                    </select>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Class / Grade</label>
                                    <select 
                                        type="text"
                                        placeholder="e.g. Grade 10-A"
                                        className="w-full p-2.5 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.className}
                                        onChange={(e) => setFormData({...formData, className: e.target.value})}
                                    >
                                        <option value="">Select Class</option>
                                        {classes.map((cls) => (
                                            <option key={cls.classId} value={cls.classId}>{cls.className}</option>
                                        ))}
                                    </select>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Exam Date *</label>
                                    <input 
                                        type="date"
                                        className="w-full p-2.5 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.examDate}
                                        onChange={(e) => setFormData({...formData, examDate: e.target.value})}
                                    />
                                </div>

                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Start Time</label>
                                        <input 
                                            type="time"
                                            className="w-full p-2.5 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition"
                                            value={formData.startTime}
                                            onChange={(e) => setFormData({...formData, startTime: e.target.value})}
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-xs font-bold text-gray-500 uppercase mb-1">End Time</label>
                                        <input 
                                            type="time"
                                            className="w-full p-2.5 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition"
                                            value={formData.endTime}
                                            onChange={(e) => setFormData({...formData, endTime: e.target.value})}
                                        />
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Room / Hall</label>
                                    <input 
                                        type="text"
                                        placeholder="e.g. Hall 04"
                                        className="w-full p-2.5 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition"
                                        value={formData.roomNumber}
                                        onChange={(e) => setFormData({...formData, roomNumber: e.target.value})}
                                    />
                                </div>

                                <button 
                                    type="submit"
                                    disabled={isSubmitting}
                                    className="w-full bg-blue-600 text-white py-3 rounded-lg font-bold hover:bg-blue-700 disabled:bg-blue-300 transition-all shadow-md flex justify-center items-center gap-2"
                                >
                                    {isSubmitting ? "Saving..." : "Save Schedule"}
                                </button>
                            </form>
                        </div>
                    </div>

                    {/* US1 AC1: Display Schedule List */}
                    <div className="lg:col-span-2">
                        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-5 border-b border-gray-100 bg-gray-50 flex items-center gap-2">
                                <HiOutlineClipboardList className="text-blue-600" size={20} />
                                <h2 className="font-bold text-gray-700">Scheduled Examinations</h2>
                            </div>

                            <div className="p-0">
                                {loading ? (
                                    <div className="p-10 text-center text-gray-400 italic">Loading schedules...</div>
                                ) : exams.length === 0 ? (
                                    <div className="p-10 text-center text-gray-400">
                                        No exams scheduled yet.
                                    </div>
                                ) : (
                                    <div className="divide-y divide-gray-100">
                                        {exams.map((exam) => (
                                            <div key={exam.id} className="p-5 hover:bg-gray-50 transition flex flex-col md:flex-row md:items-center justify-between gap-4">
                                                <div className="flex items-start gap-4">
                                                    <div className="bg-blue-50 text-blue-600 p-3 rounded-lg">
                                                        <HiOutlineBookOpen size={24} />
                                                    </div>
                                                    <div>
                                                        <h3 className="font-bold text-gray-800">{exam.subject}</h3>
                                                        <p className="text-xs text-gray-500 font-medium uppercase tracking-wider">{exam.className || 'All Classes'}</p>
                                                    </div>
                                                </div>

                                                <div className="flex flex-wrap gap-4 text-sm text-gray-600">
                                                    <div className="flex items-center gap-1.5">
                                                        <HiOutlineCalendar className="text-blue-500" />
                                                        {new Date(exam.examDate).toLocaleDateString(undefined, { dateStyle: 'medium' })}
                                                    </div>
                                                    <div className="flex items-center gap-1.5">
                                                        <HiOutlineClock className="text-blue-500" />
                                                        {exam.startTime} - {exam.endTime}
                                                    </div>
                                                    <div className="flex items-center gap-1.5">
                                                        <HiOutlineLocationMarker className="text-blue-500" />
                                                        {exam.roomNumber || 'TBD'}
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default AdminExamSchedule;