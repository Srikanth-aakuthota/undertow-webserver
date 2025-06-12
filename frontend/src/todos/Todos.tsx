import React, { useEffect, useState } from 'react';
import styles from './Todos.module.css';

type Todo = {
  id: number;
  text: string;
  completed: boolean;
};

const Todos: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [text, setText] = useState('');

  useEffect(() => {
    fetch('/api/todos')
      .then(res => res.json())
      .then(setTodos);
  }, []);

  const addTodo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!text.trim()) return;
    const res = await fetch('/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text })
    });
    const todo = await res.json();
    setTodos([...todos, todo]);
    setText('');
  };

  const toggleTodo = async (id: number, completed: boolean) => {
    await fetch(`/api/todos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ completed: !completed })
    });
    setTodos(todos.map(t => t.id === id ? { ...t, completed: !completed } : t));
  };

  const deleteTodo = async (id: number) => {
    await fetch(`/api/todos/${id}`, { method: 'DELETE' });
    setTodos(todos.filter(t => t.id !== id));
  };

  return (
    <div style={{ maxWidth: 400, margin: '2rem auto' }}>
      <h2>To-Do List</h2>
      <form onSubmit={addTodo} style={{ marginBottom: 16 }}>
        <input value={text} onChange={e => setText(e.target.value)} placeholder="Add a to-do..." />
        <button type="submit">Add</button>
      </form>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {todos.map(todo => (
          <li key={todo.id} className={styles.todoItem}>
            <span
              onClick={() => toggleTodo(todo.id, todo.completed)}
              className={todo.completed ? styles.completed : ''}
              style={{ cursor: 'pointer' }}
            >
              {todo.text}
            </span>
            <button onClick={() => deleteTodo(todo.id)} style={{ marginLeft: 8 }}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Todos;
