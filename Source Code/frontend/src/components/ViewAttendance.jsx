import React, { useEffect, useState, useMemo } from 'react';
import Sidebar from './Sidebar';
import { toast, Toaster } from 'sonner';
import { HiOutlineSearch, HiOutlineCalendar, HiUserGroup, HiCheckCircle, HiXCircle, HiClock } from 'react-icons/hi';
import api from './api';

const ViewAttendance = () => {
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);

    // Get user info from localStorage
    const user = JSON.parse(localStorage.getItem('userData')) || { role: 'Admin', id: '1' };
    const accessLevel = localStorage.getItem('accessLevel');

    useEffect(() => {
        fetchAttendance();
    }, [selectedDate, accessLevel]);

    const fetchAttendance = async () => {
        setLoading(true);
        try {
            // Logic: 
            // Admin views Teacher attendance or all Student attendance
            // Teacher views their Class attendance
            const endpoint = accessLevel === 'Admin' 
                ? `/api/attendance/date/${selectedDate}` 
                : accessLevel === 'Teacher' 
                ? `/api/attendance/class/${user.classId}/date/${selectedDate}`
                : `/api/attendance/member/${user.id}/date/${selectedDate}`;

            const response = await api.get(endpoint);
            setRecords(response.data);
        } catch (error) {
            console.error("Error fetching attendance:", error);
            toast.error("No records found for the selected date");
            setRecords([]); // Clear records on error or 404
        } finally {
            setLoading(false);
        }
    };

    const filteredRecords = useMemo(() => {
        return records.filter(record => 
            record.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [records, searchTerm]);

    // Calculate Stats
    const stats = useMemo(() => {
        const total = filteredRecords.length;
        const present = filteredRecords.filter(r => r.status === 'Present').length;
        const absent = filteredRecords.filter(r => r.status === 'Absent').length;
        const late = filteredRecords.filter(r => r.status === 'Late').length;
        return { total, present, absent, late };
    }, [filteredRecords]);

    const getStatusBadge = (status) => {
        switch (status) {
            case 'Present':
                return <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-xs font-medium flex items-center w-fit"><HiCheckCircle className="mr-1"/> Present</span>;
            case 'Absent':
                return <span className="bg-red-100 text-red-700 px-3 py-1 rounded-full text-xs font-medium flex items-center w-fit"><HiXCircle className="mr-1"/> Absent</span>;
            case 'Late':
                return <span className="bg-yellow-100 text-yellow-700 px-3 py-1 rounded-full text-xs font-medium flex items-center w-fit"><HiClock className="mr-1"/> Late</span>;
            default:
                return <span className="bg-gray-100 text-gray-700 px-3 py-1 rounded-full text-xs font-medium">N/A</span>;
        }
    };

    return (
        <div className="flex h-screen bg-gray-50 text-gray-800">
            <Sidebar />
            <Toaster richColors position="top-right" />
            
            <div className="flex-1 p-8 overflow-y-auto">
                {/* Header Section */}
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
                    <div>
                        <h1 className="text-2xl font-bold flex items-center gap-2">
                            <HiOutlineCalendar className="text-blue-600" /> Attendance Records
                        </h1>
                        <p className="text-gray-500 text-sm">
                            Showing {accessLevel === 'Admin' ? 'Staff & Teachers' : `Students of Class ${user.classId}`}
                        </p>
                    </div>

                    <div className="flex flex-wrap items-center gap-4">
                        <div className="relative">
                            <input 
                                type="date" 
                                value={selectedDate}
                                onChange={(e) => setSelectedDate(e.target.value)}
                                className="pl-4 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white shadow-sm"
                            />
                        </div>
                        <div className="relative">
                            <HiOutlineSearch className="absolute left-3 top-3 text-gray-400" size={18} />
                            <input 
                                type="text"
                                placeholder="Search member name..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none w-64 shadow-sm"
                            />
                        </div>
                    </div>
                </div>

                {/* Stats Summary Cards */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
                    <StatCard title="Total" value={stats.total} icon={<HiUserGroup />} color="blue" />
                    <StatCard title="Present" value={stats.present} icon={<HiCheckCircle />} color="green" />
                    <StatCard title="Absent" value={stats.absent} icon={<HiXCircle />} color="red" />
                    <StatCard title="Late" value={stats.late} icon={<HiClock />} color="yellow" />
                </div>

                {/* Data Table */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                    <table className="w-full text-left">
                        <thead className="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Member ID</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Member Name</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Status</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Remarks</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                            {loading ? (
                                <tr><td colSpan="4" className="text-center py-20 text-gray-400">Loading attendance data...</td></tr>
                            ) : filteredRecords.length === 0 ? (
                                <tr><td colSpan="4" className="text-center py-20 text-gray-400">No records found for this date.</td></tr>
                            ) : filteredRecords.map((record) => (
                                <tr key={record.id} className="hover:bg-gray-50 transition">
                                    <td className="px-6 py-4 text-sm text-gray-500">
                                        {record.id}
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex items-center space-x-3">
                                            <div className="w-9 h-9 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-700 font-bold text-sm">
                                                {record.name.charAt(0)}
                                            </div>
                                            <span className="font-medium text-gray-900">{record.name}</span>
                                        </div>
                                    </td>
                                    
                                    <td className="px-6 py-4">
                                        {getStatusBadge(record.status)}
                                    </td>
                                    <td className="px-6 py-4 text-sm text-gray-600 italic">
                                        {record.remarks || <span className="text-gray-300">No remarks</span>}
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

// Sub-component for Stats Cards
const StatCard = ({ title, value, icon, color }) => {
    const colors = {
        blue: 'bg-blue-50 text-blue-600 border-blue-100',
        green: 'bg-green-50 text-green-600 border-green-100',
        red: 'bg-red-50 text-red-600 border-red-100',
        yellow: 'bg-yellow-50 text-yellow-600 border-yellow-100',
    };

    return (
        <div className={`p-5 rounded-xl border ${colors[color]} flex items-center justify-between shadow-sm`}>
            <div>
                <p className="text-sm font-medium opacity-80 uppercase">{title}</p>
                <p className="text-2xl font-bold mt-1">{value}</p>
            </div>
            <div className="text-3xl opacity-40">
                {icon}
            </div>
        </div>
    );
};

export default ViewAttendance;