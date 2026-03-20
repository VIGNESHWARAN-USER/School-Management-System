import React, { useEffect, useMemo, useState } from 'react';
import { FaPlus } from "react-icons/fa";
import { HiOutlinePencilAlt, HiOutlineTrash, HiOutlineEye } from 'react-icons/hi';
import Modal from 'react-modal';
import { toast } from 'sonner';
import userimg from "../../assets/user.jpg";
import Sidebar from '../Sidebar';
import Select from 'react-select';
import api from '../../../../../../Employee-Management-Portal/frontend/src/api';
import { useNavigate } from 'react-router-dom';

Modal.setAppElement('#root');

const AddMembers = () => {
    const [userData, setUserData] = useState([]); // Combined list of Students, Teachers, Parents
    const [parents, setParents] = useState([]); // For Student -> Parent assignment
    
    const [searchTerm, setSearchTerm] = useState('');
    const [roleFilter, setRoleFilter] = useState('All');

    const [addModalIsOpen, setAddModalIsOpen] = useState(false);
    const [editModalIsOpen, setEditModalIsOpen] = useState(false);
    const [viewModalIsOpen, setViewModalIsOpen] = useState(false);

    // Dynamic state to handle all three types
    const [memberType, setMemberType] = useState('Student'); 
    const [formData, setFormData] = useState({
        name: '', email: '', password: '', age: '', 
        classId: '',  parentId: '', address: '', // Student specific
        subject: '', phoneNumber: '', // Teacher specific
        mobileNumber: '', // Parent specific
        studentIds: [], 
    });

    const navigate = useNavigate();
    const [editingMember, setEditingMember] = useState(null);
    const [viewingMember, setViewingMember] = useState(null);

    // --- Data Fetching ---
    const fetchDetails = async () => {
        try {
            const [studRes, teachRes, parentRes] = await Promise.all([
                api.get("http://localhost:8085/api/fetchAllStudents"),
                api.get("http://localhost:8085/api/fetchAllTeachers"),
                api.get("http://localhost:8085/api/fetchAllParents")
            ]);
            console.log("Students:", studRes.data);
            console.log("Teachers:", teachRes.data);
            console.log("Parents:", parentRes.data);

            // Add a "type" property to distinguish them in the table
            const students = studRes.data.map(s => ({ ...s, type: 'Student' }));
            const teachers = teachRes.data.map(t => ({ ...t, type: 'Teacher' }));
            const parentsData = parentRes.data.map(p => ({ ...p, type: 'Parent' }));

            setUserData([...students, ...teachers, ...parentsData]);
            setParents(parentsData);
        } catch (error) {
            console.error("Error fetching member details:", error);
        }
    };

    useEffect(() => { 
        fetchDetails();
    }, []);

    // --- Filtering Logic ---
    const filteredMembers = useMemo(() => {
        return userData
            .filter(m => (m.name || "").toLowerCase().includes(searchTerm.toLowerCase()))
            .filter(m => (roleFilter === 'All' ? true : m.type === roleFilter));
    }, [userData, searchTerm, roleFilter]);

    // --- Handlers ---
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleMultiSelectChange = (selectedOptions) => {
    // selectedOptions is an array of { value, label }
    const ids = selectedOptions ? selectedOptions.map(option => option.value) : [];
    setFormData(prev => ({ ...prev, studentIds: ids }));
};

    const openAddModal = () => {
        setFormData({ name: '', email: '', password: '', age: '', classId: '',  parentId: '', address: '', subject: '', phoneNumber: '', mobileNumber: '' });
        setAddModalIsOpen(true);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        
        let endpoint = "";
        if (memberType === 'Student') endpoint = "addStudent";
        else if (memberType === 'Teacher') endpoint = "addTeacher";
        else endpoint = "addParent";

        const promise = api.post(`http://localhost:8085/api/${endpoint}`, formData);

        toast.promise(promise, {
            loading: `Adding ${memberType}...`,
            success: () => {
                fetchDetails();
                setAddModalIsOpen(false);
                return `${memberType} added successfully`;
            },
            error: (err) => {
                return "Session Expired.";
            }
        });
    };

    
const InfoBlock = ({ label, value }) => (
    <div className='bg-gray-50 p-3 rounded-lg border border-gray-100'>
        <p className="text-sm font-medium text-gray-500">{label}</p>
        <p className="text-md font-semibold text-gray-900">{value || 'N/A'}</p>
    </div>
);

    const handleDelete = (id, type) => {
        console.log(`Attempting to delete ${type} with ID:`, id);
    toast.warning(`Are you sure you want to delete this ${type}?`, {
        description: "This action cannot be undone.",
        duration: 5000, // Give user time to click
        action: {
            label: "Delete",
            onClick: () => {
                const deletePromise = api.delete(`http://localhost:8085/api/delete${type}/${id}`);
                
                toast.promise(deletePromise, {
                    loading: `Deleting ${type}...`,
                    success: () => { 
                        fetchDetails(); 
                        return `${type} deleted successfully`; 
                    },
                    error: (err) => {
                        return `Failed to delete: ${err.message}`;
                    },
                });
            },
        },
        cancel: {
            label: "Cancel",
            onClick: () => console.log("Delete cancelled"),
        },
    });
};

    return (
        <div className="w-full h-screen flex bg-gray-50">
            <Sidebar/>
            <div className="w-full p-6">
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-2xl font-bold text-gray-800">School Members</h1>
                     <input
                        type="text"
                        placeholder="Search by name..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full md:w-1/3 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    />
                    <select
                        value={roleFilter}
                        onChange={(e) => setRoleFilter(e.target.value)}
                        className="px-4 py-2 border border-gray-300 rounded-lg"
                    >
                        <option value="All">All Types</option>
                        <option value="Student">Students</option>
                        <option value="Teacher">Teachers</option>
                        <option value="Parent">Parents</option>
                    </select>
                
                    <button onClick={openAddModal} className="flex items-center bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors shadow-md">
                        <FaPlus className="mr-2" /> Add Member
                    </button>
                </div>

                {/* Table */}
                <div className="bg-white rounded-lg shadow-md overflow-hidden">
                    <table className="min-w-full leading-normal">
                        <thead>
                            <tr className="bg-gray-100 text-left text-gray-600 uppercase text-sm">
                                <th className="px-5 py-3 border-b-2">Member</th>
                                <th className="px-5 py-3 border-b-2">Type</th>
                                <th className="px-5 py-3 border-b-2">Contact/Info</th>
                                <th className="px-5 py-3 border-b-2">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredMembers.map((m) => (
                                <tr key={m.id} className="hover:bg-gray-50">
                                    <td className="px-5 py-5 border-b border-gray-200 text-sm">
                                        <div className="flex items-center">
                                            <img className="w-10 h-10 rounded-full" src={userimg} alt="" />
                                            <div className="ml-3">
                                                <p className="text-gray-900 font-semibold">{m.name}</p>
                                                <p className="text-gray-600">{m.email}</p>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-5 py-5 border-b border-gray-200 text-sm">
                                        <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                                            m.type === 'Teacher' ? 'bg-purple-100 text-purple-700' : 
                                            m.type === 'Student' ? 'bg-blue-100 text-blue-700' : 'bg-orange-100 text-orange-700'
                                        }`}>
                                            {m.type}
                                        </span>
                                    </td>
                                    <td className="px-5 py-5 border-b border-gray-200 text-sm">
                                        {m.type === 'Teacher' && <p>{m.subject} | {m.phoneNumber}</p>}
                                        {m.type === 'Student' && <p>Class: {m.classId} </p>}
                                        {m.type === 'Parent' && <p>{m.mobileNumber}</p>}
                                    </td>
                                    <td className="px-5 py-5 border-b border-gray-200 text-sm">
                                        <div className="flex space-x-3">
                                            <button onClick={() => { setViewingMember(m); setViewModalIsOpen(true); }} className="text-green-600 cursor-pointer"><HiOutlineEye size={20}/></button>
                                            <button onClick={() => handleDelete(m.id, m.type)} className="text-red-600 cursor-pointer"><HiOutlineTrash size={20}/></button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* View Member Modal */}
{viewingMember && (
    <Modal 
        isOpen={viewModalIsOpen} 
        onRequestClose={() => setViewModalIsOpen(false)} 
        contentLabel="View Member Details" 
        className="modal" 
        overlayClassName="overlay"
    >
        <div className="p-4">
            <div className="flex justify-between items-start mb-6">
                <h2 className="text-2xl font-bold text-gray-800">{viewingMember.type} Profile</h2>
                <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                    viewingMember.type === 'Teacher' ? 'bg-purple-100 text-purple-700' : 
                    viewingMember.type === 'Student' ? 'bg-blue-100 text-blue-700' : 'bg-orange-100 text-orange-700'
                }`}>
                    {viewingMember.type}
                </span>
            </div>

            <div className="space-y-6">
                <div className="flex items-center space-x-4">
                    <img className="w-20 h-20 rounded-full border-2 border-blue-100" src={userimg} alt={viewingMember.name} />
                    <div>
                        <p className="text-xl font-bold text-gray-900">{viewingMember.name}</p>
                        <p className="text-gray-600">{viewingMember.email}</p>
                    </div>
                </div>

                {/* Details Grid */}
                <div className="border-t border-gray-200 pt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                    
                    {/* Common Fields */}
                    <InfoBlock label="Email Address" value={viewingMember.email} />
                    
                    {/* --- STUDENT SPECIFIC FIELDS --- */}
                    {viewingMember.type === 'Student' && (
                        <>
                            <InfoBlock label="Age" value={viewingMember.age} />
                            <InfoBlock label="Class" value={viewingMember.classId} />
                            <InfoBlock label="Address" value={viewingMember.address} />
                            <InfoBlock 
                                label="Parent ID" 
                                value={parents.find(p => p.id === viewingMember.parentId)?.name || `ID: ${viewingMember.parentId}`} 
                            />
                        </>
                    )}

                    {/* --- TEACHER SPECIFIC FIELDS --- */}
                    {viewingMember.type === 'Teacher' && (
                        <>
                            <InfoBlock label="Subject Expertise" value={viewingMember.subject} />
                            <InfoBlock label="Phone Number" value={viewingMember.phoneNumber} />
                        </>
                    )}

                    {/* --- PARENT SPECIFIC FIELDS --- */}
                    {viewingMember.type === 'Parent' && (
                        <>
                            <InfoBlock label="Age" value={viewingMember.age} />
                            <InfoBlock label="Mobile Number" value={viewingMember.mobileNumber} />
                            <InfoBlock label="Home Address" value={viewingMember.address} />
                        </>
                    )}
                </div>
            </div>

            <div className="mt-8 flex justify-end">
                <button 
                    type="button" 
                    onClick={() => setViewModalIsOpen(false)} 
                    className="px-6 py-2 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 transition-colors shadow-md"
                >
                    Close Profile
                </button>
            </div>
        </div>
    </Modal>
)}

            {/* Add Member Modal */}
            <Modal isOpen={addModalIsOpen} onRequestClose={() => setAddModalIsOpen(false)} className="modal" overlayClassName="overlay">
                <div className="p-2">
                    <h2 className="text-2xl font-bold mb-4 text-gray-800">Add New Member</h2>
                    
                    {/* Selector for Type */}
                    <div className="flex space-x-4 mb-6">
                        {['Student', 'Teacher', 'Parent'].map(t => (
                            <button 
                                key={t}
                                onClick={() => setMemberType(t)}
                                className={`px-4 py-2 rounded-lg font-bold ${memberType === t ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}`}
                            >
                                {t}
                            </button>
                        ))}
                    </div>

                    <form onSubmit={handleSubmit}>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
                            <input required name="name" placeholder="Full Name" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                            <input required name="email" type="email" placeholder="Email" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                                                        
                            {/* Shared Age field for Student and Parent */}
                            {(memberType === 'Student' || memberType === 'Parent') && (
                                <input required name="age" type="number" placeholder="Age" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                            )}

                            {/* Student Specific */}
                            {memberType === 'Student' && (
                                <>
                                    <select required name="classId" placeholder="Class ID" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" >
                                        <option value="">Select Class</option>
                                        <option value="1">Class 1</option>
                                        <option value="2">Class 2</option>
                                        <option value="3">Class 3</option>
                                        <option value="4">Class 4</option>
                                        <option value="5">Class 5</option>
                                        <option value="6">Class 6</option>
                                        <option value="7">Class 7</option>
                                        <option value="8">Class 8</option>
                                        <option value="9">Class 9</option>
                                        <option value="10">Class 10</option>
                                        <option value="11">Class 11</option>
                                        <option value="12">Class 12</option>
                                    </select>
                                </>
                            )}

                            {/* Teacher Specific */}
                            {memberType === 'Teacher' && (
                                <>
                                    <input required name="subject" placeholder="Subject" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                                    <input required name="phoneNumber" placeholder="Phone Number" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                                    <select required name="classId" placeholder="Class ID" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" >
                                        <option value="">Select Class</option>
                                        <option value="1">Class 1</option>
                                        <option value="2">Class 2</option>
                                        <option value="3">Class 3</option>
                                        <option value="4">Class 4</option>
                                        <option value="5">Class 5</option>
                                        <option value="6">Class 6</option>
                                        <option value="7">Class 7</option>
                                        <option value="8">Class 8</option>
                                        <option value="9">Class 9</option>
                                        <option value="10">Class 10</option>
                                        <option value="11">Class 11</option>
                                        <option value="12">Class 12</option>
                                    </select>
                                </>
                            )}

                            {/* Parent Specific */}
                            {memberType === 'Parent' && (
                                <>
                                    <input required name="mobileNumber" placeholder="Mobile Number" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                                    <div className="col-span-full">
    <label className="block text-sm font-medium text-gray-700 mb-2">
        Select Children
    </label>
    <Select
        isMulti
        name="children"
        options={userData
            .filter(m => m.type === 'Student' && m.parentId === null)
            .map(student => ({ 
                value: student.id, 
                label: `${student.name} (Class: ${student.classId})` 
            }))
        }
        className="basic-multi-select"
        classNamePrefix="select"
        onChange={handleMultiSelectChange}
        placeholder="Search and select students..."
        styles={{
            control: (base) => ({
                ...base,
                borderColor: '#D1D5DB', // gray-300
                borderRadius: '0.5rem',  // rounded-lg
                padding: '2px',
                '&:hover': { borderColor: '#3B82F6' } // blue-500
            })
        }}
    />
</div>
                                </>
                            )}

                            <textarea name="address" placeholder="Address" onChange={handleInputChange} className="p-2 border border-gray-300 rounded-lg" />
                        </div>
                        <div className="mt-8 flex justify-end space-x-4">
                            <button type="button" onClick={() => setAddModalIsOpen(false)} className="px-6 py-2 border rounded-lg">Cancel</button>
                            <button type="submit" className="px-6 py-2 bg-blue-600 text-white rounded-lg">Add {memberType}</button>
                        </div>
                    </form>
                </div>
            </Modal>

            {/* Note: View Modal would be similar, just mapping viewingMember fields based on viewingMember.type */}
        </div>
    );
};

// Simple reusable style for inputs
const inputStyle = "w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500";

export default AddMembers;