import React, { useEffect, useState, useMemo } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineSearch, HiOutlineDocumentDownload, HiOutlineInformationCircle,
    HiOutlineShieldCheck, HiOutlinePrinter 
} from 'react-icons/hi';
import api from '../api';

// CORRECT IMPORTS FOR VITE/REACT
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable'; 

const TransactionHistory = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    
    const studentId = localStorage.getItem('studentId');

    useEffect(() => {
        fetchHistory();
    }, []);

    const fetchHistory = async () => {
        setLoading(true);
        try {
            const res = await api.get(`http://localhost:8085/api/fees/student/${studentId}/transactions`);
            setTransactions(res.data.body || []);
        } catch (err) {
            toast.error("Failed to load payment history");
        } finally {
            setLoading(false);
        }
    };

    const filteredTx = useMemo(() => {
        return transactions.filter(tx => 
            tx.transactionId.toLowerCase().includes(searchTerm.toLowerCase()) ||
            tx.paymentMethod.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [transactions, searchTerm]);

    // --- FIXED PDF GENERATION LOGIC ---
    const downloadInvoice = (tx) => {
        try {
            const doc = new jsPDF();

            // 1. Add Header / Branding
            doc.setFontSize(22);
            doc.setTextColor(37, 99, 235); // Blue-600
            doc.text("ACADEMIA UNIVERSITY", 14, 22);
            
            doc.setFontSize(10);
            doc.setTextColor(100);
            doc.text("Official Payment Receipt", 14, 28);
            doc.text(`Generated on: ${new Date().toLocaleString()}`, 14, 33);

            // 2. Horizontal Line
            doc.setDrawColor(200, 200, 200);
            doc.line(14, 38, 196, 38);

            // 3. Invoice Metadata
            doc.setFontSize(11);
            doc.setTextColor(0);
            doc.setFont("helvetica", "bold");
            doc.text("BILL TO:", 14, 48);
            doc.setFont("helvetica", "normal");
            doc.text(`Student ID: ${studentId}`, 14, 54);
            
            doc.setFont("helvetica", "bold");
            doc.text("INVOICE DETAILS:", 120, 48);
            doc.setFont("helvetica", "normal");
            doc.text(`Tx ID: ${tx.transactionId}`, 120, 54);
            doc.text(`Date: ${new Date(tx.paymentDate).toLocaleDateString()}`, 120, 60);

            // 4. Transaction Table (Using the imported autoTable function)
            const tableColumn = ["Description", "Method", "Type", "Status", "Amount"];
            const tableRows = [
                [
                    tx.installmentLabel || "Tuition Fee Payment",
                    tx.paymentMethod,
                    tx.paymentType,
                    tx.status,
                    `$${tx.amountPaid.toFixed(2)}`
                ]
            ];

            autoTable(doc, {
                startY: 70,
                head: [tableColumn],
                body: tableRows,
                theme: 'striped',
                headStyles: { fillColor: [37, 99, 235], fontSize: 10, halign: 'center' },
                columnStyles: {
                    4: { halign: 'right', fontStyle: 'bold' }
                }
            });

            // 5. Summary and Total
            // Use doc.lastAutoTable.finalY to find where the table ended
            const finalY = doc.lastAutoTable.finalY + 15;
            doc.setFontSize(14);
            doc.setFont("helvetica", "bold");
            doc.text(`Total Amount Paid: $${tx.amountPaid.toFixed(2)}`, 196, finalY, { align: 'right' });

            // 6. Footer
            doc.setFontSize(9);
            doc.setFont("helvetica", "italic");
            doc.setTextColor(150);
            doc.text("This is a computer-generated receipt. No signature required.", 105, finalY + 30, { align: 'center' });

            // Save the PDF
            doc.save(`Invoice_${tx.transactionId}.pdf`);
            toast.success("Invoice downloaded!");
        } catch (error) {
            console.error("PDF Error:", error);
            toast.error("Could not generate PDF");
        }
    };

    const StatusBadge = ({ status }) => {
        const isSuccess = status === 'SUCCESS';
        return (
            <span className={`px-2 py-1 rounded-full text-[10px] font-bold border ${isSuccess ? 'bg-green-100 text-green-700 border-green-200' : 'bg-red-100 text-red-700 border-red-200'}`}>
                {status}
            </span>
        );
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />

            <div className="flex-1 p-8 overflow-y-auto">
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">Transaction History</h1>
                        <p className="text-sm text-gray-500">Track all fee payments for Student ID: {studentId}</p>
                    </div>

                    <div className="relative">
                        <HiOutlineSearch className="absolute left-3 top-3 text-gray-400" size={20} />
                        <input 
                            type="text"
                            placeholder="Search Transaction ID..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none w-72 shadow-sm"
                        />
                    </div>
                </div>

                {loading ? (
                    <div className="text-center py-20 text-gray-400">Loading history...</div>
                ) : filteredTx.length === 0 ? (
                    <div className="bg-white rounded-2xl p-16 text-center border border-dashed border-gray-300">
                        <HiOutlineInformationCircle size={48} className="mx-auto text-gray-300 mb-4" />
                        <h3 className="text-lg font-bold text-gray-600">No Payment History Available</h3>
                        <p className="text-gray-400">You haven't made any fee payments yet.</p>
                    </div>
                ) : (
                    <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-200">
                                <tr>
                                    <th className="px-6 py-4 text-xs font-bold text-gray-500 uppercase tracking-wider">Date & Time</th>
                                    <th className="px-6 py-4 text-xs font-bold text-gray-500 uppercase tracking-wider">Transaction Details</th>
                                    <th className="px-6 py-4 text-xs font-bold text-gray-500 uppercase tracking-wider">Method</th>
                                    <th className="px-6 py-4 text-xs font-bold text-gray-500 uppercase tracking-wider">Amount Paid</th>
                                    <th className="px-6 py-4 text-xs font-bold text-gray-500 uppercase tracking-wider text-center">Status</th>
                                    <th className="px-6 py-4 text-xs font-bold text-gray-500 uppercase tracking-wider text-right">Invoice</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-200">
                                {filteredTx.map((tx, idx) => (
                                    <tr key={idx} className="hover:bg-gray-50/50 transition">
                                        <td className="px-6 py-4">
                                            <div className="text-sm font-medium text-gray-900">
                                                {new Date(tx.paymentDate).toLocaleDateString()}
                                            </div>
                                            <div className="text-xs text-gray-400">
                                                {new Date(tx.paymentDate).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                                            </div>
                                        </td>
                                        <td className="px-6 py-4">
                                            <div className="text-xs font-mono font-bold text-blue-600 bg-blue-50 px-2 py-0.5 rounded inline-block mb-1">
                                                {tx.transactionId}
                                            </div>
                                            <div className="text-xs text-gray-500">{tx.installmentLabel}</div>
                                        </td>
                                        <td className="px-6 py-4">
                                            <div className="flex items-center text-sm text-gray-600">
                                                <HiOutlineShieldCheck className="mr-1.5 text-green-500" />
                                                {tx.paymentMethod}
                                            </div>
                                            <div className="text-[10px] text-gray-400 uppercase">{tx.paymentType}</div>
                                        </td>
                                        <td className="px-6 py-4">
                                            <div className="text-sm font-bold text-gray-900 font-mono">${tx.amountPaid.toFixed(2)}</div>
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            <StatusBadge status={tx.status} />
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <button 
                                                onClick={() => downloadInvoice(tx)}
                                                className="p-2 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition"
                                                title="Download PDF Invoice"
                                            >
                                                <HiOutlineDocumentDownload size={22} />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        
                        <div className="p-4 bg-gray-50 border-t flex items-center justify-center text-xs text-gray-400">
                            <HiOutlinePrinter className="mr-1" />
                            Showing {filteredTx.length} transactions. Contact support for disputes.
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default TransactionHistory;