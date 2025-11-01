import { useEffect, useState } from "react";
import { get, getAuth } from "../api";
import { useNavigate } from "react-router-dom";
import { useCart } from "../pages/CartContext";
import "./Home.css";
import Navbar from "../components/Navbar";

export default function Home() {
  const [restaurants, setRestaurants] = useState([]);
  const [search, setSearch] = useState("");
  const [user, setUser] = useState(null);
  const [category, setCategory] = useState("Tümü");
  const nav = useNavigate();
  const { cart } = useCart();

  const categories = [
    "Tümü",
    "Pizza",
    "Burger",
    "İçecek",
    "Tatlı",
    "Tavuk",
    "Döner",
  ];

  useEffect(() => {
    (async () => {
      try {
        const me = await getAuth("/auth/me");
        setUser(me);

        const list = await get("/restaurants");

        for (let rest of list) {
          try {
            rest.menu = await get(`/restaurants/${rest.id}/menu`);
          } catch {
            rest.menu = [];
          }
        }

        setRestaurants(list);
      } catch (err) {
        console.error("Veri alınamadı:", err);
      }
    })();
  }, []);

  const filtered = restaurants.filter((r) => {
    const text = `${r.name} ${r.description || ""} ${
      r.address || ""
    }`.toLowerCase();
    const matchesSearch = text.includes(search.toLowerCase());

    const matchesCategory =
      category === "Tümü" ||
      text.includes(category.toLowerCase()) ||
      r.menu?.some((item) =>
        item.name.toLowerCase().includes(category.toLowerCase())
      );

    return matchesSearch && matchesCategory;
  });

  return (
    <div className="home-page">
      <Navbar />

      <div className="home-top">
        <form onSubmit={(e) => e.preventDefault()} className="search-form">
          <input
            type="text"
            placeholder="Restoran ara..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="home-search"
          />
        </form>
      </div>

      <main className="restaurant-section">
        {filtered.length > 0 ? (
          <div className="restaurant-grid">
            {filtered.map((r) => (
              <div
                key={r.id}
                className="restaurant-card"
                onClick={() => nav(`/restaurants/${r.id}`)}
              >
                <img
                  src={r.logoUrl || "https://via.placeholder.com/240x160"}
                  alt={r.name}
                  className="restaurant-img"
                />
                <div className="restaurant-info">
                  <div className="restaurant-header-row">
                    <h3 className="restaurant-name">{r.name}</h3>
                  </div>

                  <p className="restaurant-desc">{r.description}</p>

                  <div className="restaurant-location-row">
                    <span className="location-icon">📍</span>
                    <span className="restaurant-address">{r.address}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="no-results">Aradığınız restoran bulunamadı.</p>
        )}
      </main>
    </div>
  );
}
