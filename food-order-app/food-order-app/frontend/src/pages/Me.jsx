import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAuth } from "../api";
import MyOrders from "./MyOrder";

export default function Me() {
  const [me, setMe] = useState(null);
  const [err, setErr] = useState("");
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        const d = await getAuth("/auth/me");
        setMe(d);
      } catch (e) {
        setErr(e.message);
      }
    })();
  }, []);

  function logout() {
    localStorage.clear();
    nav("/login");
  }

  if (err)
    return (
      <Screen>
        <h3>Hata: {err}</h3>
        <button onClick={logout}>Girişe dön</button>
      </Screen>
    );
  if (!me)
    return (
      <Screen>
        <p>Yükleniyor...</p>
      </Screen>
    );

  return (
    <Screen>
      <h2>Merhaba, {me.fullName}!</h2>
      <p>
        Rolün: <b>{me.role}</b>
      </p>
      <button onClick={logout} style={{ marginTop: 12 }}>
        Çıkış
      </button>
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
const box = {
  width: 640,
  padding: 24,
  borderRadius: 12,
  background: "#fff",
  boxShadow: "0 10px 30px rgba(0,0,0,.08)",
};
