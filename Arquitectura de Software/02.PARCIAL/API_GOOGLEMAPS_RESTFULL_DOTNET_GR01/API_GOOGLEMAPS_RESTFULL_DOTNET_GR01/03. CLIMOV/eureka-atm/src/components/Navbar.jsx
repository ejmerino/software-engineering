export default function Navbar({ onToggleMenu, title = "EUREKABANK", subtitle = "React + .NET API + Google Maps" }) {
  return (
    <header className="topbar">
      <button className="icon-btn" onClick={onToggleMenu} aria-label="Abrir menú">
        ☰
      </button>

      <div className="brand">
        <div className="brand-title">{title}</div>
        <div className="brand-sub">{subtitle}</div>
      </div>
    </header>
  );
}
