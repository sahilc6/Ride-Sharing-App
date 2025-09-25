import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import HomePage from './components/HomePage';
import DriverPage from './components/DriverForm';
import RiderPage from './components/RiderForm';
import MatchingPage from './components/MatchingSection';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/drivers" element={<DriverPage />} />
            <Route path="/riders" element={<RiderPage />} />
            <Route path="/matching" element={<MatchingPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;