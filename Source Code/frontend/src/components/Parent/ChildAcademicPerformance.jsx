import React, { useEffect, useState, useMemo } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineAcademicCap, 
    HiCheckCircle, 
    HiXCircle, 
    HiTrendingUp, 
    HiBookOpen,
    HiChartBar
} from 'react-icons/hi';
import api from '../api';

const ChildAcademicPerformance = () => {
    const [grades, setGrades] = useState([]);
    const [loading, setLoading] = useState(true);

    const user = JSON.parse(localStorage.getItem('userData')) || { name: 'User', id: '1' };
    const userId = localStorage.getItem('studentId');
    const accessLevel = localStorage.getItem('accessLevel');

    console.log(user);

    useEffect(() => {
        fetchMyGrades();
    }, []);

    const fetchMyGrades = async () => {
        setLoading(true);
        try {
           
            const response = await api.get(`/api/exams-management/results/${userId}`);
            console.log(response.data);
            setGrades(response.data);
        } catch (error) {
            console.error("Error fetching grades:", error);
            toast.error("Failed to load your academic records");
            setGrades([]);
        } finally {
            setLoading(false);
        }
    };

    // Calculate Academic Stats
    const stats = useMemo(() => {
        const totalSubjects = grades.length;
        const totalObtained = grades.reduce((acc, curr) => acc + curr.marksObtained, 0);
        const totalMax = grades.reduce((acc, curr) => acc + curr.totalMarks, 0);
        
        const percentage = totalMax > 0 ? ((totalObtained / totalMax) * 100).toFixed(1) : 0;
        const passed = grades.filter(g => g.gradeLetter !== 'F').length;
        const failed = grades.filter(g => g.gradeLetter === 'F').length;

        return { totalSubjects, percentage, passed, failed, totalObtained };
    }, [grades]);

    const getGradeBadge = (grade) => {
        const baseClass = "px-3 py-1 rounded-full text-xs font-medium flex items-center w-fit";
        switch (grade) {
            case 'A':
            case 'A+':
                return <span className={`bg-green-100 text-green-700 ${baseClass}`}><HiCheckCircle className="mr-1"/> Excellent ({grade})</span>;
            case 'B':
            case 'C':
                return <span className={`bg-blue-100 text-blue-700 ${baseClass}`}><HiTrendingUp className="mr-1"/> Good ({grade})</span>;
            case 'D':
                return <span className={`bg-yellow-100 text-yellow-700 ${baseClass}`}><HiChartBar className="mr-1"/> Average ({grade})</span>;
            case 'F':
                return <span className={`bg-red-100 text-red-700 ${baseClass}`}><HiXCircle className="mr-1"/> Failed ({grade})</span>;
            default:
                return <span className={`bg-gray-100 text-gray-700 ${baseClass}`}>{grade}</span>;
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
                        <HiOutlineAcademicCap className="text-blue-600" /> Academic Performance
                    </h1>
                    <p className="text-gray-500 text-sm mt-1">
                        Viewing academic report for <span className="font-semibold text-blue-600">{user.name}</span> (ID: {user.id})
                    </p>
                </div>

                {/* Performance Stats Summary */}
                <div className="grid grid-cols-1 md:grid-cols-5 gap-4 mb-8">
                    <StatCard title="Overall Score" value={`${stats.percentage}%`} icon={<HiChartBar />} color="indigo" />
                    <StatCard title="Total Subjects" value={stats.totalSubjects} icon={<HiBookOpen />} color="blue" />
                    <StatCard title="Passed" value={stats.passed} icon={<HiCheckCircle />} color="green" />
                    <StatCard title="Failed" value={stats.failed} icon={<HiXCircle />} color="red" />
                    <StatCard title="Total Marks" value={stats.totalObtained} icon={<HiTrendingUp />} color="yellow" />
                </div>

                {/* Grades Table */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                    <div className="px-6 py-4 border-b border-gray-100 bg-gray-50/50 flex justify-between items-center">
                        <h2 className="font-semibold text-gray-700">Subject-wise Result</h2>
                        <span className="text-xs text-gray-400 font-medium uppercase tracking-wider"></span>
                    </div>
                    <table className="w-full text-left">
                        <thead className="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Subject</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase text-center">Marks</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase text-center">Percentage</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Grade Status</th>
                                <th className="px-6 py-4 text-sm font-semibold text-gray-600 uppercase">Remarks</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                            {loading ? (
                                <tr><td colSpan="5" className="text-center py-20 text-gray-400">Loading your grades...</td></tr>
                            ) : grades.length === 0 ? (
                                <tr><td colSpan="5" className="text-center py-20 text-gray-400">No academic records found.</td></tr>
                            ) : grades.map((record, index) => (
                                <tr key={index} className="hover:bg-gray-50 transition">
                                    <td className="px-6 py-4">
                                        <div className="text-sm font-bold text-gray-900">{record.subjectName}</div>
                                        <div className="text-xs text-gray-400">Code: {record.subjectId}</div>
                                    </td>
                                    <td className="px-6 py-4 text-sm font-medium text-gray-700 text-center">
                                        {record.marksObtained} <span className="text-gray-400 font-normal">/ {record.totalMarks}</span>
                                    </td>
                                    <td className="px-6 py-4 text-center">
                                        <div className="w-full bg-gray-100 rounded-full h-1.5 mb-1 max-w-[100px] mx-auto">
                                            <div 
                                                className="bg-blue-500 h-1.5 rounded-full" 
                                                style={{ width: `${(record.marksObtained/record.totalMarks)*100}%` }}
                                            ></div>
                                        </div>
                                        <span className="text-xs font-semibold text-gray-500">
                                            {((record.marksObtained/record.totalMarks)*100).toFixed(1)}%
                                        </span>
                                    </td>
                                    <td className="px-6 py-4">
                                        {getGradeBadge(record.gradeLetter)}
                                    </td>
                                    <td className="px-6 py-4 text-sm text-gray-600 italic">
                                        {record.remarks || 'N/A'}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
                
                {/* Footer Disclaimer */}
                <p className="mt-6 text-xs text-gray-400 italic text-center">
                    * This is an unofficial digital transcript. Please collect the original marksheet from the school office.
                </p>
            </div>
        </div>
    );
};

// StatCard Component (Exact replica of your reference for UI consistency)
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

export default ChildAcademicPerformance;