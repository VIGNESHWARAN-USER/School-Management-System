import React, { useEffect, useState } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineCash, HiOutlineCalendar, HiOutlineInformationCircle, 
    HiOutlineCheckCircle, HiOutlineClock, HiOutlineCreditCard,
    HiOutlineReceiptTax, HiOutlineUserCircle
} from 'react-icons/hi';
import api from '../api';

const FeeDashboard = () => {
    const [feeData, setFeeData] = useState([]);
    const [showPayModal, setShowPayModal] = useState(false);
    const [selectedInstallment, setSelectedInstallment] = useState(null);

    const user = JSON.parse(localStorage.getItem('userData'));

    useEffect(() => {
        fetchFeeDetails();
    }, []);

    const fetchFeeDetails = async () => {
        try {
            console.log("Hii");
            const res = await api.get(`/api/fees/student/${user.id}`);
            console.log("Fetched fee details:", res.data);
            setFeeData(res.data);
        } catch (err) {
            console.log("Error fetching fee details:", err);    
            toast.error("Failed to load fee details");
        } 
    };

    const handlePayment = async () => {
        if (!selectedInstallment) return;
        
        const promise = new Promise((resolve) => setTimeout(resolve, 2000)); // Simulate gateway

        toast.promise(promise, {
            loading: 'Redirecting to Payment Gateway...',
            success: () => {
                setShowPayModal(false);
                fetchFeeDetails(); // Refresh data
                return 'Payment Successful! Status Updated.';
            },
            error: 'Transaction failed. Please try again.'
        });
    };
 

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                {/* 1. Header Summary (US_001 & US_013) */}
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">Fee Statement</h1>
                        <div className="flex items-center text-gray-500 mt-1">
                            <HiOutlineUserCircle className="mr-1" />
                            <span>Student ID: {user.id} | Class: {feeData?.classId}</span>
                        </div>
                    </div>
                    
                    <div className="flex gap-4">
                        <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200">
                            <p className="text-xs font-bold text-gray-400 uppercase">Total Paid</p>
                            <p className="text-xl font-bold text-green-600">${feeData?.amountPaid}</p>
                        </div>
                        <div className="bg-white p-4 rounded-xl shadow-sm border border-red-100 bg-red-50">
                            <p className="text-xs font-bold text-red-400 uppercase">Remaining Balance</p>
                            <p className="text-xl font-bold text-red-600">${feeData?.remainingBalance}</p>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    
                    {/* 2. Fee Breakdown (US_002) */}
                    <div className="lg:col-span-2 space-y-6">
                        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-4 bg-gray-50 border-b">
                                <h2 className="font-bold text-gray-700 flex items-center">
                                    <HiOutlineReceiptTax className="mr-2 text-blue-600" /> Fee Breakdown & Taxes
                                </h2>
                            </div>
                            <table className="w-full text-left">
                                <thead className="text-xs text-gray-400 uppercase bg-gray-50/50">
                                    <tr>
                                        <th className="px-6 py-3">Component</th>
                                        <th className="px-6 py-3">Description</th>
                                        <th className="px-6 py-3 text-right">Tax %</th>
                                        <th className="px-6 py-3 text-right">Amount</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-100">
                                    {feeData?.breakdown.map((comp, idx) => (
                                        <tr key={idx} className="text-sm">
                                            <td className="px-6 py-4 font-bold text-gray-700">{comp.name.replace('_', ' ')}</td>
                                            <td className="px-6 py-4 text-gray-500">{comp.description}</td>
                                            <td className="px-6 py-4 text-right text-gray-400">{comp.taxPercentage}%</td>
                                            <td className="px-6 py-4 text-right font-mono font-bold">${comp.amount}</td>
                                        </tr>
                                    ))}
                                    <tr className="bg-gray-50 font-bold">
                                        <td colSpan="3" className="px-6 py-4 text-right">Total (Incl. Taxes)</td>
                                        <td className="px-6 py-4 text-right text-blue-600 font-mono text-lg">
                                            ${feeData?.totalFeeWithTax}
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    {/* 3. Installment Plan & Payment (US_012 & US_003) */}
                    <div className="lg:col-span-1">
                        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-4 bg-gray-50 border-b">
                                <h2 className="font-bold text-gray-700 flex items-center">
                                    <HiOutlineCalendar className="mr-2 text-purple-600" /> Payment Schedule
                                </h2>
                            </div>
                            <div className="p-4 space-y-4">
                                {feeData?.installments.map((inst, idx) => (
                                    <div key={idx} className={`p-4 rounded-xl border transition ${inst.status === 'PAID' ? 'bg-green-50 border-green-200' : 'bg-white border-gray-200'}`}>
                                        <div className="flex justify-between items-start mb-2">
                                            <div>
                                                <p className="text-sm font-bold text-gray-700">{inst.name}</p>
                                                <p className="text-xs text-gray-500 flex items-center mt-1">
                                                    <HiOutlineClock className="mr-1" /> Due: {inst.dueDate}
                                                </p>
                                            </div>
                                            {inst.status === 'PAID' ? (
                                                <span className="flex items-center text-xs font-bold text-green-600 bg-green-100 px-2 py-1 rounded-full">
                                                    <HiOutlineCheckCircle className="mr-1"/> PAID
                                                </span>
                                            ) : (
                                                <span className="text-xs font-bold text-orange-600 bg-orange-100 px-2 py-1 rounded-full">
                                                    PENDING
                                                </span>
                                            )}
                                        </div>
                                        <div className="flex justify-between items-center mt-4">
                                            <p className="text-lg font-bold font-mono text-gray-800">${inst.amount}</p>
                                            
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Payment Gateway Modal (US_015) */}
            {showPayModal && (
                <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
                        <div className="p-6 bg-gray-50 border-b text-center">
                            <HiOutlineCreditCard className="mx-auto text-blue-600 mb-2" size={40} />
                            <h2 className="text-xl font-bold text-gray-800">Complete Payment</h2>
                            <p className="text-sm text-gray-500">Amount to pay: <span className="font-bold text-gray-800">${selectedInstallment?.amount}</span></p>
                        </div>
                        
                        <div className="p-6 space-y-4">
                            <label className="text-xs font-bold text-gray-400 uppercase">Select Payment Method</label>
                            <div className="grid grid-cols-1 gap-3">
                                {['UPI (PhonePe/GPay)', 'Debit / Credit Card', 'Net Banking'].map((method) => (
                                    <button key={method} className="flex items-center justify-between p-4 border rounded-xl hover:border-blue-500 hover:bg-blue-50 transition group">
                                        <span className="font-medium text-gray-700 group-hover:text-blue-700">{method}</span>
                                        <div className="w-4 h-4 rounded-full border border-gray-300 group-hover:border-blue-500"></div>
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div className="p-6 bg-gray-50 flex gap-3">
                            <button onClick={() => setShowPayModal(false)} className="flex-1 py-3 text-gray-600 font-bold">Cancel</button>
                            <button onClick={handlePayment} className="flex-1 py-3 bg-blue-600 text-white rounded-xl font-bold shadow-lg hover:bg-blue-700">Confirm Payment</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default FeeDashboard;