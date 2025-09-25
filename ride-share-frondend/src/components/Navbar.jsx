import React from 'react';
import { NavLink } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <NavLink to="/" className="brand-link">
          <h1 className="brand-title">Ride Share Dashboard</h1>
        </NavLink>
      </div>
      
      <div className="navbar-menu">
        <ul className="navbar-nav">
          <li className="nav-item">
            <NavLink 
              to="/" 
              className={({ isActive }) => isActive ? 'nav-link nav-link--active' : 'nav-link'}
              end
            >
              Home
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink 
              to="/drivers" 
              className={({ isActive }) => isActive ? 'nav-link nav-link--active' : 'nav-link'}
            >
              Drivers
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink 
              to="/riders" 
              className={({ isActive }) => isActive ? 'nav-link nav-link--active' : 'nav-link'}
            >
              Riders
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink 
              to="/matching" 
              className={({ isActive }) => isActive ? 'nav-link nav-link--active' : 'nav-link'}
            >
              Matching
            </NavLink>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;