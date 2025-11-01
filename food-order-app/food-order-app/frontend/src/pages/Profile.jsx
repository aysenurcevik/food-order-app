import { useEffect, useState } from "react";
import { getAuth } from "../api";
import { useNavigate } from "react-router-dom";
import "./Profile.css";
import { useCart } from "../pages/CartContext";
import Navbar from "../components/Navbar";

export default function Profile() {
  const [user, setUser] = useState(null);
  const nav = useNavigate();
  const { cart } = useCart();

  useEffect(() => {
    (async () => {
      try {
        const me = await getAuth("/auth/me");
        setUser(me);
      } catch (err) {
        console.error("Kullanıcı bilgisi alınamadı:", err);
      }
    })();
  }, []);

  function logout() {
    localStorage.clear();
    nav("/login");
  }

  if (!user)
    return (
      <div className="profile-loading">
        <h3>Yükleniyor...</h3>
      </div>
    );

  return (
    <div className="profile-page">
      <Navbar />

      <main className="profile-container">
        <div className="profile-card">
          <img
            src="https://cdn-icons-png.flaticon.com/512/847/847969.png"
            alt="Profil"
            className="profile-avatar"
          />
          <h2>{user.fullName}</h2>
          <p>
            <strong>E-posta:</strong> {user.email}
          </p>
          <p>
            <strong>Rol:</strong>{" "}
            {user.role === "CUSTOMER"
              ? "Müşteri"
              : user.role === "RESTAURANT_OWNER"
              ? "Restoran Sahibi"
              : "Admin"}
          </p>
          <div className="profile-buttons">
            <button onClick={() => nav("/home")}>Ana Sayfa</button>
            {user?.role === "CUSTOMER" && (
              <>
                <button onClick={() => nav("/orders/my")}>
                  Geçmiş Siparişlerim
                </button>
              </>
            )}
            <button onClick={() => nav("/restaurants")}>Restoranlar</button>
            <button className="danger" onClick={logout}>
              Çıkış Yap
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}
