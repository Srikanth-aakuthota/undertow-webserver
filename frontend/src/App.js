import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Todos from './todos/Todos';
import Calendar from './calendar/Calendar';
import Invitations from './invitations/Invitations';

function Home() {
  return (
    <div style={{textAlign: 'center', marginTop: '2rem'}}>
      <h1>Welcome to the To-Do App!</h1>
      <p><Link to="/todos">Go to To-Do List</Link></p>
    </div>
  );
}

function NotFound() {
  return <h2 style={{textAlign: 'center'}}>404 - Page Not Found</h2>;
}

function App() {
  return (
    <Router>
      <nav style={{textAlign: 'center', margin: '1rem'}}>
        <Link to="/">Home</Link> | <Link to="/todos">To-Do List</Link> | <Link to="/calendar">Calendar</Link> | <Link to="/invitations">Invitations</Link>
      </nav>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/todos" element={<Todos />} />
        <Route path="/calendar" element={<Calendar />} />
        <Route path="/invitations" element={<Invitations />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;
