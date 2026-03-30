import React, { useState, useEffect } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineHome, 
    HiOutlineBookOpen, 
    HiOutlinePlus, 
    HiOutlineTrash, 
    HiOutlineHashtag, 
    HiOutlineUsers,
    HiOutlineCalendar,
    HiOutlineIdentification
} from 'react-icons/hi';
import api from "../api";

const ManageResources = () => {
    const [activeTab, setActiveTab] = useState('classrooms'); // 'classrooms' or 'subjects'
    const [loading, setLoading] = useState(false);
    
    // Data Lists
    const [classrooms, setClassrooms] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [teachers, setTeachers] = useState([]);

    // Form States
    const [classFormData, setClassFormData] = useState({
        className: '', section: '', capacity: '', academicYear: '2023-24'
    });
    const [subjectFormData, setSubjectFormData] = useState({
        subjectName: '', subjectCode: ''
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [resClass, resSub, resTeacher] = await Promise.all([
                api.get('/api/classrooms'),
                api.get('/api/subjects'),
                api.get('/api/teachers')
            ]);
            setClassrooms(resClass.data);
            setSubjects(resSub.data);
            setTeachers(resTeacher.data);
        } catch (err) {
            toast.error("Failed to load data");
        }
    };

    const handleAddClassroom = async (e) => {
        e.preventDefault();
        setLoading(true);
        console.log("Submitting classroom data:", classFormData);
        const promise = api.post('/api/addClassRoom', classFormData);
        toast.promise(promise, {
            loading: 'Adding classroom...',
            success: (res) => {
                setClassrooms([...classrooms, res.data]);
                setClassFormData({ className: '', section: '', capacity: '', academicYear: '2023-24' });
                return 'Classroom added successfully!';
            },
            error: 'Failed to add classroom'
        });
        setLoading(false);
    };

    const handleAddSubject = async (e) => {
        e.preventDefault();
        setLoading(true);
        const promise = api.post('/api/addSubject', subjectFormData);
        toast.promise(promise, {
            loading: 'Adding subject...',
            success: (res) => {
                setSubjects([...subjects, res.data]);
                setSubjectFormData({ subjectName: '', subjectCode: '' });
                return 'Subject added successfully!';
            },
            error: 'Failed to add subject'
        });
        setLoading(false);
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                {/* Header */}
                <div className="mb-8">
                    <h1 className="text-2xl font-bold text-gray-800">Academic Resources</h1>
                    <p className="text-gray-500">Manage school classrooms and curriculum subjects</p>
                </div>

                {/* Tab Switcher */}
                <div className="flex space-x-4 mb-8 bg-gray-200 p-1 rounded-xl w-fit">
                    <button 
                        onClick={() => setActiveTab('classrooms')}
                        className={`flex items-center space-x-2 px-6 py-2 rounded-lg font-semibold transition ${activeTab === 'classrooms' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-500 hover:text-gray-700'}`}
                    >
                        <HiOutlineHome size={20} /> <span>Classrooms</span>
                    </button>
                    <button 
                        onClick={() => setActiveTab('subjects')}
                        className={`flex items-center space-x-2 px-6 py-2 rounded-lg font-semibold transition ${activeTab === 'subjects' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-500 hover:text-gray-700'}`}
                    >
                        <HiOutlineBookOpen size={20} /> <span>Subjects</span>
                    </button>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    
                    {/* LEFT COLUMN: ADD FORM */}
                    <div className="lg:col-span-1">
                        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200 sticky top-8">
                            <h2 className="text-lg font-bold text-gray-800 mb-6 flex items-center gap-2">
                                <HiOutlinePlus className="text-blue-600" />
                                {activeTab === 'classrooms' ? 'New Classroom' : 'New Subject'}
                            </h2>

                            {activeTab === 'classrooms' ? (
                                <form onSubmit={handleAddClassroom} className="space-y-4">
                                    <div>
                                        <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Class Name</label>
                                        <div className="relative">
                                            <HiOutlineIdentification className="absolute left-3 top-3 text-gray-400" />
                                            <input type="text" placeholder="e.g. Grade 10" required value={classFormData.className} onChange={(e) => setClassFormData({...classFormData, className: e.target.value})} className="w-full pl-10 pr-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition" />
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Class Advisor</label>
                                        <div className="relative">
                                            <HiOutlineUsers className="absolute left-3 top-3 text-gray-400" />
                                            <select value={classFormData.teacherId} onChange={(e) => setClassFormData({...classFormData, teacherId: e.target.value})} className="w-full pl-10 pr-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition">
                                                <option value="">Select Teacher</option>
                                                {teachers.map((teacher) => (
                                                    console.log("Teacher option:", teacher.id),
                                                    <option key={teacher.id} value={teacher.id}>
                                                        {teacher.name}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>

                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Section</label>
                                            <input type="text" placeholder="A, B, C" required value={classFormData.section} onChange={(e) => setClassFormData({...classFormData, section: e.target.value})} className="w-full px-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition" />
                                        </div>
                                        <div>
                                            <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Capacity</label>
                                            <div className="relative">
                                                <HiOutlineUsers className="absolute left-3 top-3 text-gray-400" />
                                                <input type="number" placeholder="40" required value={classFormData.capacity} onChange={(e) => setClassFormData({...classFormData, capacity: e.target.value})} className="w-full pl-10 pr-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition" />
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Academic Year</label>
                                        <div className="relative">
                                            <HiOutlineCalendar className="absolute left-3 top-3 text-gray-400" />
                                            <input type="text" placeholder="2023-24" value={classFormData.academicYear} onChange={(e) => setClassFormData({...classFormData, academicYear: e.target.value})} className="w-full pl-10 pr-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition" />
                                        </div>
                                    </div>
                                    <button disabled={loading} className="w-full bg-blue-600 text-white py-2 rounded-lg font-bold hover:bg-blue-700 transition shadow-md">Add Classroom</button>
                                </form>
                            ) : (
                                <form onSubmit={handleAddSubject} className="space-y-4">
                                    <div>
                                        <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Subject Name</label>
                                        <div className="relative">
                                            <HiOutlineBookOpen className="absolute left-3 top-3 text-gray-400" />
                                            <input type="text" placeholder="Mathematics" required value={subjectFormData.subjectName} onChange={(e) => setSubjectFormData({...subjectFormData, subjectName: e.target.value})} className="w-full pl-10 pr-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition" />
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-xs font-semibold text-gray-500 uppercase mb-1">Subject Code</label>
                                        <div className="relative">
                                            <HiOutlineHashtag className="absolute left-3 top-3 text-gray-400" />
                                            <input type="text" placeholder="MATH101" required value={subjectFormData.subjectCode} onChange={(e) => setSubjectFormData({...subjectFormData, subjectCode: e.target.value})} className="w-full pl-10 pr-4 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition" />
                                        </div>
                                    </div>
                                    <button disabled={loading} className="w-full bg-blue-600 text-white py-2 rounded-lg font-bold hover:bg-blue-700 transition shadow-md">Add Subject</button>
                                </form>
                            )}
                        </div>
                    </div>

                    {/* RIGHT COLUMN: LIST VIEW */}
                    <div className="lg:col-span-2">
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                            <table className="w-full text-left">
                                <thead className="bg-gray-50 border-b border-gray-100">
                                    {activeTab === 'classrooms' ? (
                                        <tr>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase">Class & Section</th>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase text-center">Capacity</th>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase text-center">Year</th>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase text-right">Action</th>
                                        </tr>
                                    ) : (
                                        <tr>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase">Subject Name</th>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase">Code</th>
                                            <th className="p-4 text-xs font-bold text-gray-400 uppercase text-right">Action</th>
                                        </tr>
                                    )}
                                </thead>
                                <tbody className="divide-y divide-gray-50">
                                    {activeTab === 'classrooms' ? (
                                        classrooms.map((cls) => (
                                            <tr key={cls.classId} className="hover:bg-gray-50 transition">
                                                <td className="p-4">
                                                    <div className="font-bold text-gray-700">{cls.className}</div>
                                                    <div className="text-xs text-blue-500 font-medium">Section {cls.section}</div>
                                                </td>
                                                <td className="p-4 text-center text-gray-600 font-medium">{cls.capacity}</td>
                                                <td className="p-4 text-center text-gray-400 text-sm">{cls.academicYear}</td>
                                                <td className="p-4 text-right">
                                                    <button className="text-red-400 hover:text-red-600 transition p-2"><HiOutlineTrash size={18} /></button>
                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        subjects.map((sub) => (
                                            <tr key={sub.subjectId} className="hover:bg-gray-50 transition">
                                                <td className="p-4 font-bold text-gray-700">{sub.subjectName}</td>
                                                <td className="p-4"><span className="bg-gray-100 px-2 py-1 rounded text-xs font-mono font-bold text-gray-600">{sub.subjectCode}</span></td>
                                                <td className="p-4 text-right">
                                                    <button className="text-red-400 hover:text-red-600 transition p-2"><HiOutlineTrash size={18} /></button>
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                    {(activeTab === 'classrooms' ? classrooms : subjects).length === 0 && (
                                        <tr>
                                            <td colSpan="4" className="p-10 text-center text-gray-400 italic">No records found. Add your first {activeTab.slice(0, -1)}!</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ManageResources;