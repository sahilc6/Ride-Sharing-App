import React, { useState, useEffect } from 'react';
import { addRider } from '../utils/api';

const RiderPage = () => {
  // Rider form state
  const [riderForm, setRiderForm] = useState({
    id: '',
    pickupLocationId: '',
    dropLocationId: '',
    requested: true
  });
  const [riderLoading, setRiderLoading] = useState(false);
  const [riderMessage, setRiderMessage] = useState(null);

  // Rider list state (local storage for demo)
  const [riders, setRiders] = useState([]);

  // Clear messages after 5 seconds
  useEffect(() => {
    if (riderMessage) {
      const timer = setTimeout(() => setRiderMessage(null), 5000);
      return () => clearTimeout(timer);
    }
  }, [riderMessage]);

  // Load riders from local storage on mount
  useEffect(() => {
    const savedRiders = localStorage.getItem('riders');
    if (savedRiders) {
      setRiders(JSON.parse(savedRiders));
    }
  }, []);

  const handleRiderChange = (e) => {
    const { name, value, type, checked } = e.target;
    setRiderForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleRiderSubmit = async (e) => {
    e.preventDefault();
    setRiderLoading(true);
    setRiderMessage(null);

    try {
      // Client-side validation
      if (!riderForm.id || !riderForm.pickupLocationId || !riderForm.dropLocationId) {
        throw new Error('Please fill in all required fields');
      }

      if (parseInt(riderForm.id) <= 0 || parseInt(riderForm.pickupLocationId) <= 0 || parseInt(riderForm.dropLocationId) <= 0) {
        throw new Error('All IDs must be positive numbers');
      }

      if (riderForm.pickupLocationId === riderForm.dropLocationId) {
        throw new Error('Pickup and drop locations must be different');
      }

      const requestData = {
        id: parseInt(riderForm.id),
        pickupLocationId: parseInt(riderForm.pickupLocationId),
        dropLocationId: parseInt(riderForm.dropLocationId),
        requested: riderForm.requested
      };

      // Call your Spring Boot backend
      const response = await addRider(requestData);
      
      // Handle string response from Spring Boot
      if (typeof response === 'string' && response.includes('successfully')) {
        setRiderMessage({ type: 'success', text: response });
        setRiderForm({ id: '', pickupLocationId: '', dropLocationId: '', requested: true });
        
        // Add to local storage for display
        const newRiders = [...riders, { ...requestData, addedAt: new Date().toISOString() }];
        setRiders(newRiders);
        localStorage.setItem('riders', JSON.stringify(newRiders));
      } else {
        throw new Error('Unexpected response from server');
      }
      
    } catch (error) {
      setRiderMessage({ type: 'error', text: error.message });
    } finally {
      setRiderLoading(false);
    }
  };

  const clearRiderHistory = () => {
    setRiders([]);
    localStorage.removeItem('riders');
  };

  return (
    <div className="page">
      <div className="page-container">
        <header className="page-header">
          <h1 className="page-title">Rider Management</h1>
          <p className="page-subtitle">Add and manage riders requesting rides in your system</p>
        </header>

        <div className="page-content">
          {/* Add Rider Form */}
          <section className="content-section">
            <div className="section-header">
              <h2 className="section-title">Add New Rider</h2>
              <p className="section-subtitle">Register a new rider with pickup and drop-off locations</p>
            </div>

            {riderMessage && (
              <div className={`message message--${riderMessage.type}`}>
                {riderMessage.text}
              </div>
            )}

            <form onSubmit={handleRiderSubmit} className="form">
              <div className="form-group">
                <label className="form-label">Rider ID *</label>
                <input
                  type="number"
                  name="id"
                  value={riderForm.id}
                  onChange={handleRiderChange}
                  className="form-control"
                  placeholder="Enter unique rider ID"
                  min="1"
                  required
                />
                <small className="form-help">Must be a unique positive integer</small>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">Pickup Location ID *</label>
                  <input
                    type="number"
                    name="pickupLocationId"
                    value={riderForm.pickupLocationId}
                    onChange={handleRiderChange}
                    className="form-control"
                    placeholder="Pickup location"
                    min="1"
                    required
                  />
                  <small className="form-help">Where the rider wants to be picked up</small>
                </div>
                <div className="form-group">
                  <label className="form-label">Drop Location ID *</label>
                  <input
                    type="number"
                    name="dropLocationId"
                    value={riderForm.dropLocationId}
                    onChange={handleRiderChange}
                    className="form-control"
                    placeholder="Drop location"
                    min="1"
                    required
                  />
                  <small className="form-help">Where the rider wants to be dropped off</small>
                </div>
              </div>
              <div className="form-group">
                <div className="checkbox-group">
                  <input
                    type="checkbox"
                    name="requested"
                    checked={riderForm.requested}
                    onChange={handleRiderChange}
                    id="rider-requested"
                  />
                  <label className="form-label" htmlFor="rider-requested">Requesting ride</label>
                </div>
                <small className="form-help">Only requesting riders will be included in matching</small>
              </div>
              <button 
                type="submit" 
                className={`btn btn--primary btn--lg ${riderLoading ? 'btn--loading' : ''}`}
                disabled={riderLoading}
              >
                {riderLoading ? 'Adding Rider...' : 'Add Rider to System'}
              </button>
            </form>
          </section>

          {/* Rider Statistics */}
          <section className="content-section">
            <div className="section-header">
              <h2 className="section-title">Rider Statistics</h2>
              <p className="section-subtitle">Local session statistics</p>
            </div>
            
            <div className="stats-grid">
              <div className="stat-card stat-card--primary">
                <div className="stat-value">{riders.length}</div>
                <div className="stat-label">Total Riders Added</div>
              </div>
              <div className="stat-card stat-card--warning">
                <div className="stat-value">{riders.filter(r => r.requested).length}</div>
                <div className="stat-label">Requesting Rides</div>
              </div>
              <div className="stat-card stat-card--info">
                <div className="stat-value">{riders.filter(r => !r.requested).length}</div>
                <div className="stat-label">Not Requesting</div>
              </div>
            </div>
          </section>

          {/* Recent Riders List */}
          {riders.length > 0 ? (
            <section className="content-section">
              <div className="section-header">
                <h2 className="section-title">Recently Added Riders</h2>
                <div className="section-actions">
                  <p className="section-subtitle">Last {Math.min(riders.length, 10)} riders added in this session</p>
                  <button 
                    onClick={clearRiderHistory}
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
                      <th>Rider ID</th>
                      <th>Pickup Location</th>
                      <th>Drop Location</th>
                      <th>Status</th>
                      <th>Added At</th>
                    </tr>
                  </thead>
                  <tbody>
                    {riders.slice(-10).reverse().map((rider, index) => (
                      <tr key={`${rider.id}-${index}`}>
                        <td><strong>#{rider.id}</strong></td>
                        <td>Location {rider.pickupLocationId}</td>
                        <td>Location {rider.dropLocationId}</td>
                        <td>
                          <span className={`status-badge ${rider.requested ? 'status-badge--warning' : 'status-badge--inactive'}`}>
                            {rider.requested ? 'Requesting' : 'Not Requesting'}
                          </span>
                        </td>
                        <td>{new Date(rider.addedAt).toLocaleTimeString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          ) : (
            <section className="content-section">
              <div className="empty-state">
                <div className="empty-state-icon">👥</div>
                <h3 className="empty-state-title">No Riders Added Yet</h3>
                <p className="empty-state-description">
                  Start by adding your first rider to the system using the form above. 
                  All riders will be stored in your Spring Boot database.
                </p>
              </div>
            </section>
          )}
        </div>
      </div>
    </div>
  );
};

export default RiderPage;