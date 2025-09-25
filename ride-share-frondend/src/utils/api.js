const API_BASE_URL = process.env.REACT_APP_API_URL;

// API helper function with better error handling for your Spring Boot backend
export const apiCall = async (endpoint, options = {}) => {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    });
    
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('API endpoint not found. Please ensure the backend is running.');
      }
      if (response.status >= 500) {
        throw new Error('Server error. Please try again later.');
      }
      
      // Try to get error message from response body
      try {
        const errorText = await response.text();
        throw new Error(errorText || `HTTP error! status: ${response.status}`);
      } catch {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
    }
    
    // Handle different content types
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      // For your Spring Boot endpoints that return plain text responses
      return await response.text();
    }
  } catch (error) {
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
      throw new Error('Unable to connect to the backend server. Please ensure it is running on localhost:8080.');
    }
    throw error;
  }
};

// Specific API functions for your Spring Boot backend
export const addDriver = async (driverData) => {
  return await apiCall('/api/v1/drivers', {
    method: 'POST',
    body: JSON.stringify(driverData)
  });
};

export const addRider = async (riderData) => {
  return await apiCall('/api/v1/riders', {
    method: 'POST',
    body: JSON.stringify(riderData)
  });
};

export const solveMatching = async () => {
  return await apiCall('/api/v1/matching/solve');
};

export const getMatchingStatus = async () => {
  return await apiCall('/api/v1/matching/status');
};

export const getApiInfo = async () => {
  return await apiCall('/api/v1/matching/info');
};