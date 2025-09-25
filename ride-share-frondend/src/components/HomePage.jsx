import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div className="page">
      <div className="page-container">
        <header className="page-header">
          <h1 className="page-title">Welcome to Ride Share Dashboard</h1>
          <p className="page-subtitle">
            Manage drivers, riders, and optimize ride matching with our comprehensive platform
          </p>
        </header>

        <div className="home-grid">
          <div className="feature-card">
            <div className="feature-icon">🚗</div>
            <h3 className="feature-title">Driver Management</h3>
            <p className="feature-description">
              Add and manage drivers, track their availability, and monitor their locations
            </p>
            <Link to="/drivers" className="btn btn--primary">
              Manage Drivers
            </Link>
          </div>

          <div className="feature-card">
            <div className="feature-icon">👥</div>
            <h3 className="feature-title">Rider Management</h3>
            <p className="feature-description">
              Register riders, track their ride requests, and manage pickup/drop locations
            </p>
            <Link to="/riders" className="btn btn--primary">
              Manage Riders
            </Link>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🔄</div>
            <h3 className="feature-title">Matching Algorithm</h3>
            <p className="feature-description">
              Run optimization algorithms to efficiently match drivers with riders
            </p>
            <Link to="/matching" className="btn btn--primary">
              Run Matching
            </Link>
          </div>
        </div>

        <div className="stats-overview">
          <h2 className="stats-title">System Overview</h2>
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-value">0</div>
              <div className="stat-label">Total Drivers</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">0</div>
              <div className="stat-label">Total Riders</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">0</div>
              <div className="stat-label">Active Matches</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">Ready</div>
              <div className="stat-label">System Status</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;