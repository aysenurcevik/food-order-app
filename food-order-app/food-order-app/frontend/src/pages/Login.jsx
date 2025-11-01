import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { post } from "../api";

export default function Login() {
  const [email, setEmail] = useState("customer@example.com");
  const [password, setPassword] = useState("cust123");
  const [err, setErr] = useState("");
  const nav = useNavigate();

  async function submit(e) {
    e.preventDefault();
    setErr("");
    try {
      const d = await post("/auth/login", { email, password });
      localStorage.setItem("token", d.token);
      localStorage.setItem("role", d.role);
      localStorage.setItem("email", d.email);
      if (d.role === "RESTAURANT_OWNER") {
        nav("/owner/restaurants");
      } else {
        nav("/home");
      }
    } catch (e) {
      setErr(e.message);
    }
  }

  return (
    <Screen>
      <form onSubmit={submit} style={card}>
        <h2>Giriş</h2>
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
          type="password"
          placeholder="Şifre"
        />
        {err && <div style={{ color: "#b91c1c" }}>{err}</div>}
        <button style={btn}>Giriş Yap</button>
        <div style={{ marginTop: 8, fontSize: 13 }}>
          <Link to="/register">Kayıt Ol</Link>
        </div>
        <div style={{ marginTop: 8 }}>
          <Link to="/forgot-password">Şifremi Unuttum?</Link>
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
const box = { width: 360 };
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
