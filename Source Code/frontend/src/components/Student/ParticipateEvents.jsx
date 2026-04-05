import React, { useEffect, useState, useMemo } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { 
    HiOutlineSearch, HiOutlinePencilAlt, HiOutlineTrash, HiOutlineEye, 
    HiOutlineUserGroup, HiOutlineCalendar, HiOutlineLocationMarker, HiX, 
    HiOutlineDocumentText, HiOutlineClock
} from 'react-icons/hi';
import api from '../api';
import { data } from 'react-router-dom';

const ParticipateEvents = () => {
    const [events, setEvents] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(true);
    const user = JSON.parse(localStorage.getItem('userData'));
    
    // States for Modals
    const [viewEvent, setViewEvent] = useState(null);
    const [editEvent, setEditEvent] = useState(null);

    console.log(viewEvent);

    useEffect(() => {
        fetchEvents();
    }, []);

    console.log(user);

    const fetchEvents = async () => {
        setLoading(true);
        try {
            const response = await api.get('http://localhost:8085/api/events/all');
            setEvents(response.data);
        } catch (error) {
            toast.error("Failed to load events");
        } finally {
            setLoading(false);
        }
    };

    const handleParticipate = async () => {

        const payload = {
            studentId: user.id,
            eventId: viewEvent.id,
        };

        try {
            console.log(payload);
            await api.post(`http://localhost:8085/api/events/participate`, payload);
            fetchEvents();
            toast.success("Successfully registered the event!");
            setViewEvent(null);
        } catch (error) {
            toast.error("Failed to participate in the event");
        }
    };


    const filteredEvents = useMemo(() => {
        return events.filter(e => 
            e.eventName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            e.organizer.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [events, searchTerm]);

    const StatusBadge = ({ status }) => {
        const colors = {
            Scheduled: 'bg-blue-100 text-blue-700 border-blue-200',
            Ongoing: 'bg-green-100 text-green-700 border-green-200',
            Completed: 'bg-gray-100 text-gray-700 border-gray-200',
            Cancelled: 'bg-red-100 text-red-700 border-red-200',
        };
        return (
            <span className={`px-2 py-1 rounded-full text-xs font-bold border ${colors[status] || colors.Scheduled}`}>
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
                        <h1 className="text-2xl font-bold text-gray-800">Participate Events</h1>
                    </div>
                    <div className="relative">
                        <HiOutlineSearch className="absolute left-3 top-3 text-gray-400" size={20} />
                        <input 
                            type="text"
                            placeholder="Search events..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none w-64 shadow-sm"
                        />
                    </div>
                </div>

                <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                    <table className="w-full text-left">
                        <thead className="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th className="px-6 py-4 text-xs font-semibold text-gray-600 uppercase">Event Details</th>
                                <th className="px-6 py-4 text-xs font-semibold text-gray-600 uppercase">Date & Time</th>
                                <th className="px-6 py-4 text-xs font-semibold text-gray-600 uppercase">Status</th>
                                <th className="px-6 py-4 text-xs font-semibold text-gray-600 uppercase">Participants</th>
                                <th className="px-6 py-4 text-xs font-semibold text-gray-600 uppercase text-right">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                            {loading ? (
                                <tr><td colSpan="5" className="text-center py-10">Loading events...</td></tr>
                            ) : filteredEvents.map((event) => (
                                <tr key={event.id} className="hover:bg-gray-50 transition">
                                    <td className="px-6 py-4">
                                        <div className="font-bold text-gray-900">{event.eventName}</div>
                                        <div className="text-xs text-gray-500">{event.eventLocation}</div>
                                    </td>
                                    <td className="px-6 py-4 text-sm text-gray-600">
                                        <div>{event.eventDate}</div>
                                        <div className="text-xs">{event.eventTime}</div>
                                    </td>
                                    <td className="px-6 py-4">
                                        <StatusBadge status={event.eventStatus} />
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex items-center text-sm text-gray-600">
                                            <HiOutlineUserGroup className="mr-1" />
                                            {event.registrationDTOS?.length || 0} / {event.maxParticipants}
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 text-right space-x-2">
                                        <button onClick={() => setViewEvent(event)} className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition">Participate</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* VIEW MODAL */}
            {viewEvent && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-xl shadow-2xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col">
                        <div className="p-6 border-b flex justify-between items-center bg-gray-50">
                            <h2 className="text-xl font-bold text-gray-800">{viewEvent.eventName} - Details</h2>
                            <button onClick={() => setViewEvent(null)} className="text-gray-500 hover:text-gray-800"><HiX size={24}/></button>
                        </div>
                        <div className="p-6 overflow-y-auto flex-1">
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                                <div className="bg-blue-50 p-4 rounded-lg">
                                    <label className="text-xs font-bold text-blue-600 uppercase">Organizer</label>
                                    <p className="text-gray-800 font-medium">{viewEvent.organizer}</p>
                                </div>
                                <div className="bg-green-50 p-4 rounded-lg">
                                    <label className="text-xs font-bold text-green-600 uppercase">Location</label>
                                    <p className="text-gray-800 font-medium">{viewEvent.eventLocation}</p>
                                </div>
                                <div className="bg-purple-50 p-4 rounded-lg">
                                    <label className="text-xs font-bold text-purple-600 uppercase">Description</label>
                                    <p className="text-gray-800 text-sm">{viewEvent.eventDescription}</p>
                                </div>
                            </div>
                            <h3 className="font-bold text-gray-700 mb-4 flex items-center"><HiOutlineUserGroup className="mr-2"/> Registered Participants</h3>
                            <div className="border rounded-lg overflow-hidden">
                                <table className="w-full text-left text-sm">
                                    <thead className="bg-gray-100">
                                        <tr>
                                            <th className="px-4 py-2">ID</th>
                                            <th className="px-4 py-2">Student Name</th>
                                            <th className="px-4 py-2">Email</th>
                                            <th className="px-4 py-2">Class ID</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y">
                                        {viewEvent.registrationDTOS?.length > 0 ? viewEvent.registrationDTOS.map((reg, idx) => (
                                            <tr key={idx} className="hover:bg-gray-50">
                                                <td className="px-4 py-2">{reg.studentId}</td>
                                                <td className="px-4 py-2 font-medium">{reg.name}</td>
                                                <td className="px-4 py-2">{reg.email}</td>
                                                <td className="px-4 py-2 text-gray-500">{reg.classId}</td>
                                            </tr>
                                        )) : <tr><td colSpan="4" className="text-center py-4 text-gray-500">No participants yet.</td></tr>}
                                    </tbody>
                                </table>
                            </div>
                            <button onClick={()=>{handleParticipate()}} className='px-4 py-2 rounded-lg cursor-pointer bg-blue-600 mt-6 text-white hover:bg-blue-700'>Participate</button>
                        </div>
                    </div>
                </div>
            )}

            {/* EDIT MODAL */}
            {editEvent && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-xl shadow-2xl w-full max-w-2xl overflow-hidden">
                        <div className="p-6 border-b flex justify-between items-center bg-gray-50">
                            <h2 className="text-xl font-bold text-gray-800">Edit Event Details</h2>
                            <button onClick={() => setEditEvent(null)} className="text-gray-500 hover:text-gray-800"><HiX size={24}/></button>
                        </div>
                        
                        <form onSubmit={handleUpdate} className="p-8 space-y-6 max-h-[75vh] overflow-y-auto">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Event Name</label>
                                    <div className="relative">
                                        <HiOutlineCalendar className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="text" required
                                            value={editEvent.eventName}
                                            onChange={(e) => setEditEvent({ ...editEvent, eventName: e.target.value })}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        />
                                    </div>
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Event Status</label>
                                    <select 
                                        value={editEvent.eventStatus}
                                        onChange={(e) => setEditEvent({ ...editEvent, eventStatus: e.target.value })}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white"
                                    >
                                        <option value="Scheduled">Scheduled</option>
                                        <option value="Ongoing">Ongoing</option>
                                        <option value="Completed">Completed</option>
                                        <option value="Cancelled">Cancelled</option>
                                    </select>
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Description</label>
                                <div className="relative">
                                    <HiOutlineDocumentText className="absolute left-3 top-3 text-gray-400" size={20} />
                                    <textarea 
                                        rows="3"
                                        value={editEvent.eventDescription}
                                        onChange={(e) => setEditEvent({ ...editEvent, eventDescription: e.target.value })}
                                        className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    ></textarea>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Date</label>
                                    <input 
                                        type="date" required min = {new Date().toISOString().split('T')[0]}
                                        value={editEvent.eventDate}
                                        onChange={(e) => setEditEvent({ ...editEvent, eventDate: e.target.value })}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Time</label>
                                    <div className="relative">
                                        <HiOutlineClock className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="time" required
                                            value={editEvent.eventTime}
                                            onChange={(e) => setEditEvent({ ...editEvent, eventTime: e.target.value })}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        />
                                    </div>
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Location</label>
                                    <div className="relative">
                                        <HiOutlineLocationMarker className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="text"
                                            value={editEvent.eventLocation}
                                            onChange={(e) => setEditEvent({ ...editEvent, eventLocation: e.target.value })}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Organizer</label>
                                    <input 
                                        type="text"
                                        value={editEvent.organizer}
                                        onChange={(e) => setEditEvent({ ...editEvent, organizer: e.target.value })}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Max Participants</label>
                                    <div className="relative">
                                        <HiOutlineUserGroup className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="number"
                                            value={editEvent.maxParticipants}
                                            onChange={(e) => setEditEvent({ ...editEvent, maxParticipants: parseInt(e.target.value) })}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className="flex items-center justify-end space-x-4 pt-6 border-t">
                                <button 
                                    type="button"
                                    onClick={() => setEditEvent(null)}
                                    className="px-6 py-2 border border-gray-300 rounded-lg font-bold text-gray-600 hover:bg-gray-100"
                                >
                                    Cancel
                                </button>
                                <button 
                                    type="submit"
                                    className="bg-blue-600 text-white px-8 py-2 rounded-lg font-bold hover:bg-blue-700 shadow-md"
                                >
                                    Save Changes
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ParticipateEvents;