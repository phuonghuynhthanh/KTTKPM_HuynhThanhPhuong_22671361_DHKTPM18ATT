import "./App.css";

function App() {
  return (
    <main className="app-shell">
      <section className="hero-card">
        <p className="eyebrow">Lab 5 / Part 2 / Ex13</p>
        <h1>React app duoc phuc vu boi Nginx</h1>
        <p className="description">
          Day la ban cai dat toi gian: React tao giao dien, build thanh static
          files, sau do Nginx serve ket qua tu thu muc build.
        </p>
        <div className="status-grid">
          <div className="status-item">
            <span>Frontend</span>
            <strong>React</strong>
          </div>
          <div className="status-item">
            <span>Web server</span>
            <strong>Nginx</strong>
          </div>
          <div className="status-item">
            <span>Port</span>
            <strong>80</strong>
          </div>
        </div>
      </section>
    </main>
  );
}

export default App;
