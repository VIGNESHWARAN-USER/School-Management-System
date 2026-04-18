import React, { useState, useEffect } from 'react';
import Sidebar from '../Sidebar';
import { 
    HiOutlineClock, 
    HiOutlineBookOpen, 
    HiOutlineUser, 
    HiOutlinePrinter, 
    HiOutlineCalendar,
    HiOutlineInformationCircle
} from 'react-icons/hi';
import api from "../api";

const ScheduleView = () => {
    const [schedules, setSchedules] = useState([]);
    const [loading, setLoading] = useState(true);
    const [metadata, setMetadata] = useState({ title: "Class Timetable", subtitle: "" });

    const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
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
        fetchData();
    }, []);

    const user = JSON.parse(localStorage.getItem("userData"));
    const classId = user?.classId;
    const role = localStorage.getItem("accessLevel");

    const fetchData = async () => {
        try {
            setLoading(true);
            let endpoint = `/api/fetchClassSchedule/${classId}`;
            const res = await api.get(endpoint);
            setSchedules(res.data);
            
            // Set Subtitle based on data (e.g., "Class 10-A" or "Mr. Smith's Schedule")
            if (res.data.length > 0) {
                setMetadata({
                    title: role === 'teacher' ? "Teaching Schedule" : "Class Timetable",
                    subtitle: role === 'teacher' ? "Personalized Weekly View" : `Section: ${res.data[0].className || 'Assigned Class'}`
                });
            }
        } catch (err) {
            console.error("Error fetching schedule", err);
        } finally {
            setLoading(false);
        }
    };

    const handlePrint = () => {
        window.print();
    };

    if (loading) return (
        <div className="flex h-screen items-center justify-center bg-gray-50">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
    );

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            
            <div className="flex-1 p-8 overflow-y-auto print:p-0">
                {/* Header - Hidden during Print via CSS if needed, but usually kept for context */}
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4 print:hidden">
                    <div>
                        <h1 className="text-2xl font-black text-gray-800 tracking-tight">{metadata.title}</h1>
                        <p className="text-gray-500 font-medium flex items-center gap-2">
                            <HiOutlineCalendar className="text-blue-500" /> {metadata.subtitle}
                        </p>
                    </div>
                    <button 
                        onClick={handlePrint}
                        className="flex items-center space-x-2 bg-white border border-gray-200 text-gray-700 px-4 py-2 rounded-xl hover:bg-gray-50 transition shadow-sm font-bold"
                    >
                        <HiOutlinePrinter size={20} /> <span>Print Schedule</span>
                    </button>
                </div>


                {/* Main Schedule Table */}
                <div className="bg-white rounded-3xl shadow-xl shadow-gray-200/50 border border-gray-100 overflow-hidden print:shadow-none print:border-gray-300">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-gray-50/50 border-b border-gray-100">
                                    <th className="p-6 text-xs font-black text-gray-400 uppercase tracking-[0.15em] w-48">Time Period</th>
                                    {days.map(day => (
                                        <th key={day} className="p-6 text-xs font-black text-gray-400 uppercase tracking-[0.15em] text-center border-l border-gray-50">
                                            {day}
                                        </th>
                                    ))}
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {timeSlots.map((time, idx) => {
                                    const isSpecial = time === "Break" || time === "Lunch";
                                    const slotStartTime = !isSpecial ? time.split(' - ')[0] : null;

                                    return (
                                        <tr key={idx} className={`${isSpecial ? "bg-gray-50/30" : "hover:bg-blue-50/10 transition-colors"}`}>
                                            <td className="p-6 align-middle">
                                                <div className={`flex items-center gap-3 text-sm font-bold ${isSpecial ? "text-amber-500" : "text-gray-500"}`}>
                                                    <HiOutlineClock size={18} className={isSpecial ? "text-amber-400" : "text-blue-400"} /> 
                                                    {time}
                                                </div>
                                            </td>

                                            {days.map(day => {
                                                const slot = schedules.find(s => s.dayOfWeek === day && s.startTime === slotStartTime);

                                                return (
                                                    <td key={day} className="p-3 border-l border-gray-50 min-w-[180px] h-36 align-top">
                                                        {isSpecial ? (
                                                            <div className="flex items-center justify-center h-full">
                                                                <span className="text-[10px] font-black uppercase tracking-[0.3em] text-gray-300 rotate-180 [writing-mode:vertical-lr]">
                                                                    {time}
                                                                </span>
                                                            </div>
                                                        ) : slot ? (
                                                            <div className="h-full bg-gradient-to-br from-white to-gray-50/50 border border-gray-100 p-4 rounded-2xl shadow-sm flex flex-col justify-between">
                                                                <div>
                                                                    <div className="flex items-center gap-2 mb-2">
                                                                        <div className="w-1.5 h-1.5 rounded-full bg-blue-500"></div>
                                                                        <p className="text-xs font-black text-gray-800 uppercase tracking-tight truncate">
                                                                            {slot.subjectName}
                                                                        </p>
                                                                    </div>
                                                                    <p className="text-[11px] text-gray-500 flex items-center gap-1.5 font-bold mb-1">
                                                                        <HiOutlineUser className="text-blue-300" size={14}/> 
                                                                        {role === 'teacher' ? `Room: ${slot.roomNumber || 'N/A'}` : slot.teacherName}
                                                                    </p>
                                                                </div>
                                                                
                                                                <div className="flex items-center gap-1.5 mt-auto">
                                                                    <HiOutlineBookOpen className="text-gray-300" size={14} />
                                                                    <span className="text-[10px] text-gray-400 font-bold uppercase">
                                                                        {slot.className || 'Theory'}
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        ) : (
                                                            <div className="h-full w-full rounded-2xl border border-gray-50 flex items-center justify-center">
                                                                <div className="w-1 h-1 bg-gray-100 rounded-full"></div>
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

                {/* Print Footer */}
                <div className="hidden print:block mt-8 text-center border-t pt-4 text-gray-400 text-xs">
                    Generated by School Management System — {new Date().toLocaleDateString()}
                </div>
            </div>
        </div>
    );
};

export default ScheduleView;