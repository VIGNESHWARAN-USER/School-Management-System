import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from './components/Login/Login';
import ForgotPassword from './components/Login/ForgotPassword'; 
import VerifyOTP from './components/Login/VerifyOTP';        
import ResetPassword from './components/Login/ResetPassword';
import AddMembers from './components/Admin/AddMembers';
import { Toaster } from 'sonner';
import MarkAttendance from './components/MarkAttendance';

function App() {

  return (
    <>
      <Toaster richColors position="top-right" /> 
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/verify-otp" element={<VerifyOTP />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/add-members" element={<AddMembers />} />
        <Route path="/mark-attendance" element={<MarkAttendance />} />
      </Routes>
    </>
  );
}



export default App;