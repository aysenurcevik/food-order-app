import { useEffect, useState } from "react";
import { getAuth } from "../api";
import { useNavigate } from "react-router-dom";
import { useCart } from "../pages/CartContext";

export default function Navbar({ onCartPage = false }) {
  const [user, setUser] = useState(null);
  const [ownerRestaurantId, setOwnerRestaurantId] = useState(null);
  const nav = useNavigate();
  const { cart } = useCart();

  useEffect(() => {
    (async () => {
      try {
        const me = await getAuth("/auth/me");

        if (me.role === "RESTAURANT_OWNER") {
          const myRests = await getAuth("/owner/restaurants/mine");
          if (myRests.length > 0) {
            setOwnerRestaurantId(myRests[0].id);
          }
        }

        setUser(me);
      } catch {}
    })();
  }, []);

  function logout() {
    localStorage.clear();
    nav("/login");
  }

  return (
    <header className="home-header">
      <h2 className="home-logo" onClick={() => nav("/home")}>
        FoodOrder
      </h2>

      <div className="home-actions">
        {user?.role === "CUSTOMER" && (
          <>
            {!onCartPage ? (
              <button className="home-btn" onClick={() => nav("/cart")}>
                Sepetim ({cart.length})
              </button>
            ) : (
              <button className="home-btn" onClick={() => nav("/home")}>
                Ana Sayfa
              </button>
            )}
          </>
        )}

        {user?.role === "RESTAURANT_OWNER" && (
          <>
            <button className="home-btn" onClick={() => nav("/owner")}>
              Yönetim Paneli
            </button>

            <button
              className="home-btn"
              onClick={() => {
                if (!ownerRestaurantId) return alert("Restoran bulunamadı!");
                nav(`/owner/orders/${ownerRestaurantId}`);
              }}
            >
              Gelen Siparişler
            </button>
          </>
        )}

        {user?.role === "ADMIN" && (
          <button className="home-btn" onClick={() => nav("/admin/orders")}>
            Tüm Siparişler
          </button>
        )}

        <button className="home-btn" onClick={() => nav("/profile")}>
          {user ? user.fullName : "Profil"}
        </button>

        <button className="home-btn logout" onClick={logout}>
          Çıkış
        </button>
      </div>
    </header>
  );
}
