import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { get } from "../api";
import "./RestaurantDetail.css";
import { useCart } from "../pages/CartContext";

export default function RestaurantDetail() {
  const { id } = useParams();
  const [restaurant, setRestaurant] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { addToCart } = useCart();
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        const r = await get(`/restaurants/${id}`);
        setRestaurant(r);

        const list = await get(`/restaurants/${id}/products`);
        setProducts(list.content || list);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);

  if (loading) return <div className="detail-loading">Yükleniyor...</div>;
  if (error)
    return (
      <div className="detail-error">
        <p>Bir hata oluştu: {error}</p>
        <button onClick={() => nav("/restaurants")}>Geri Dön</button>
      </div>
    );
  if (!restaurant) return <p>Restoran bulunamadı.</p>;

  return (
    <div className="restaurant-detail">
      <header className="detail-header">
        <button className="back-btn" onClick={() => nav("/restaurants")}>
          ← Geri
        </button>
        <h2 className="restaurant-name">{restaurant.name}</h2>
      </header>

      <div className="restaurant-info">
        <img
          src={restaurant.logoUrl || "https://via.placeholder.com/150"}
          alt={restaurant.name}
          className="restaurant-logo"
        />
        <div className="restaurant-meta">
          <p className="restaurant-desc">{restaurant.description}</p>
          <p className="restaurant-address">{restaurant.address}</p>
        </div>
      </div>

      <h3 className="menu-title">Menü</h3>
      <div className="menu-grid">
        {products.length === 0 ? (
          <p>Bu restoranda henüz ürün bulunmuyor.</p>
        ) : (
          products.map((p) => (
            <div key={p.id} className="menu-card">
              <img
                src={p.imageUrl || "https://via.placeholder.com/120"}
                alt={p.name}
                className="menu-img"
              />
              <div className="menu-body">
                <h4>{p.name}</h4>
                <p className="menu-desc">{p.description}</p>
                <div className="menu-footer">
                  <span className="menu-price">{p.price} ₺</span>
                  <button className="menu-btn" onClick={() => addToCart(p)}>
                    Sepete Ekle
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
