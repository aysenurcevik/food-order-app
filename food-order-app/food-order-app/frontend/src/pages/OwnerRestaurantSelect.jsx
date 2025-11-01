import { useEffect, useState } from "react";
import { getAuth } from "../api";
import { useNavigate } from "react-router-dom";
import "./OwnerRestaurantSelect.css";

export default function OwnerRestaurantSelect() {
  const [restaurants, setRestaurants] = useState([]);
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        const data = await getAuth("/owner/restaurants/mine");
        setRestaurants(data);

        if (data.length === 1) {
          nav(`/owner/orders/${data[0].id}`);
        }
      } catch (err) {
        console.error(err);
        alert("Restoranlar getirilemedi!");
      }
    })();
  }, []);

  if (restaurants.length === 0) {
    return <p>Henüz bir restoran eklemediniz.</p>;
  }

  return (
    <div className="owner-select-page">
      <h2>Restoranını Seç</h2>
      <div className="restaurant-list">
        {restaurants.map((r) => (
          <button
            key={r.id}
            className="restaurant-card-btn"
            onClick={() => nav(`/owner/orders/${r.id}`)}
          >
            <h3>{r.name}</h3>
            <p>{r.address}</p>
          </button>
        ))}
      </div>
    </div>
  );
}
