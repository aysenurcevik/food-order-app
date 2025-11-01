import { useEffect, useState } from "react";
import { get, getAuth } from "../api";
import { useNavigate } from "react-router-dom";
import "./Restaurants.css";
import Navbar from "../components/Navbar";

export default function Restaurants() {
  const [restaurants, setRestaurants] = useState([]);
  const [search, setSearch] = useState("");
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        await getAuth("/auth/me");
        const list = await get("/restaurants");
        setRestaurants(list);
      } catch (err) {
        console.error(err);
      }
    })();
  }, []);

  const filtered = restaurants.filter((r) => {
    const text = `${r.name} ${r.description || ""} ${
      r.address || ""
    }`.toLowerCase();
    return text.includes(search.toLowerCase());
  });

  return (
    <div className="restaurants-page">
      <Navbar />

      <div className="restaurants-top">
        <form onSubmit={(e) => e.preventDefault()} className="search-form">
          <input
            type="text"
            placeholder="Restoran ara..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="search-input"
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
                <h3>{r.name}</h3>
                <p>{r.description}</p>
                <p className="restaurant-address">{r.address}</p>
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
