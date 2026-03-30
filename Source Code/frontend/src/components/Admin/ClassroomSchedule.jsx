import React, { useState, useEffect } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineClock, 
    HiOutlineBookOpen, 
    HiOutlineUser, 
    HiOutlineHome, 
    HiOutlineCalendar, 
    HiOutlinePlus,
    HiOutlineTrash,
    HiOutlineX,
    HiOutlineArrowLeft
} from 'react-icons/hi';
import api from "../api";

const ManageSchedule = () => {
    const [loading, setLoading] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    
    // Data lists for dropdowns and mapping
    const [classes, setClasses] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [teachers, setTeachers] = useState([]);
    const [schedules, setSchedules] = useState([]);

    const [selectedClassId, setSelectedClassId] = useState('');

    // Form state
    const [formData, setFormData] = useState({
        classId: '',
        subjectId: '',
        teacherId: '',
        dayOfWeek: 'Monday',
        startTime: '',
        endTime: ''
    });

    const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    // Your specific time slot structure
    const timeSlots = [
        "09:00 - 10:40", 
        "Break", 
        "10:40 - 12:35", 
        "Lunch", 
        "01:25 - 02:55", 
        "Break", 
        "03:15 - 04:40"
    ];

    useEffect(() => {
        fetchInitialData();
    }, []);

    const fetchInitialData = async () => {
        try {
            const [resClasses, resSubjects, resTeachers] = await Promise.all([
                api.get('/api/classrooms'),
                api.get('/api/subjects'),
                api.get('/api/teachers')
            ]);

            setClasses(resClasses.data);
            setSubjects(resSubjects.data);
            setTeachers(resTeachers.data);
            
            if (resClasses.data.length > 0) {
                const firstId = resClasses.data[0].classId;
                setSelectedClassId(firstId);
                fetchSchedule(firstId);
            }
        } catch (err) {
            toast.error("Failed to load setup data");
        }
    };

    const fetchSchedule = async (classId) => {
        try {
            const res = await api.get(`/api/fetchClassSchedule/${classId}`);
            setSchedules(res.data);
        } catch (err) {
            toast.error("Error fetching schedule");
        }
    };

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Helper functions to resolve Names from IDs
    const getSubjectName = (id) => {
        const subject = subjects.find(s => s.subjectId === parseInt(id));
        return subject ? subject.subjectName : 'Unknown Subject';
    };

    const getTeacherName = (id) => {
        const teacher = teachers.find(t => t.id === parseInt(id));
        return teacher ? teacher.name : 'Unknown Teacher';
    };

    const submitSlot = async (e) => {
        e.preventDefault();
        setLoading(true);

        const payload = {
            classId: parseInt(formData.classId || selectedClassId),
            subjectId: parseInt(formData.subjectId),
            teacherId: parseInt(formData.teacherId),
            dayOfWeek: formData.dayOfWeek,
            startTime: formData.startTime,
            endTime: formData.endTime
        };

        const promise = api.post('/api/addSchedule', payload);

        toast.promise(promise, {
            loading: 'Saving slot...',
            success: () => {
                setIsModalOpen(false);
                fetchSchedule(selectedClassId);
                return 'Schedule slot added!';
            },
            error: (err) => `Error: ${err.response?.data || err.message}`
        });
        setLoading(false);
    };

    // Open modal and pre-fill time/day if clicking on empty cell
    const handleCellClick = (day, timeStr) => {
        if (timeStr === "Break" || timeStr === "Lunch") return;
        
        const [start, end] = timeStr.split(' - ');
        setFormData({
            ...formData,
            classId: selectedClassId,
            dayOfWeek: day,
            startTime: start,
            endTime: end
        });
        setIsModalOpen(true);
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                {/* Header Section */}
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">Classroom Schedule</h1>
                        <p className="text-gray-500">Manage daily periods and teacher assignments</p>
                    </div>
                    <div className="flex items-center gap-4">
                        <div className="flex items-center bg-white border border-gray-200 rounded-lg px-3 py-1 shadow-sm">
                            <span className="text-xs font-bold text-gray-400 uppercase mr-2">Viewing Class:</span>
                            <select 
                                value={selectedClassId} 
                                onChange={(e) => {
                                    setSelectedClassId(e.target.value);
                                    fetchSchedule(e.target.value);
                                }}
                                className="bg-transparent font-bold text-blue-600 outline-none cursor-pointer"
                            >
                                {classes.map(c => <option key={c.classId} value={c.classId}>{c.className} - {c.section}</option>)}
                            </select>
                        </div>
                        <button 
                            onClick={() => setIsModalOpen(true)}
                            className="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition shadow-md font-bold"
                        >
                            <HiOutlinePlus size={20} /> <span>Add Period</span>
                        </button>
                    </div>
                </div>

                {/* Timetable Grid */}
                <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-gray-50 border-b border-gray-100">
                                    <th className="p-4 text-xs font-bold text-gray-400 uppercase tracking-widest w-40">Time Range</th>
                                    {days.map(day => (
                                        <th key={day} className="p-4 text-xs font-bold text-gray-400 uppercase tracking-widest text-center border-l border-gray-50">{day}</th>
                                    ))}
                                </tr>
                            </thead>
                            <tbody>
                                {timeSlots.map((time, idx) => {
                                    const isSpecial = time === "Break" || time === "Lunch";
                                    // Extract start time for matching data: "09:00 - 10:40" -> "09:00"
                                    const slotStartTime = !isSpecial ? time.split(' - ')[0] : null;

                                    return (
                                        <tr key={idx} className={`border-b border-gray-50 ${isSpecial ? "bg-amber-50/30" : "group"}`}>
                                            <td className="p-4 align-middle">
                                                <div className={`flex items-center gap-2 text-sm font-bold ${isSpecial ? "text-amber-500" : "text-gray-400"}`}>
                                                    <HiOutlineClock size={16} /> {time}
                                                </div>
                                            </td>
                                            
                                            {days.map(day => {
                                                const slot = schedules.find(s => s.dayOfWeek === day && s.startTime === slotStartTime);
                                                
                                                return (
                                                    <td 
                                                        key={day} 
                                                        className={`p-2 border-l border-gray-50 min-w-[160px] h-32 align-top transition-colors`}
                                                    >
                                                        {isSpecial ? (
                                                            <div className="flex items-center justify-center h-full text-[10px] font-black uppercase tracking-[0.2em] text-amber-300">
                                                                {time}
                                                            </div>
                                                        ) : slot ? (
                                                            <div className="bg-white border border-blue-100 border-l-4 border-l-blue-500 p-3 rounded-xl shadow-sm relative group/slot hover:shadow-md transition-all">
                                                                <p className="text-xs font-black text-blue-900 uppercase mb-1 truncate">
                                                                    {getSubjectName(slot.subjectId)}
                                                                </p>
                                                                <p className="text-[11px] text-gray-500 flex items-center gap-1 font-semibold truncate">
                                                                    <HiOutlineUser className="text-blue-400" size={14}/> 
                                                                    {getTeacherName(slot.teacherId)}
                                                                </p>
                                                                <div className="mt-3 flex items-center justify-between">
                                                                    <span className="text-[9px] bg-blue-50 text-blue-600 px-2 py-0.5 rounded-full font-bold">
                                                                        {slot.startTime} - {slot.endTime}
                                                                    </span>
                                                                    <button className="opacity-0 group-hover/slot:opacity-100 text-red-300 hover:text-red-500 transition-opacity">
                                                                        <HiOutlineTrash size={16} />
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        ) : (
                                                            <div 
                                                                onClick={() => handleCellClick(day, time)}
                                                                className="w-full h-full rounded-xl border-2 border-dashed border-transparent hover:border-gray-200 hover:bg-gray-50 flex items-center justify-center text-transparent hover:text-gray-300 cursor-pointer transition-all"
                                                            >
                                                                <HiOutlinePlus size={24} />
                                                            </div>
                                                        )}
                                                    </td>
                                                );
                                            })}
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            {/* ADD SLOT MODAL */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl shadow-2xl w-full max-w-2xl overflow-hidden animate-in fade-in zoom-in duration-200">
                        <div className="flex items-center justify-between p-6 border-b border-gray-100">
                            <div>
                                <h2 className="text-xl font-bold text-gray-800">Add Schedule Slot</h2>
                                <p className="text-sm text-gray-400">Assign a teacher and subject to a period</p>
                            </div>
                            <button onClick={() => setIsModalOpen(false)} className="bg-gray-50 p-2 rounded-full text-gray-400 hover:text-gray-600 transition">
                                <HiOutlineX size={20} />
                            </button>
                        </div>
                        
                        <form onSubmit={submitSlot} className="p-8 space-y-6">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-xs font-bold text-gray-400 uppercase tracking-widest mb-2">Classroom</label>
                                    <div className="relative">
                                        <HiOutlineHome className="absolute left-3 top-3 text-gray-400" size={18} />
                                        <select name="classId" value={formData.classId || selectedClassId} onChange={handleInputChange} className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none bg-gray-50/50">
                                            {classes.map(c => <option key={c.classId} value={c.classId}>{c.className} - {c.section}</option>)}
                                        </select>
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-400 uppercase tracking-widest mb-2">Day of Week</label>
                                    <div className="relative">
                                        <HiOutlineCalendar className="absolute left-3 top-3 text-gray-400" size={18} />
                                        <select name="dayOfWeek" value={formData.dayOfWeek} onChange={handleInputChange} className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none bg-gray-50/50">
                                            {days.map(d => <option key={d} value={d}>{d}</option>)}
                                        </select>
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-400 uppercase tracking-widest mb-2">Subject</label>
                                    <div className="relative">
                                        <HiOutlineBookOpen className="absolute left-3 top-3 text-gray-400" size={18} />
                                        <select name="subjectId" required value={formData.subjectId} onChange={handleInputChange} className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none bg-white shadow-sm">
                                            <option value="">Select Subject</option>
                                            {subjects.map(s => <option key={s.subjectId} value={s.subjectId}>{s.subjectName}</option>)}
                                        </select>
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-400 uppercase tracking-widest mb-2">Teacher</label>
                                    <div className="relative">
                                        <HiOutlineUser className="absolute left-3 top-3 text-gray-400" size={18} />
                                        <select name="teacherId" required value={formData.teacherId} onChange={handleInputChange} className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none bg-white shadow-sm">
                                            <option value="">Select Teacher</option>
                                            {teachers.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
                                        </select>
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-400 uppercase tracking-widest mb-2">Start Time</label>
                                    <div className="relative">
                                        <HiOutlineClock className="absolute left-3 top-3 text-gray-400" size={18} />
                                        <input type="time" name="startTime" value={formData.startTime} onChange={handleInputChange} className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none" />
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-bold text-gray-400 uppercase tracking-widest mb-2">End Time</label>
                                    <div className="relative">
                                        <HiOutlineClock className="absolute left-3 top-3 text-gray-400" size={18} />
                                        <input type="time" name="endTime" value={formData.endTime} onChange={handleInputChange} className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none" />
                                    </div>
                                </div>
                            </div>

                            <div className="flex items-center justify-end space-x-4 pt-6 border-t border-gray-100">
                                <button 
                                    type="button" 
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-6 py-2.5 rounded-xl font-bold text-gray-500 hover:bg-gray-100 transition"
                                >
                                    Cancel
                                </button>
                                <button 
                                    type="submit" 
                                    disabled={loading}
                                    className="bg-blue-600 text-white px-10 py-2.5 rounded-xl font-bold hover:bg-blue-700 transition shadow-lg shadow-blue-200 disabled:bg-blue-300"
                                >
                                    {loading ? 'Saving...' : 'Add to Schedule'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ManageSchedule;