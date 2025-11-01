import { useState } from "react";
import { post } from "../api";
import { useNavigate } from "react-router-dom";

export default function ResetPassword() {
  const [email, setEmail] = useState("");
  const [msg, setMsg] = useState("");
  const [err, setErr] = useState("");
  const nav = useNavigate();

  async function submit(e) {
    e.preventDefault();
    setErr("");
    setMsg("");

    try {
      const res = await post("/auth/reset-password", { email });
      setMsg(`Şifreniz sıfırlandı. Yeni şifre: ${res.newPassword}`);
    } catch (e) {
      setErr(e.message);
    }
  }

  return (
    <div style={wrap}>
      <form onSubmit={submit} style={card}>
        <h2>Şifre Sıfırla</h2>

        <input
          style={input}
          placeholder="E-posta adresiniz"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        {err && <p style={{ color: "red" }}>{err}</p>}
        {msg && <p style={{ color: "green" }}>{msg}</p>}

        <button style={btn}>Sıfırla</button>
        <button style={back} onClick={() => nav("/login")} type="button">
          Geri Dön
        </button>
      </form>
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

const card = {
  padding: 24,
  borderRadius: 12,
  background: "#fff",
  width: 360,
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

const back = {
  padding: 10,
  border: "1px solid #111827",
  borderRadius: 8,
  background: "white",
  color: "#111827",
  cursor: "pointer",
};
