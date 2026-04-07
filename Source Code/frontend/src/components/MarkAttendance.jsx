import React, { useEffect, useState, useMemo } from 'react';
import Sidebar from './Sidebar';
import { toast, Toaster } from 'sonner';
import { HiOutlineSearch, HiCheckCircle, HiXCircle, HiClock } from 'react-icons/hi';
import api from './api';

const MarkAttendance = () => {
    const [members, setMembers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [attendanceData, setAttendanceData] = useState({}); 
    const [loading, setLoading] = useState(true);
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);

    // Get user info from localStorage
    const user = JSON.parse(localStorage.getItem('userData')) || { role: 'Admin', id: '1' }; 
    const accessLevel = localStorage.getItem('accessLevel');

    useEffect(() => {

        

        fetchMembers();
    }, [accessLevel]);
    const fetchMembers = async () => {
        setLoading(true);
        try {
            console.log(user);
            // Logic: Admin marks Teachers | Teacher marks Students
            const endpoint = accessLevel === 'Admin' 
                ? '/api/fetchAllTeachers' 
                : `/api/fetchAllStudents/${user.classId}`;
            
            const response = await api.get(endpoint);
            setMembers(response.data);
            console.log("Fetched members for attendance:", response.data);
            // Initialize attendance states as 'Present' by default
            const initialData = {};
            response.data.forEach(m => {
                initialData[m.id] = { status: 'Present', remarks: '' };
            });
            setAttendanceData(initialData);
        } catch (error) {
            toast.error("Failed to load members list");
        } finally {
            setLoading(false);
        }
    };

    // Filter members by search term
    const filteredMembers = useMemo(() => {
        return members.filter(m => 
            m.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [members, searchTerm]);

    const handleStatusChange = (id, status) => {
        setAttendanceData(prev => ({
            ...prev,
            [id]: { ...prev[id], status }
        }));
    };

    const handleRemarkChange = (id, remarks) => {
        setAttendanceData(prev => ({
            ...prev,
            [id]: { ...prev[id], remarks }
        }));
    };

    const submitAttendance = async () => {
        const payload = members.map(m => ({
            id: m.id,
            status: attendanceData[m.id].status,
            remarks: attendanceData[m.id].remarks,
            date: selectedDate,
            markedBy: user.id,
            classId: user.classId || null
        }));

        const endpoint = accessLevel === 'Admin'
            ? '/api/markTeacherAttendance'
            : '/api/markStudentAttendance';

        console.log("Submitting attendance with payload:", payload);
        const promise = api.post(endpoint, payload);

        toast.promise(promise, {
            loading: 'Saving attendance...',
            success: 'Attendance marked successfully!',
            error:  "Failed to save attendance. Please try again."
         
        });
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />
            
            <div className="flex-1 p-8 overflow-y-auto">
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">Mark Attendance</h1>
                        <p className="text-gray-500">
                            {accessLevel === 'Admin' ? 'Marking Teachers' : 'Marking Students'}
                        </p>
                    </div>

                    <div className="flex flex-wrap items-center gap-4">
                        <input 
                            type="date" 
                            value={selectedDate}
                            onChange={(e) => setSelectedDate(e.target.value)}
                            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />
                        <div className="relative">
                            <HiOutlineSearch className="absolute left-3 top-3 text-gray-400" size={20} />
                            <input 
                                type="text"
                                placeholder="Search by name..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none w-64"
                            />
                        </div>
                        <button 
                            onClick={submitAttendance}
                            className="bg-blue-600 text-white px-6 py-2 rounded-lg font-bold hover:bg-blue-700 transition shadow-md"
                        >
                            Save Attendance
                        </button>
                    </div>
                </div>

                <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                    <table className="w-full text-left">
                        <thead className="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Member ID</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Member Name</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase text-center">Status</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Remarks (Optional)</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                            {loading ? (
                                <tr><td colSpan="3" className="text-center py-10">Loading list...</td></tr>
                            ) : filteredMembers.map((m) => (
                                <tr key={m.id} className="hover:bg-gray-50 transition">
                                    <td className="px-6 py-4">
                                        <div className="flex items-center space-x-3">
                                            <span className="font-medium text-gray-900">{m.id}</span>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex items-center space-x-3">
                                            <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold">
                                                {m.name.charAt(0)}
                                            </div>
                                            <span className="font-medium text-gray-900">{m.name}</span>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex justify-center items-center space-x-2">
                                            {/* Present */}
                                            <button 
                                                onClick={() => handleStatusChange(m.id, 'Present')}
                                                className={`p-2 rounded-lg flex items-center space-x-1 border transition ${attendanceData[m.id]?.status === 'Present' ? 'bg-green-100 border-green-500 text-green-700' : 'bg-white border-gray-200 text-gray-400'}`}
                                            >
                                                <HiCheckCircle size={18} />
                                                <span className="text-xs font-bold uppercase">Present</span>
                                            </button>
                                            
                                            {/* Absent */}
                                            <button 
                                                onClick={() => handleStatusChange(m.id, 'Absent')}
                                                className={`p-2 rounded-lg flex items-center space-x-1 border transition ${attendanceData[m.id]?.status === 'Absent' ? 'bg-red-100 border-red-500 text-red-700' : 'bg-white border-gray-200 text-gray-400'}`}
                                            >
                                                <HiXCircle size={18} />
                                                <span className="text-xs font-bold uppercase">Absent</span>
                                            </button>

                                            {/* Late */}
                                            <button 
                                                onClick={() => handleStatusChange(m.id, 'Late')}
                                                className={`p-2 rounded-lg flex items-center space-x-1 border transition ${attendanceData[m.id]?.status === 'Late' ? 'bg-yellow-100 border-yellow-500 text-yellow-700' : 'bg-white border-gray-200 text-gray-400'}`}
                                            >
                                                <HiClock size={18} />
                                                <span className="text-xs font-bold uppercase">Late</span>
                                            </button>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4">
                                        {(attendanceData[m.id]?.status === 'Absent' || attendanceData[m.id]?.status === 'Late') && (
                                            <input 
                                                type="text"
                                                placeholder="Enter reason..."
                                                value={attendanceData[m.id]?.remarks}
                                                onChange={(e) => handleRemarkChange(m.id, e.target.value)}
                                                className="w-full px-3 py-1.5 border border-gray-300 rounded-md text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                            />
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default MarkAttendance;