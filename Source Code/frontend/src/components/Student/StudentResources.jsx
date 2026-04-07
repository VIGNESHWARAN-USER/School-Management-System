import React, { useState, useEffect, useMemo } from 'react';
import Sidebar from '../Sidebar';
import api from '../api';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineSearch, HiOutlineDownload, HiOutlineDocumentText, 
    HiOutlineFilter, HiOutlineInformationCircle, HiOutlineBookOpen 
} from 'react-icons/hi';

const StudentResources = () => {
    const [resources, setResources] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('ALL');

    // Scenario Check: Verify if student is registered
    const studentId = localStorage.getItem('userData') ? JSON.parse(localStorage.getItem('userData')).id : null;
    const isRegistered = !!studentId;

    useEffect(() => {
        if (!isRegistered) {
            toast.error("Warning: Unregistered student. You may not have full access to resources.");
        }
        fetchResources();
    }, [isRegistered]);

    const fetchResources = async () => {
        setLoading(true);
        try {
            const res = await api.get('/api/resources');
            setResources(res.data);
        } catch (err) {
            toast.error("Failed to load resources from server.");
        } finally {
            setLoading(false);
        }
    };

    // Filter and Search Logic
    const filteredResources = useMemo(() => {
        return resources.filter(res => {
            const matchesSearch = res.title.toLowerCase().includes(searchTerm.toLowerCase()) || 
                                 res.description.toLowerCase().includes(searchTerm.toLowerCase());
            const matchesCategory = selectedCategory === 'ALL' || res.category === selectedCategory;
            return matchesSearch && matchesCategory;
        });
    }, [resources, searchTerm, selectedCategory]);

    const handleDownload = (id, fileName) => {
        if (!isRegistered) {
            toast.error("Access Denied: Please register/login to download materials.");
            return;
        }
        // Redirect to backend download endpoint
        window.open(`https://springboot-app-kwal.onrender.com/api/resources/download/${id}`, '_blank');
        toast.success(`Downloading ${fileName}`);
    };

    const categories = [
        { id: 'ALL', label: 'All Resources' },
        { id: 'STUDY_MATERIAL', label: 'Study Materials' },
        { id: 'ASSIGNMENT', label: 'Assignments' },
        { id: 'REFERENCE', label: 'References' },
    ];

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                {/* Header & Stats */}
                <div className="mb-8">
                    <h1 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
                        <HiOutlineBookOpen className="text-blue-600" />
                        Resource Library
                    </h1>
                    <p className="text-sm text-gray-500">Access your study materials, assignments, and reference guides.</p>
                </div>

                {/* Search and Filter Bar */}
                <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200 mb-8 flex flex-col md:flex-row gap-4 justify-between items-center">
                    <div className="relative w-full md:w-96">
                        <HiOutlineSearch className="absolute left-3 top-3 text-gray-400" size={20} />
                        <input 
                            type="text"
                            placeholder="Search by title or description..."
                            className="pl-10 pr-4 py-2 w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className="flex items-center gap-2 overflow-x-auto w-full md:w-auto pb-2 md:pb-0">
                        <HiOutlineFilter className="text-gray-400 hidden md:block" />
                        {categories.map((cat) => (
                            <button
                                key={cat.id}
                                onClick={() => setSelectedCategory(cat.id)}
                                className={`px-4 py-2 rounded-full text-xs font-bold whitespace-nowrap transition ${
                                    selectedCategory === cat.id 
                                    ? 'bg-blue-600 text-white shadow-md' 
                                    : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                                }`}
                            >
                                {cat.label}
                            </button>
                        ))}
                    </div>
                </div>

                {/* Main Content Area */}
                {loading ? (
                    <div className="flex flex-col items-center justify-center py-20">
                        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-blue-600"></div>
                        <p className="mt-4 text-gray-500">Loading your resources...</p>
                    </div>
                ) : filteredResources.length === 0 ? (
                    <div className="bg-white rounded-2xl p-16 text-center border border-dashed border-gray-300">
                        <HiOutlineInformationCircle size={48} className="mx-auto text-gray-300 mb-4" />
                        <h3 className="text-lg font-bold text-gray-600">No Resources Found</h3>
                        <p className="text-gray-400">Try adjusting your search or category filters.</p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {filteredResources.map((res) => (
                            <div key={res.id} className="group bg-white rounded-xl border border-gray-200 shadow-sm hover:shadow-md hover:border-blue-200 transition-all overflow-hidden flex flex-col">
                                <div className="p-5 flex-1">
                                    <div className="flex justify-between items-start mb-4">
                                        <div className={`p-3 rounded-lg ${
                                            res.category === 'ASSIGNMENT' ? 'bg-orange-50 text-orange-600' : 
                                            res.category === 'STUDY_MATERIAL' ? 'bg-blue-50 text-blue-600' : 'bg-purple-50 text-purple-600'
                                        }`}>
                                            <HiOutlineDocumentText size={24} />
                                        </div>
                                        <span className={`text-[10px] font-bold px-2 py-1 rounded-full uppercase ${
                                            res.category === 'ASSIGNMENT' ? 'bg-orange-100 text-orange-600' : 
                                            res.category === 'STUDY_MATERIAL' ? 'bg-blue-100 text-blue-600' : 'bg-purple-100 text-purple-600'
                                        }`}>
                                            {res.category.replace('_', ' ')}
                                        </span>
                                    </div>

                                    <h3 className="font-bold text-gray-800 text-lg mb-2 group-hover:text-blue-600 transition-colors">
                                        {res.title}
                                    </h3>
                                    <p className="text-sm text-gray-500 line-clamp-3 mb-4">
                                        {res.description || "No description provided."}
                                    </p>

                                    <div className="flex items-center text-[11px] text-gray-400 gap-4 mt-auto">
                                        <div className="flex flex-col">
                                            <span className="font-semibold text-gray-500 uppercase">Uploaded On</span>
                                            <span>{new Date(res.uploadDate).toLocaleDateString()}</span>
                                        </div>
                                        <div className="flex flex-col">
                                            <span className="font-semibold text-gray-500 uppercase">Format</span>
                                            <span className="uppercase">{res.fileType.split('/')[1]}</span>
                                        </div>
                                    </div>
                                </div>

                                <button 
                                    onClick={() => handleDownload(res.id, res.fileName)}
                                    className="w-full bg-gray-50 group-hover:bg-blue-600 group-hover:text-white border-t border-gray-100 py-3 flex items-center justify-center gap-2 text-sm font-bold text-gray-600 transition-all"
                                >
                                    <HiOutlineDownload size={18} />
                                    Download Resource
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default StudentResources;