import React, { useState, useEffect } from 'react';
import Sidebar from '../Sidebar';
import api from '../api';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineCloudUpload, HiOutlineDocumentText, HiOutlineFolderOpen, 
    HiOutlinePlus, HiOutlineX, HiOutlineTrash, HiOutlineDownload 
} from 'react-icons/hi';

const TeacherResources = () => {
    const [resources, setResources] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showUploadForm, setShowUploadForm] = useState(false);
    const [isUploading, setIsUploading] = useState(false);

    // Form State
    const [file, setFile] = useState(null);
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        category: 'STUDY_MATERIAL'
    });

    // Mock Teacher ID (In real app, get from Auth Context or LocalStorage)
    const userData = localStorage.getItem('userData'); 
    const teacherId = userData ? JSON.parse(userData).id : null;
    const isRegistered = !!teacherId;

    useEffect(() => {
        fetchResources();
    }, []);

    const fetchResources = async () => {
        try {
            const res = await api.get('/api/resources');
            setResources(res.data);
        } catch (err) {
            toast.error("Failed to load resources");
        } finally {
            setLoading(false);
        }
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUpload = async (e) => {
        e.preventDefault();

        // Scenario 2: Verify unregistered teacher
        if (!isRegistered) {
            toast.error("Warning: Unregistered users cannot upload materials. Please login as a teacher.");
            return;
        }

        if (!file) {
            toast.error("Please select a file to upload");
            return;
        }

        const data = new FormData();
        data.append('file', file);
        data.append('title', formData.title);
        data.append('description', formData.description);
        data.append('category', formData.category);
        data.append('teacherId', teacherId);

        setIsUploading(true);
        try {
            await api.post('/api/resources/upload', data, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            toast.success("Resource shared successfully!");
            setShowUploadForm(false);
            setFormData({ title: '', description: '', category: 'STUDY_MATERIAL' });
            setFile(null);
            fetchResources(); // Refresh list
        } catch (err) {
            toast.error("Upload failed. Try again.");
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                {/* Header Section */}
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">Educational Resources</h1>
                        <p className="text-sm text-gray-500">Manage study materials and assignments for your students</p>
                    </div>
                    
                    <button 
                        onClick={() => setShowUploadForm(!showUploadForm)}
                        className={`flex items-center gap-2 px-4 py-2 rounded-lg transition font-medium ${
                            showUploadForm ? 'bg-gray-200 text-gray-700' : 'bg-blue-600 text-white hover:bg-blue-700'
                        }`}
                    >
                        {showUploadForm ? <HiOutlineX /> : <HiOutlinePlus />}
                        {showUploadForm ? "Cancel" : "Upload Resource"}
                    </button>
                </div>

                {/* Upload Form - Logic for Scenario 1 (Registered Teacher) */}
                {showUploadForm && (
                    <div className="bg-white p-6 rounded-xl border border-blue-100 shadow-sm mb-8 animate-in fade-in slide-in-from-top-4">
                        <h3 className="text-lg font-bold text-gray-700 mb-4 flex items-center gap-2">
                            <HiOutlineCloudUpload className="text-blue-600" />
                            Share New Material
                        </h3>
                        <form onSubmit={handleUpload} className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-600">Title</label>
                                    <input 
                                        type="text" 
                                        required
                                        className="w-full mt-1 p-2 border border-gray-300 rounded-md outline-none focus:ring-2 focus:ring-blue-500"
                                        value={formData.title}
                                        onChange={(e) => setFormData({...formData, title: e.target.value})}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-600">Category</label>
                                    <select 
                                        className="w-full mt-1 p-2 border border-gray-300 rounded-md outline-none focus:ring-2 focus:ring-blue-500"
                                        value={formData.category}
                                        onChange={(e) => setFormData({...formData, category: e.target.value})}
                                    >
                                        <option value="STUDY_MATERIAL">Study Material</option>
                                        <option value="ASSIGNMENT">Assignment</option>
                                        <option value="REFERENCE">Reference</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-600">File</label>
                                    <input 
                                        type="file" 
                                        onChange={handleFileChange}
                                        className="w-full mt-1 text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                                    />
                                </div>
                            </div>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-600">Description</label>
                                    <textarea 
                                        rows="5"
                                        className="w-full mt-1 p-2 border border-gray-300 rounded-md outline-none focus:ring-2 focus:ring-blue-500"
                                        value={formData.description}
                                        onChange={(e) => setFormData({...formData, description: e.target.value})}
                                    ></textarea>
                                </div>
                                <button 
                                    disabled={isUploading}
                                    type="submit"
                                    className="w-full bg-blue-600 text-white py-2 rounded-md font-bold hover:bg-blue-700 disabled:bg-blue-300 transition"
                                >
                                    {isUploading ? "Uploading..." : "Share with Students"}
                                </button>
                            </div>
                        </form>
                    </div>
                )}

                {/* Resource List */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {loading ? (
                        <p className="text-gray-500">Loading resources...</p>
                    ) : resources.length === 0 ? (
                        <div className="col-span-full py-20 text-center bg-white rounded-xl border-2 border-dashed">
                            <HiOutlineFolderOpen size={50} className="mx-auto text-gray-300" />
                            <p className="mt-2 text-gray-500 font-medium">No resources shared yet.</p>
                        </div>
                    ) : (
                        resources.map((res) => (
                            <div key={res.id} className="bg-white p-5 rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition">
                                <div className="flex justify-between items-start mb-3">
                                    <div className="p-2 bg-blue-50 text-blue-600 rounded-lg">
                                        <HiOutlineDocumentText size={24} />
                                    </div>
                                    <span className={`text-[10px] font-bold px-2 py-1 rounded-full uppercase ${
                                        res.category === 'ASSIGNMENT' ? 'bg-orange-100 text-orange-600' : 'bg-green-100 text-green-600'
                                    }`}>
                                        {res.category.replace('_', ' ')}
                                    </span>
                                </div>
                                <h3 className="font-bold text-gray-800 line-clamp-1">{res.title}</h3>
                                <p className="text-xs text-gray-500 mt-1 line-clamp-2 min-h-[32px]">{res.description}</p>
                                
                                <div className="mt-4 pt-4 border-t border-gray-50 flex justify-between items-center">
                                    <div className="text-[10px] text-gray-400">
                                        Uploaded: {new Date(res.uploadDate).toLocaleDateString()}
                                    </div>
                                    <div className="flex gap-2">
                                        <a 
                                            href={`http://localhost:8085/api/resources/download/${res.id}`}
                                            className="p-2 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-full transition"
                                            title="Download"
                                        >
                                            <HiOutlineDownload size={18} />
                                        </a>
                                        <button className="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-full transition">
                                            <HiOutlineTrash size={18} />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default TeacherResources;