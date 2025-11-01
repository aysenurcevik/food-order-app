import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { post } from "../api";

export default function Register() {
  const [email, setEmail] = useState("new@ex.com");
  const [password, setPassword] = useState("newpass123");
  const [fullName, setFullName] = useState("New User");
  const [role, setRole] = useState("CUSTOMER");
  const [err, setErr] = useState("");
  const nav = useNavigate();

  async function submit(e) {
    e.preventDefault();
    setErr("");
    try {
      const d = await post("/auth/register", {
        email,
        password,
        fullName,
        role,
      });
      localStorage.setItem("token", d.token);
      localStorage.setItem("role", d.role);
      localStorage.setItem("email", d.email);
      nav("/home");
    } catch (e) {
      setErr(e.message);
    }
  }

  return (
    <Screen>
      <form onSubmit={submit} style={card}>
        <h2>Kayıt Ol</h2>
        <input
          style={input}
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          placeholder="Ad Soyad"
        />
        <input
          style={input}
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="E-posta"
        />
        <input
          style={input}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Şifre"
          type="password"
        />
        <select
          style={input}
          value={role}
          onChange={(e) => setRole(e.target.value)}
        >
          <option value="CUSTOMER">Customer</option>
          <option value="RESTAURANT_OWNER">Restaurant Owner</option>
          <option value="ADMIN">Admin</option>
        </select>
        {err && <div style={{ color: "#b91c1c" }}>{err}</div>}
        <button style={btn}>Kayıt Ol</button>
        <div style={{ marginTop: 8, fontSize: 13 }}>
          <Link to="/login">Hesabın var mı?</Link>
        </div>
      </form>
    </Screen>
  );
}

function Screen({ children }) {
  return (
    <div style={wrap}>
      <div style={box}>{children}</div>
    </div>
  );
}
const wrap = {
  minHeight: "100vh",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  background: "#f7f7f9",
};
const box = { width: 420 };
const card = {
  padding: 24,
  borderRadius: 12,
  background: "#fff",
  boxShadow: "0 10px 30px rgba(0,0,0,.08)",
  display: "flex",
  flexDirection: "column",
  gap: 10,
};
const input = { padding: 10, border: "1px solid #ddd", borderRadius: 8 };
const btn = {
  padding: 10,
  border: "none",
  borderRadius: 8,
  background: "#111827",
  color: "#fff",
  cursor: "pointer",
};
