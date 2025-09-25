import React, { useState, useEffect } from 'react';
import { apiCall } from '../utils/api';

const MatchingSection = ({ onMatchingComplete }) => {
  const [matchingResults, setMatchingResults] = useState(null);
  const [matchingLoading, setMatchingLoading] = useState(false);
  const [matchingMessage, setMatchingMessage] = useState(null);

  // System status
  const [systemStatus, setSystemStatus] = useState(null);
  const [apiInfo, setApiInfo] = useState(null);
  const [statusLoading, setStatusLoading] = useState(false);

  // Clear messages after 5 seconds
  useEffect(() => {
    if (matchingMessage) {
      const timer = setTimeout(() => setMatchingMessage(null), 5000);
      return () => clearTimeout(timer);
    }
  }, [matchingMessage]);

  // Load system status on component mount
  useEffect(() => {
    loadSystemStatus();
  }, []);

  const handleMatching = async () => {
    setMatchingLoading(true);
    setMatchingMessage(null);
    setMatchingResults(null);

    try {
      const result = await apiCall('/api/v1/matching/solve');
      setMatchingResults(result);
      
      if (result.matches && result.matches.length > 0) {
        setMatchingMessage({ 
          type: 'success', 
          text: result.message || `Successfully matched ${result.matches.length} driver(s) with rider(s)!` 
        });
      } else {
        setMatchingMessage({ 
          type: 'info', 
          text: 'Matching completed but no matches were found. Please ensure there are available drivers and requested riders.' 
        });
      }
      
      // Notify parent component if callback provided
      if (onMatchingComplete) {
        onMatchingComplete(result);
      }
    } catch (error) {
      setMatchingMessage({ type: 'error', text: error.message });
      // Show mock data if backend is unavailable for demonstration
      if (error.message.includes('connect to the backend')) {
        setMatchingResults({
          matches: [
            {
              driverId: 1,
              driverLocation: 1,
              riderId: 1,
              pickupLocation: 2,
              dropLocation: 3,
              totalCost: 15,
              description: "Demo: Driver 1 at location 1 assigned to Rider 1"
            }
          ],
          totalCost: 15,
          status: "DEMO",
          message: "Demo data (backend unavailable)"
        });
      }
    } finally {
      setMatchingLoading(false);
    }
  };

  const loadSystemStatus = async () => {
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

  const refreshSystemStatus = () => {
    loadSystemStatus();
  };

  return (
    <section className="dashboard-section">
      <div className="matching-header">
        <h2 className="section-title">Driver-Rider Matching & System Status</h2>
        <div className="matching-controls">
          <button 
            onClick={handleMatching}
            className={`btn btn--primary btn--lg ${matchingLoading ? 'btn--loading' : ''}`}
            disabled={matchingLoading}
          >
            {matchingLoading ? 'Running Algorithm...' : 'Run Matching Algorithm'}
          </button>
          <button 
            onClick={refreshSystemStatus}
            className={`btn btn--secondary ${statusLoading ? 'btn--loading' : ''}`}
            disabled={statusLoading}
          >
            {statusLoading ? 'Refreshing...' : 'Refresh Status'}
          </button>
        </div>
      </div>

      {matchingMessage && (
        <div className={`message message--${matchingMessage.type}`}>
          {matchingMessage.text}
        </div>
      )}

      <div className="matching-content">
        {/* Matching Results */}
        <div className="matching-results-section">
          <h3 className="subsection-title">Matching Results</h3>
          {matchingResults ? (
            <div className="matching-results">
              <div className="total-cost">
                <p className="total-cost-value">{matchingResults.totalCost || 0}</p>
                <p className="total-cost-label">Total Cost</p>
              </div>
              
              {matchingResults.matches && matchingResults.matches.length > 0 ? (
                <div className="table-container">
                  <table className="table">
                    <thead>
                      <tr>
                        <th>Driver ID</th>
                        <th>Driver Location</th>
                        <th>Rider ID</th>
                        <th>Pickup Location</th>
                        <th>Drop Location</th>
                        <th>Cost</th>
                      </tr>
                    </thead>
                    <tbody>
                      {matchingResults.matches.map((match, index) => (
                        <tr key={index}>
                          <td>{match.driverId}</td>
                          <td>{match.driverLocation}</td>
                          <td>{match.riderId}</td>
                          <td>{match.pickupLocation}</td>
                          <td>{match.dropLocation}</td>
                          <td>{match.totalCost}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="empty-state">
                  <p>No matches found. Add drivers and riders to see matching results.</p>
                </div>
              )}
            </div>
          ) : (
            <div className="empty-state">
              <p>Click "Run Matching Algorithm" to see driver-rider matches.</p>
            </div>
          )}
        </div>

        {/* System Status */}
        <div className="system-status-section">
          <h3 className="subsection-title">System Status</h3>
          <div className="status-grid-large">
            {systemStatus && (
              <div className="status-card">
                <h4>System Status</h4>
                <p className={systemStatus.status === 'Unavailable' || systemStatus.status === 'Error' ? 'status-error' : ''}>
                  {systemStatus.status || 'Unknown'}
                </p>
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
        </div>
      </div>
    </section>
  );
};

export default MatchingSection;