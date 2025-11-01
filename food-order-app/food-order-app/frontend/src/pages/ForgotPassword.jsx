import { useState } from "react";
import { post } from "../api";
import { useNavigate } from "react-router-dom";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const nav = useNavigate();

  async function submit(e) {
    e.preventDefault();
    setMessage("");

    try {
      const res = await post("/auth/reset-password", {
        email,
        newPassword,
      });

      setMessage(res.message + " ✅");
    } catch (e) {
      setMessage("Hata: " + e.message);
    }
  }

  return (
    <div style={wrap}>
      <form onSubmit={submit} style={card}>
        <h2>Şifre Yenile</h2>
        <input
          style={input}
          placeholder="E-posta"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          style={input}
          placeholder="Yeni Şifre"
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />

        <button style={btn}>Şifreyi Güncelle</button>

        {message && <p style={{ marginTop: 10 }}>{message}</p>}

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
