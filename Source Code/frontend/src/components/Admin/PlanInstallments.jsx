import React, { useState, useEffect, useMemo } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineCalendar, HiOutlineViewGrid, HiOutlinePlus, 
    HiOutlineTrash, HiOutlineChartPie, HiOutlineInformationCircle 
} from 'react-icons/hi';
import api from '../api';

const PlanInstallments = () => {
    const [loading, setLoading] = useState(false);
    const [structures, setStructures] = useState([]); 
    const [selectedStructure, setSelectedStructure] = useState(null);

    // Installment rows state
    const [installments, setInstallments] = useState([
        { installmentName: 'Term 1', dueDate: '', percentage: 50 },
        { installmentName: 'Term 2', dueDate: '', percentage: 50 }
    ]);

    useEffect(() => {
        const fetchStructures = async () => {
            try {
                const res = await api.get('http://localhost:8085/api/fees/all-structures');
                console.log("Fetched fee structures:", res.data);
                setStructures(res.data);
                if (selectedStructure) {
                    const data = await api.get(`http://localhost:8085/api/fees/get-installments/${selectedStructure?.feeStructureId}`);
                    console.log("Fetched installments:", data.data);
                    setInstallments(data.data || []);
                }
                else setInstallments([]);
            } catch (err) {
                console.log("Error fetching fee structures or installments:", err);
                toast.error("Failed to load fee structures");
            }
        };
        fetchStructures();
    }, [selectedStructure]);
    

    const handleStructureChange = (id) => {
        console.log("Selected structure ID:", id);
        const found = structures.find(s => s.classId === id);
        setSelectedStructure(found);
    };

    const addInstallment = () => {
        setInstallments([...installments, { installmentName: '', dueDate: '', percentage: 0 }]);
    };

    const removeInstallment = (index) => {
        setInstallments(installments.filter((_, i) => i !== index));
    };

    const updateInstallment = (index, field, value) => {
        const updated = [...installments];
        updated[index][field] = value;
        setInstallments(updated);
    };

    // US_014 Logic: Check if total percentages equal 100%
    const totalPercentage = useMemo(() => {
        return installments.reduce((acc, curr) => acc + (parseFloat(curr.percentage) || 0), 0);
    }, [installments]);
    console.log(selectedStructure)
    const saveInstallmentPlan = async (e) => {
        e.preventDefault();
        
        if (!selectedStructure) return toast.error("Please select a fee structure first");
        if (totalPercentage !== 100) return toast.error(`Total percentage must equal 100%. Current: ${totalPercentage}%`);
        
        setLoading(true);
        const payload = {
            feeStructureId: selectedStructure.feeStructureId,
            installments: installments
        };

        console.log("Saving installment plan with payload:", payload);

        const promise = api.post('http://localhost:8085/api/fees/save-installments', payload);

        toast.promise(promise, {
            loading: 'Creating installment plan...',
            success: 'Installment plan saved and applied!',
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
                    <h1 className="text-2xl font-bold text-gray-800">Plan Installments</h1>
                    <p className="text-gray-500">Divide the total fee into multiple payment terms</p>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    
                    {/* LEFT: Selection & Info */}
                    <div className="lg:col-span-1 space-y-6">
                        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
                            <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase">Select Fee Structure</label>
                            <select 
                                onChange={(e) => handleStructureChange(e.target.value)}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white mb-4"
                            >
                                <option value="">Select Class</option>
                                {structures.map(s => (
                                    <option key={s.id} value={s.classId}>{s.className}</option>
                                ))}
                            </select>

                            {selectedStructure && (
                                <div className="mt-4 p-4 bg-blue-50 rounded-lg border border-blue-100">
                                    <div className="text-xs text-blue-600 font-bold uppercase mb-1">Total Fee Amount</div>
                                    <div className="text-2xl font-bold text-blue-800 font-mono">
                                        ${selectedStructure.totalAmount?.toLocaleString()}
                                    </div>
                                    <p className="text-xs text-blue-500 mt-2 italic">
                                        *Installments will be calculated based on this total.
                                    </p>
                                </div>
                            )}
                        </div>

                        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
                            <h3 className="font-bold text-gray-800 flex items-center mb-4">
                                <HiOutlineChartPie className="mr-2 text-purple-600" size={20}/> Allocation Status
                            </h3>
                            <div className="w-full bg-gray-200 rounded-full h-4 mb-2">
                                <div 
                                    className={`h-4 rounded-full transition-all duration-500 ${totalPercentage === 100 ? 'bg-green-500' : 'bg-purple-500'}`} 
                                    style={{ width: `${Math.min(totalPercentage, 100)}%` }}
                                ></div>
                            </div>
                            <div className="flex justify-between text-sm font-bold">
                                <span className={totalPercentage !== 100 ? 'text-red-500' : 'text-green-600'}>
                                    {totalPercentage}% Allocated
                                </span>
                                <span className="text-gray-400">Target: 100%</span>
                            </div>
                        </div>
                    </div>

                    {/* RIGHT: Installment Rows */}
                    <div className="lg:col-span-2">
                        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-4 bg-gray-50 border-b flex justify-between items-center">
                                <h2 className="font-bold text-gray-700 flex items-center">
                                    <HiOutlineCalendar className="mr-2 text-blue-600" size={20}/> Payment Schedule
                                </h2>
                                <button 
                                    type="button" onClick={addInstallment}
                                    className="flex items-center space-x-1 text-sm bg-blue-600 text-white px-3 py-1.5 rounded-lg hover:bg-blue-700 transition"
                                >
                                    <HiOutlinePlus /> <span>Add Installment</span>
                                </button>
                            </div>

                            <div className="p-6">
                                <div className="space-y-4">
                                    {installments.map((inst, index) => (
                                        <div key={index} className="flex flex-col md:flex-row items-center gap-4 p-4 border rounded-lg bg-gray-50 relative">
                                            <div className="flex-1">
                                                <label className="text-[10px] font-bold text-gray-400 uppercase">Label</label>
                                                <input 
                                                    placeholder="e.g. Quarter 1"
                                                    value={inst.installmentName}
                                                    onChange={(e) => updateInstallment(index, 'installmentName', e.target.value)}
                                                    className="w-full bg-transparent border-b border-gray-300 focus:border-blue-500 outline-none py-1"
                                                />
                                            </div>
                                            <div className="w-full md:w-44">
                                                <label className="text-[10px] font-bold text-gray-400 uppercase">Due Date</label>
                                                <input 
                                                    type="date"
                                                    value={inst.dueDate}
                                                    onChange={(e) => updateInstallment(index, 'dueDate', e.target.value)}
                                                    className="w-full bg-transparent border-b border-gray-300 focus:border-blue-500 outline-none py-1"
                                                />
                                            </div>
                                            <div className="w-full md:w-28">
                                                <label className="text-[10px] font-bold text-gray-400 uppercase">Share (%)</label>
                                                <input 
                                                    type="number"
                                                    value={inst.percentage}
                                                    onChange={(e) => updateInstallment(index, 'percentage', parseFloat(e.target.value))}
                                                    className="w-full bg-transparent border-b border-gray-300 focus:border-blue-500 outline-none py-1 font-bold"
                                                />
                                            </div>
                                            <button 
                                                onClick={() => removeInstallment(index)}
                                                className="mt-4 md:mt-0 text-red-400 hover:text-red-600"
                                            >
                                                <HiOutlineTrash size={20}/>
                                            </button>
                                        </div>
                                    ))}
                                </div>

                                <div className="mt-8 flex justify-end">
                                    <button 
                                        onClick={saveInstallmentPlan}
                                        disabled={loading || totalPercentage !== 100}
                                        className="bg-blue-600 text-white px-10 py-3 rounded-xl font-bold hover:bg-blue-700 transition shadow-lg disabled:bg-gray-300"
                                    >
                                        {loading ? 'Saving Plan...' : 'Finalize & Save Plan'}
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div className="mt-6 flex items-start p-4 bg-yellow-50 border border-yellow-200 rounded-xl">
                            <HiOutlineInformationCircle className="text-yellow-600 mr-3 mt-1" size={20}/>
                            <p className="text-xs text-yellow-700 leading-relaxed">
                                <strong>Note:</strong> Once saved, this installment plan will be visible to parents of the selected class. 
                                Each installment will generate an "Unpaid" status until the parent completes the transaction.
                            </p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default PlanInstallments;