import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import {
  FaUserMd,
  FaUsers,
  FaRegCalendarAlt,
  FaSignOutAlt,
  FaPills,
  FaBars,
  FaTimes,
  FaAmbulance,
  FaUpload,
} from "react-icons/fa";
import {
  MdDashboard,
  MdEvent,
  MdFilterList,
  MdLibraryAdd,
  MdInventory,
  MdWarning,
  MdDelete,
  MdReceipt,
} from "react-icons/md";
import img from "../assets/login.jpg"; 


const Sidebar = ({ redCount }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const isAddEventPage = false;
  const accessLevel = localStorage.getItem("accessLevel") || "unknown";
  console.log("Access Level in Sidebar:", accessLevel);
  const [isOpen, setIsOpen] = useState(false);

  const menus = {
    Student: [
      { name: "Dashboard", to: "../dashboard", icon: <MdDashboard /> },
      { name: "Participate Events", to: "../participate-events", icon: <MdEvent /> },
      { name: "Fees Details", to: "../fees-details", icon: <MdReceipt /> },
    ],
    Parent: [
      { name: "Dashboard", to: "../dashboard", icon: <MdDashboard /> },
      { name: "View Fee Details", to: "../parent-fee", icon: <MdReceipt /> },
      { name: "Transaction History", to: "../transaction-history", icon: <MdReceipt /> },
    ],
    Teacher: [
      { name: "Dashboard", to: "../dashboard", icon: <MdDashboard /> },
      { name: "Mark Attendance", to: "../mark-attendance", icon: <MdEvent /> },
      { name: "View Attendance", to: "../view-attendance", icon: <MdEvent /> },
    ],
    Admin: [
      { name: "Admin Dashboard", to: "../admindashboard", icon: <FaUsers /> },
      { name: "Add Members", to: "../add-members", icon: <FaUsers /> },
      { name: "Manage Resources", to: "../manage-resources", icon: <FaUsers /> },
      { name: "Mark Attendance", to: "../mark-attendance", icon: <MdEvent /> },
      { name: "View Attendance", to: "../view-attendance", icon: <MdEvent /> },
      { name: "Manage Events", to: "../manage-events", icon: <MdEvent /> },
      { name: "Create Fee Structure", to: "../create-fee-structure", icon: <MdReceipt /> },
      { name: "Plan Installments", to: "../plan-installments", icon: <MdReceipt /> },
      { name: "Scedule Class", to: "../class-schedule", icon: <MdReceipt /> }
    ],
  };

  const currentMenu = menus[accessLevel] || [];


  return (
    <>
      <button
        className="md:hidden fixed top-4 left-4 z-50 p-2 bg-blue-600 text-white rounded-md shadow-md"
        onClick={() => setIsOpen(!isOpen)}
        aria-label="Toggle Menu"
        aria-expanded={isOpen}
        aria-controls="sidebar"
      >
        {isOpen ? <FaTimes size={20} /> : <FaBars size={20} />}
      </button>

      {/* Overlay for Mobile */}
      {isOpen && (
        <div
          className="md:hidden fixed inset-0 bg-black opacity-50 z-30"
          onClick={() => setIsOpen(false)}
          aria-hidden="true"
        ></div>
      )}

      
      <div
        id="sidebar"
        className={`
          fixed inset-y-0 left-0 z-40
          w-64 h-screen overflow-y-auto bg-blue-600 text-white flex flex-col shadow-lg
          transform transition-transform duration-300 ease-in-out
          ${isOpen ? 'translate-x-0' : '-translate-x-full'}
          md:relative md:translate-x-0 md:w-1/5 md:h-full md:flex md:shrink-0
          [scrollbar-width:none] [&::-webkit-scrollbar]:hidden
          `}
      >
        
        <div className="p-6 flex justify-center flex-shrink-0 relative">
          <h1 className="text-2xl font-bold">SMS Portal</h1>
        </div>

        
        <nav className="flex-1 px-4 py-2">
          {currentMenu.map((item, index) => {
            const absoluteBaseUrl = window.location.origin + location.pathname.substring(0, location.pathname.lastIndexOf('/'));
            const absoluteItemPath = new URL(item.to, absoluteBaseUrl + '/').pathname;
            const isActive = location.pathname === absoluteItemPath || (location.pathname.startsWith(absoluteItemPath + '/'));

            return (
              <Link
                key={index}
                to={item.to}
                className={`flex items-center justify-between p-3 mx-0 my-1 text-base rounded-lg font-medium transition duration-200 ease-in-out transform ${isActive
                  ? "bg-white text-blue-600 scale-100 shadow-md font-semibold"
                  : "hover:bg-blue-500 hover:text-white hover:scale-105"
                  }`}
              >
                
                <div className="flex items-center space-x-3">
                  <span className="flex-shrink-0 w-5 h-5">{item.icon}</span>
                  <span>{item.name}</span>
                </div>

              </Link>
            );
          })}
        </nav>


        
        <p className="flex justify-center font-bold tracking-wider text-sm px-4 py-2 text-center">
          Login as: {accessLevel.toUpperCase()}
        </p>

       
        <div className="p-4 mt-auto flex-shrink-0">
          <button
            onClick={() => {
              localStorage.clear();
              navigate("../login");
            }}
            className="w-full flex items-center justify-center space-x-3 py-2 bg-red-500 hover:bg-red-600 text-white font-semibold rounded-lg transition duration-200 ease-in-out"
          >
            <FaSignOutAlt />
            <span>Logout</span>
          </button>
        </div>
      </div>
    </>
  );
};

export default Sidebar;