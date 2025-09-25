import React, { useState, useEffect } from 'react';
import { addDriver } from '../utils/api';

const DriverPage = () => {
  // Driver form state
  const [driverForm, setDriverForm] = useState({
    id: '',
    locationId: '',
    available: true
  });
  const [driverLoading, setDriverLoading] = useState(false);
  const [driverMessage, setDriverMessage] = useState(null);

  // Driver list state (local storage for demo)
  const [drivers, setDrivers] = useState([]);

  // Clear messages after 5 seconds
  useEffect(() => {
    if (driverMessage) {
      const timer = setTimeout(() => setDriverMessage(null), 5000);
      return () => clearTimeout(timer);
    }
  }, [driverMessage]);

  // Load drivers from local storage on mount
  useEffect(() => {
    const savedDrivers = localStorage.getItem('drivers');
    if (savedDrivers) {
      setDrivers(JSON.parse(savedDrivers));
    }
  }, []);

  const handleDriverChange = (e) => {
    const { name, value, type, checked } = e.target;
    setDriverForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleDriverSubmit = async (e) => {
    e.preventDefault();
    setDriverLoading(true);
    setDriverMessage(null);

    try {
      // Client-side validation
      if (!driverForm.id || !driverForm.locationId) {
        throw new Error('Please fill in all required fields');
      }

      if (parseInt(driverForm.id) <= 0 || parseInt(driverForm.locationId) <= 0) {
        throw new Error('ID and Location ID must be positive numbers');
      }

      const requestData = {
        id: parseInt(driverForm.id),
        locationId: parseInt(driverForm.locationId),
        available: driverForm.available
      };

      // Call your Spring Boot backend
      const response = await addDriver(requestData);
      
      // Handle string response from Spring Boot
      if (typeof response === 'string' && response.includes('successfully')) {
        setDriverMessage({ type: 'success', text: response });
        setDriverForm({ id: '', locationId: '', available: true });
        
        // Add to local storage for display
        const newDrivers = [...drivers, { ...requestData, addedAt: new Date().toISOString() }];
        setDrivers(newDrivers);
        localStorage.setItem('drivers', JSON.stringify(newDrivers));
      } else {
        throw new Error('Unexpected response from server');
      }
      
    } catch (error) {
      setDriverMessage({ type: 'error', text: error.message });
    } finally {
      setDriverLoading(false);
    }
  };

  const clearDriverHistory = () => {
    setDrivers([]);
    localStorage.removeItem('drivers');
  };

  return (
    <div className="page">
      <div className="page-container">
        <header className="page-header">
          <h1 className="page-title">Driver Management</h1>
          <p className="page-subtitle">Add and manage drivers in your ride-sharing system</p>
        </header>

        <div className="page-content">
          {/* Add Driver Form */}
          <section className="content-section">
            <div className="section-header">
              <h2 className="section-title">Add New Driver</h2>
              <p className="section-subtitle">Register a new driver with location and availability</p>
            </div>

            {driverMessage && (
              <div className={`message message--${driverMessage.type}`}>
                {driverMessage.text}
              </div>
            )}

            <form onSubmit={handleDriverSubmit} className="form">
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">Driver ID *</label>
                  <input
                    type="number"
                    name="id"
                    value={driverForm.id}
                    onChange={handleDriverChange}
                    className="form-control"
                    placeholder="Enter unique driver ID"
                    min="1"
                    required
                  />
                  <small className="form-help">Must be a unique positive integer</small>
                </div>
                <div className="form-group">
                  <label className="form-label">Location ID *</label>
                  <input
                    type="number"
                    name="locationId"
                    value={driverForm.locationId}
                    onChange={handleDriverChange}
                    className="form-control"
                    placeholder="Enter location ID"
                    min="1"
                    required
                  />
                  <small className="form-help">Location where driver is positioned</small>
                </div>
              </div>
              <div className="form-group">
                <div className="checkbox-group">
                  <input
                    type="checkbox"
                    name="available"
                    checked={driverForm.available}
                    onChange={handleDriverChange}
                    id="driver-available"
                  />
                  <label className="form-label" htmlFor="driver-available">Available for rides</label>
                </div>
                <small className="form-help">Only available drivers will be included in matching</small>
              </div>
              <button 
                type="submit" 
                className={`btn btn--primary btn--lg ${driverLoading ? 'btn--loading' : ''}`}
                disabled={driverLoading}
              >
                {driverLoading ? 'Adding Driver...' : 'Add Driver to System'}
              </button>
            </form>
          </section>

          {/* Driver Statistics */}
          <section className="content-section">
            <div className="section-header">
              <h2 className="section-title">Driver Statistics</h2>
              <p className="section-subtitle">Local session statistics</p>
            </div>
            
            <div className="stats-grid">
              <div className="stat-card stat-card--primary">
                <div className="stat-value">{drivers.length}</div>
                <div className="stat-label">Total Drivers Added</div>
              </div>
              <div className="stat-card stat-card--success">
                <div className="stat-value">{drivers.filter(d => d.available).length}</div>
                <div className="stat-label">Available Drivers</div>
              </div>
              <div className="stat-card stat-card--info">
                <div className="stat-value">{drivers.filter(d => !d.available).length}</div>
                <div className="stat-label">Unavailable Drivers</div>
              </div>
            </div>
          </section>

          {/* Recent Drivers List */}
          {drivers.length > 0 ? (
            <section className="content-section">
              <div className="section-header">
                <h2 className="section-title">Recently Added Drivers</h2>
                <div className="section-actions">
                  <p className="section-subtitle">Last {Math.min(drivers.length, 10)} drivers added in this session</p>
                  <button 
                    onClick={clearDriverHistory}
                    className="btn btn--secondary btn--sm"
                  >
                    Clear History
                  </button>
                </div>
              </div>
              
              <div className="table-container">
                <table className="table">
                  <thead>
                    <tr>
                      <th>Driver ID</th>
                      <th>Location ID</th>
                      <th>Status</th>
                      <th>Added At</th>
                    </tr>
                  </thead>
                  <tbody>
                    {drivers.slice(-10).reverse().map((driver, index) => (
                      <tr key={`${driver.id}-${index}`}>
                        <td><strong>#{driver.id}</strong></td>
                        <td>Location {driver.locationId}</td>
                        <td>
                          <span className={`status-badge ${driver.available ? 'status-badge--success' : 'status-badge--inactive'}`}>
                            {driver.available ? 'Available' : 'Unavailable'}
                          </span>
                        </td>
                        <td>{new Date(driver.addedAt).toLocaleTimeString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          ) : (
            <section className="content-section">
              <div className="empty-state">
                <div className="empty-state-icon">🚗</div>
                <h3 className="empty-state-title">No Drivers Added Yet</h3>
                <p className="empty-state-description">
                  Start by adding your first driver to the system using the form above. 
                  All drivers will be stored in your Spring Boot database.
                </p>
              </div>
            </section>
          )}
        </div>
      </div>
    </div>
  );
};

export default DriverPage;