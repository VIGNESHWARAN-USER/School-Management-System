import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from './components/Login/Login';
import ForgotPassword from './components/Login/ForgotPassword'; 
import VerifyOTP from './components/Login/VerifyOTP';        
import ResetPassword from './components/Login/ResetPassword';
import AddMembers from './components/Admin/AddMembers';
import { Toaster } from 'sonner';
import MarkAttendance from './components/MarkAttendance';
import ViewAttendance from './components/ViewAttendance';
import AddEvent from './components/Admin/AddEvent';
import ManageEvents from './components/Admin/ManageEvents';
import ParticipateEvents from './components/Student/ParticipateEvents';
import CreateFeeStructure from './components/Admin/CreateFeeStructure';
import PlanInstallments from './components/Admin/PlanInstallments';
import ParentFeeDashboard from './components/Parent/ParentFeeDashboard';
import FeeDashboard from './components/Student/FeeDashboard';
import TransactionHistory from './components/Parent/TransactionHistory';
import ClassroomSchedule from './components/Admin/ClassroomSchedule';
import ManageResources from './components/Admin/ManageResources';
import MyAttendance from './components/MyAttendance';
import ViewChildAttendance from './components/Parent/ViewChildAttendance';


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
        <Route path="/view-attendance" element={<ViewAttendance />} />
        <Route path="/add-event" element={<AddEvent />} />
        <Route path="/manage-events" element={<ManageEvents />} />
        <Route path='/participate-events' element={<ParticipateEvents/>} />
        <Route path="/create-fee-structure" element={<CreateFeeStructure />} />
        <Route path="/plan-installments" element={<PlanInstallments/>} />
        <Route path="/parent-fee" element={<ParentFeeDashboard />} />
        <Route path="/fees-details" element={<FeeDashboard />} />
        <Route path="/transaction-history" element={<TransactionHistory />} />
        <Route path="/class-schedule" element={<ClassroomSchedule/>} />
        <Route path="/manage-resources" element={<ManageResources />} />
        <Route path="/my-attendance" element={<MyAttendance />} />
        <Route path="/child-attendance" element={<ViewChildAttendance />} />
      </Routes>
    </>
  );
}



export default App;