import React, { useState, useEffect, useMemo } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineCash, HiOutlinePlus, HiOutlineTrash, 
    HiOutlineInformationCircle, HiOutlineCalculator 
} from 'react-icons/hi';
import api from '../api';
import { use } from 'react';

const CreateFeeStructure = () => {
    const [loading, setLoading] = useState(false);
    const [classes, setClasses] = useState([]);
    useEffect(() => {
        const fetchClasses = async () => {
            try {
                const response = await api.get('/api/classrooms');
                setClasses(response.data);
            } catch (error) {
                console.error('Error fetching classes:', error);
                toast.error('Failed to fetch classes');
            }
        };
        fetchClasses();
    }, []);


    // Main Form State
    const [formData, setFormData] = useState({
        classId: '',
        academicYear: '2025-2026',
        components: [
            { componentName: 'Tuition Fee', description: 'Core academic charges', amount: 0, taxPercentage: 0 },
            { componentName: 'Bus Fee', description: 'Transport charges', amount: 0, taxPercentage: 0 }
        ]
    });

    useEffect(() => {
        if (formData.classId) {
            fetchExistingStructure(formData.classId);
        }
    }, [formData.classId]);


    const   fetchExistingStructure = async (classId) => {
        if (!classId) return;
        try {
            console.log(classId)
            const response = await api.get(`/api/fees/structure/${classId}`);
            if (response.data) {
                const { academicYear, components } = response.data;
                setFormData({ classId, academicYear, components });
            } else {
                setFormData(prev => ({ ...prev, classId, academicYear: '2025-2026', components: [] }));
            }
        } catch (error) {
            console.error('Error fetching existing fee structure:', error);
            toast.error('Failed to fetch existing fee structure');
        }
    };


    // Add a new empty component row
    const addComponent = () => {
        setFormData({
            ...formData,
            components: [...formData.components, { componentName: '', description: '', amount: 0, taxPercentage: 0 }]
        });
    };

    // Remove a component row
    const removeComponent = (index) => {
        const updated = formData.components.filter((_, i) => i !== index);
        setFormData({ ...formData, components: updated });
    };

    // Handle input changes for components
    const handleComponentChange = (index, field, value) => {
        const updatedComponents = [...formData.components];
        updatedComponents[index][field] = value;
        setFormData({ ...formData, components: updatedComponents });
    };

    // Calculate Totals for US_002 (Summing up correctly)
    const totals = useMemo(() => {
        let subtotal = 0;
        let taxTotal = 0;

        formData.components.forEach(c => {
            const amt = parseFloat(c.amount) || 0;
            const tax = (amt * (parseFloat(c.taxPercentage) || 0)) / 100;
            subtotal += amt;
            taxTotal += tax;
        });

        return {
            subtotal: subtotal.toFixed(2),
            taxTotal: taxTotal.toFixed(2),
            grandTotal: (subtotal + taxTotal).toFixed(2)
        };
    }, [formData.components]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!formData.classId) return toast.error("Please select a class");
        
        setLoading(true);
        const promise = await api.post('/api/fees/create-structure', formData);
        console.log('API Response:', promise.data);
        toast.promise(promise, {
            loading: 'Saving fee structure...',
            success: 'Fee structure created successfully!',
            error: (err) => `Error: ${err.response?.data || 'Failed to save'}`
        });
        setLoading(false);
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />
            
            <div className="flex-1 p-8 overflow-y-auto">
                <div className="mb-8">
                    <h1 className="text-2xl font-bold text-gray-800">Create Fee Structure</h1>
                    <p className="text-gray-500">Define class-wise fee components and taxes</p>
                </div>

                <form onSubmit={handleSubmit} className="space-y-6">
                    {/* Header Card: Class and Year */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200 grid grid-cols-1 md:grid-cols-2 gap-6">
    
                        {/* Select Class */}
                        <div>
                            <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase">
                                Select Class
                            </label>

                            <select 
                                required
                                value={formData.classId}
                                onChange={(e) => {
                                    const selected = classes.find(
                                        c => String(c.classId) === e.target.value
                                    );

                                    setFormData({
                                        ...formData,
                                        classId: selected?.classId || '',
                                        academicYear: selected?.academicYear || ''
                                    });
                                }}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white"
                            >
                                <option value="">Select a Class</option>
                                {classes.map(c => (
                                    <option key={c.classId} value={c.classId}>
                                        {c.className}
                                    </option>
                                ))}
                            </select>
                        </div>

                        {/* Academic Year */}
                        <div>
                            <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase">
                                Academic Year
                            </label>

                            <input 
                                type="text"
                                value={formData.academicYear || ''}
                                readOnly
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 cursor-not-allowed"
                            />
                        </div>

                    </div>

                    {/* Breakdown Section (US_002) */}
                    <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                        <div className="p-4 bg-gray-50 border-b flex justify-between items-center">
                            <h2 className="font-bold text-gray-700 flex items-center">
                                <HiOutlineCash className="mr-2 text-blue-600" size={20}/> Fee Breakdown
                            </h2>
                            <button 
                                type="button" 
                                onClick={addComponent}
                                className="flex items-center space-x-1 text-sm bg-blue-600 text-white px-3 py-1.5 rounded-lg hover:bg-blue-700 transition"
                            >
                                <HiOutlinePlus /> <span>Add Component</span>
                            </button>
                        </div>

                        <table className="w-full text-left">
                            <thead className="bg-gray-50 text-xs uppercase text-gray-500 border-b">
                                <tr>
                                    <th className="px-6 py-3">Component Name</th>
                                    <th className="px-6 py-3">Description</th>
                                    <th className="px-6 py-3 w-32">Base Amount</th>
                                    <th className="px-6 py-3 w-24">Tax %</th>
                                    <th className="px-6 py-3 w-16"></th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {formData.components.map((comp, index) => (
                                    <tr key={index} className="hover:bg-gray-50 transition">
                                        <td className="px-6 py-3">
                                            <select 
                                                required
                                                placeholder="e.g. Exam Fee"
                                                className="w-full bg-transparent border-b border-transparent focus:border-blue-500 outline-none py-1"
                                                value={comp.componentName}
                                                onChange={(e) => handleComponentChange(index, 'componentName', e.target.value)}
                                            >
                                                <option value="">Select Component</option>
                                                <option value="TUITION">Tuition Fee</option>
                                                <option value="BUS">Bus Fee</option>
                                                <option value="EXAM">Exam Fee</option>
                                                <option value="BOOKS">Books Fee</option>
                                                <option value="LIBRARY">Library Fee</option>
                                                <option value="STATIONARY">Stationary Fee</option>
                                            </select>

                                        </td>
                                        <td className="px-6 py-3">
                                            <input 
                                                placeholder="Clarification for parents..."
                                                className="w-full bg-transparent border-b border-transparent focus:border-blue-500 outline-none py-1 text-gray-500 text-sm"
                                                value={comp.description}
                                                onChange={(e) => handleComponentChange(index, 'description', e.target.value)}
                                            />
                                        </td>
                                        <td className="px-6 py-3">
                                            <input 
                                                type="number" required min="0"
                                                className="w-full bg-transparent border-b border-transparent focus:border-blue-500 outline-none py-1 font-mono"
                                                value={comp.amount}
                                                onChange={(e) => handleComponentChange(index, 'amount', e.target.value)}
                                            />
                                        </td>
                                        <td className="px-6 py-3">
                                            <input 
                                                type="number" min="0" max="100"
                                                className="w-full bg-transparent border-b border-transparent focus:border-blue-500 outline-none py-1 font-mono"
                                                value={comp.taxPercentage}
                                                onChange={(e) => handleComponentChange(index, 'taxPercentage', e.target.value)}
                                            />
                                        </td>
                                        <td className="px-6 py-3 text-right">
                                            <button 
                                                type="button" 
                                                onClick={() => removeComponent(index)}
                                                className="text-red-400 hover:text-red-600 transition"
                                            >
                                                <HiOutlineTrash size={18}/>
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {/* Summary and Submission */}
                    <div className="flex flex-col md:flex-row gap-6">
                        <div className="flex-1 bg-blue-50 p-6 rounded-xl border border-blue-100 flex items-start space-x-4">
                            <HiOutlineInformationCircle className="text-blue-500 mt-1" size={24}/>
                            <p className="text-sm text-blue-700">
                                This structure will be applied to all students in the selected class. 
                                Parents will see this detailed breakdown in their payment portal.
                            </p>
                        </div>

                        <div className="w-full md:w-80 bg-white p-6 rounded-xl shadow-sm border border-gray-200">
                            <h3 className="font-bold text-gray-800 mb-4 flex items-center">
                                <HiOutlineCalculator className="mr-2" /> Calculation Summary
                            </h3>
                            <div className="space-y-2 text-sm border-b pb-4 mb-4">
                                <div className="flex justify-between">
                                    <span className="text-gray-500">Subtotal:</span>
                                    <span className="font-mono">${totals.subtotal}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-gray-500">Total Tax:</span>
                                    <span className="font-mono text-red-500">+${totals.taxTotal}</span>
                                </div>
                            </div>
                            <div className="flex justify-between items-center mb-6">
                                <span className="font-bold text-gray-800 uppercase text-xs">Total Amount</span>
                                <span className="text-xl font-bold text-blue-600 font-mono">${totals.grandTotal}</span>
                            </div>
                            <button 
                                type="submit"
                                disabled={loading}
                                className="w-full bg-blue-600 text-white py-3 rounded-lg font-bold hover:bg-blue-700 transition shadow-md disabled:bg-blue-300"
                            >
                                {loading ? 'Saving...' : 'Save Fee Structure'}
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateFeeStructure;