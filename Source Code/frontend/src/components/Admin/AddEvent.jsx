import React, { useState } from 'react';
import Sidebar from '../Sidebar';
import { toast, Toaster } from 'sonner';
import { HiOutlineCalendar, HiOutlineLocationMarker, HiOutlineUserGroup, HiOutlineClock, HiOutlineDocumentText, HiOutlineArrowLeft } from 'react-icons/hi';
import api from "../api"; 

const AddEvent = () => {
    const [loading, setLoading] = useState(false);
    
    // Default form state matching EventDTO
    const [eventData, setEventData] = useState({
        eventName: '',
        eventDescription: '',
        eventDate: new Date().toISOString().split('T')[0],
        eventTime: '09:00',
        eventLocation: '',
        organizer: '',
        maxParticipants: 50,
        eventStatus: 'Scheduled'
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEventData(prev => ({ ...prev, [name]: value }));
    };

    const submitEvent = async (e) => {
        e.preventDefault();
        setLoading(true);

        // API endpoint
        const endpoint = '/api/events/add';

        const promise = api.post(endpoint, eventData);

        toast.promise(promise, {
            loading: 'Creating event...',
            success: (response) => {
                // Clear form on success
                setEventData({
                    eventName: '',
                    eventDescription: '',
                    eventDate: new Date().toISOString().split('T')[0],
                    eventTime: '09:00',
                    eventLocation: '',
                    organizer: '',
                    maxParticipants: 50,
                    eventStatus: 'Scheduled'
                });
                return 'Event created successfully!';
            },
            error: (err) => `Failed to create event: ${err.response?.data || err.message}`
        });

        setLoading(false);
    };

    return (
        <div className="flex h-screen bg-gray-50">
            <Sidebar />
            <Toaster richColors position="top-right" />
            
            <div className="flex-1 p-8 overflow-y-auto">
                {/* Header Section */}
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">Create New Event</h1>
                        <p className="text-gray-500">Schedule and manage upcoming school activities</p>
                    </div>
                    <button 
                        type="button" 
                        onClick={() => window.history.back()}
                        className="flex items-center space-x-1 text-sm bg-gray-600 text-white px-3 py-1.5 rounded-lg hover:bg-gray-700 transition"
                    >
                        <HiOutlineArrowLeft /> <span>Back</span>
                    </button>
                </div>

                {/* Form Section - White Card Layout */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                    <div className="p-8">
                        <form onSubmit={submitEvent} className="space-y-6">
                            
                            {/* Event Name & Status */}
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Event Name</label>
                                    <div className="relative">
                                        <HiOutlineCalendar className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="text"
                                            name="eventName"
                                            required
                                            placeholder="e.g. Annual Sports Day"
                                            value={eventData.eventName}
                                            onChange={handleChange}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                        />
                                    </div>
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Event Status</label>
                                    <select 
                                        name="eventStatus"
                                        value={eventData.eventStatus}
                                        onChange={handleChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition bg-white"
                                    >
                                        <option value="Scheduled">Scheduled</option>
                                        <option value="Ongoing">Ongoing</option>
                                        <option value="Completed">Completed</option>
                                        <option value="Cancelled">Cancelled</option>
                                    </select>
                                </div>
                            </div>

                            {/* Description */}
                            <div>
                                <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Description</label>
                                <div className="relative">
                                    <HiOutlineDocumentText className="absolute left-3 top-3 text-gray-400" size={20} />
                                    <textarea 
                                        name="eventDescription"
                                        rows="3"
                                        placeholder="Enter event details and agenda..."
                                        value={eventData.eventDescription}
                                        onChange={handleChange}
                                        className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                    ></textarea>
                                </div>
                            </div>

                            {/* Date, Time, and Location */}
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Date</label>
                                    <input 
                                        type="date"
                                        name="eventDate"
                                        min={new Date().toISOString().split('T')[0]}
                                        required
                                        value={eventData.eventDate}
                                        onChange={handleChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Time</label>
                                    <div className="relative">
                                        <HiOutlineClock className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="time"
                                            name="eventTime"
                                            required
                                            value={eventData.eventTime}
                                            onChange={handleChange}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                        />
                                    </div>
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Location</label>
                                    <div className="relative">
                                        <HiOutlineLocationMarker className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="text"
                                            name="eventLocation"
                                            placeholder="Auditorium / Ground"
                                            value={eventData.eventLocation}
                                            onChange={handleChange}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                        />
                                    </div>
                                </div>
                            </div>

                            {/* Organizer and Participants */}
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Organizer</label>
                                    <input 
                                        type="text"
                                        name="organizer"
                                        placeholder="Name or Department"
                                        value={eventData.organizer}
                                        onChange={handleChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-600 mb-2 uppercase tracking-wider">Max Participants</label>
                                    <div className="relative">
                                        <HiOutlineUserGroup className="absolute left-3 top-3 text-gray-400" size={20} />
                                        <input 
                                            type="number"
                                            name="maxParticipants"
                                            value={eventData.maxParticipants}
                                            onChange={handleChange}
                                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition"
                                        />
                                    </div>
                                </div>
                            </div>

                            {/* Action Buttons */}
                            <div className="flex items-center justify-end space-x-4 pt-6 border-t border-gray-100">
                                <button 
                                    type="button"
                                    onClick={() => window.history.back()}
                                    className="px-6 py-2 border border-gray-300 rounded-lg font-bold text-gray-600 hover:bg-gray-100 transition"
                                >
                                    Cancel
                                </button>
                                <button 
                                    type="submit"
                                    disabled={loading}
                                    className="bg-blue-600 text-white px-8 py-2 rounded-lg font-bold hover:bg-blue-700 transition shadow-md disabled:bg-blue-300"
                                >
                                    {loading ? 'Saving...' : 'Create Event'}
                                </button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AddEvent;