import React, { useState, useEffect } from 'react';
import { apiCall } from '../utils/api';

const StatusSection = ({ onStatusUpdate }) => {
  const [systemStatus, setSystemStatus] = useState(null);
  const [apiInfo, setApiInfo] = useState(null);
  const [statusLoading, setStatusLoading] = useState(false);

  const loadStatus = async () => {
    setStatusLoading(true);
    try {
      const [status, info] = await Promise.allSettled([
        apiCall('/api/v1/matching/status'),
        apiCall('/api/v1/matching/info')
      ]);

      if (status.status === 'fulfilled') {
        setSystemStatus(status.value);
      } else {
        setSystemStatus({ status: 'Unavailable', error: 'Could not connect to backend' });
      }

      if (info.status === 'fulfilled') {
        setApiInfo(info.value);
      } else {
        setApiInfo({ 
          version: 'Unknown',
          service: 'Ride Matching API',
          description: 'Backend unavailable',
          status: 'Offline'
        });
      }
      
      // Notify parent component if callback provided
      if (onStatusUpdate) {
        onStatusUpdate({ 
          systemStatus: status.status === 'fulfilled' ? status.value : null,
          apiInfo: info.status === 'fulfilled' ? info.value : null
        });
      }
    } catch (error) {
      setSystemStatus({ status: 'Error', message: error.message });
      setApiInfo({ 
        version: 'Unknown',
        service: 'Ride Matching API',
        description: 'Backend unavailable',
        status: 'Offline'
      });
    } finally {
      setStatusLoading(false);
    }
  };

  // Load status on component mount
  useEffect(() => {
    loadStatus();
  }, []);

  return (
    <section className="dashboard-section">
      <h2 className="section-title">System Status</h2>
      <button 
        onClick={loadStatus}
        className={`btn btn--secondary ${statusLoading ? 'btn--loading' : ''}`}
        disabled={statusLoading}
      >
        {statusLoading ? 'Refreshing...' : 'Refresh Status'}
      </button>

      <div className="status-grid">
        {systemStatus && (
          <div className="status-card">
            <h4>System Status</h4>
            <p>{systemStatus.status || 'Unknown'}</p>
          </div>
        )}
        
        {apiInfo && (
          <>
            <div className="status-card">
              <h4>API Version</h4>
              <p>{apiInfo.version || 'N/A'}</p>
            </div>
            <div className="status-card">
              <h4>Service</h4>
              <p>{apiInfo.service || 'Ride Matching API'}</p>
            </div>
            <div className="status-card">
              <h4>Description</h4>
              <p>{apiInfo.description || 'Driver-Rider Matching System'}</p>
            </div>
          </>
        )}
      </div>
    </section>
  );
};

export default StatusSection;