import React, { useEffect, useState, useMemo } from 'react';
import Sidebar from './Sidebar';
import { toast, Toaster } from 'sonner';
import { HiOutlineCalendar, HiCheckCircle, HiXCircle, HiClock, HiChartPie } from 'react-icons/hi';
import api from './api';

const MyAttendance = () => {
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);

    // Get user info from localStorage
    const user = JSON.parse(localStorage.getItem('userData')) || { name: 'User', id: '1' };
    const accessLevel = localStorage.getItem('accessLevel');
    console.log("User data loaded:", user, "Access level:", accessLevel);
    useEffect(() => {
        fetchMyAttendance();
    }, []);

    const fetchMyAttendance = async () => {
        setLoading(true);
        try {
            // Endpoint changed to fetch all historical records for this specific user ID
            const response = await api.get(`/api/attendance/${accessLevel}/${user.id}`);
            // Ensure records are sorted by date (newest first)
            const sortedData = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
            setRecords(sortedData);
        } catch (error) {
            console.error("Error fetching attendance:", error);
            toast.error("Failed to load your attendance history");
            setRecords([]);
        } finally {
            setLoading(false);
        }
    };

    const stats = useMemo(() => {
        const total = records.length;
        const present = records.filter(r => r.status === 'Present').length;
        const absent = records.filter(r => r.status === 'Absent').length;
        const late = records.filter(r => r.status === 'Late').length;
        const percentage = total > 0 ? ((present / total) * 100).toFixed(1) : 0;
        return { total, present, absent, late, percentage };
    }, [records]);

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
                <div className="mb-8">
                    <h1 className="text-2xl font-bold flex items-center gap-2">
                        <HiOutlineCalendar className="text-blue-600" /> My Attendance History
                    </h1>
                    <p className="text-gray-500 text-sm mt-1">
                        Viewing complete attendance logs for <span className="font-semibold text-blue-600">{user.name}</span> (ID: {user.id})
                    </p>
                </div>

                {/* Personal Stats Summary */}
                <div className="grid grid-cols-1 md:grid-cols-5 gap-4 mb-8">
                    <StatCard title="Attendance Rate" value={`${stats.percentage}%`} icon={<HiChartPie />} color="indigo" />
                    <StatCard title="Total Days" value={stats.total} icon={<HiOutlineCalendar />} color="blue" />
                    <StatCard title="Present" value={stats.present} icon={<HiCheckCircle />} color="green" />
                    <StatCard title="Absent" value={stats.absent} icon={<HiXCircle />} color="red" />
                    <StatCard title="Late" value={stats.late} icon={<HiClock />} color="yellow" />
                </div>

                {/* History Table */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                    <div className="px-6 py-4 border-b border-gray-100 bg-gray-50/50">
                        <h2 className="font-semibold text-gray-700">Detailed Logs</h2>
                    </div>
                    <table className="w-full text-left">
                        <thead className="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase text-center">Date</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Day</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Status</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Remarks/Notes</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                            {loading ? (
                                <tr><td colSpan="4" className="text-center py-20 text-gray-400">Loading your history...</td></tr>
                            ) : records.length === 0 ? (
                                <tr><td colSpan="4" className="text-center py-20 text-gray-400">No attendance records found.</td></tr>
                            ) : records.map((record, index) => (
                                <tr key={index} className="hover:bg-gray-50 transition">
                                    <td className="px-6 py-4 text-sm font-medium text-gray-900 text-center">
                                        {new Date(record.date).toLocaleDateString('en-GB', {
                                            day: '2-digit',
                                            month: 'short',
                                            year: 'numeric'
                                        })}
                                    </td>
                                    <td className="px-6 py-4 text-sm text-gray-500">
                                        {new Date(record.date).toLocaleDateString('en-US', { weekday: 'long' })}
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

const StatCard = ({ title, value, icon, color }) => {
    const colors = {
        blue: 'bg-blue-50 text-blue-600 border-blue-100',
        green: 'bg-green-50 text-green-600 border-green-100',
        red: 'bg-red-50 text-red-600 border-red-100',
        yellow: 'bg-yellow-50 text-yellow-600 border-yellow-100',
        indigo: 'bg-indigo-50 text-indigo-600 border-indigo-100',
    };

    return (
        <div className={`p-5 rounded-xl border ${colors[color]} flex items-center justify-between shadow-sm`}>
            <div>
                <p className="text-xs font-bold opacity-80 uppercase tracking-wider">{title}</p>
                <p className="text-2xl font-black mt-1">{value}</p>
            </div>
            <div className="text-3xl opacity-30">
                {icon}
            </div>
        </div>
    );
};

export default MyAttendance;