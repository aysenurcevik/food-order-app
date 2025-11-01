import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getAuth, patch } from "../api";
import "./OwnerOrders.css";
import Navbar from "../components/Navbar";

export default function OwnerOrders() {
  const { restaurantId } = useParams();
  const [orders, setOrders] = useState([]);

  if (!restaurantId) {
    return <p>Restoran ID bulunamadı, lütfen kontrol edin.</p>;
  }

  async function loadOrders() {
    try {
      const d = await getAuth(`/owner/orders/${restaurantId}`);
      setOrders(d);
    } catch (err) {
      console.error(err);
      alert("Siparişler alınamadı!");
    }
  }

  useEffect(() => {
    loadOrders();
  }, []);

  async function updateStatus(orderId, status) {
    try {
      await patch(`/owner/orders/${orderId}/status`, { status });
      loadOrders();
    } catch (err) {
      alert("Durum güncellenemedi: " + err.message);
    }
  }

  const formatStatus = (s) => {
    switch (s) {
      case "PREPARING":
        return "Hazırlanıyor";
      case "ON_THE_WAY":
        return "Yolda";
      case "DELIVERED":
        return "Teslim Edildi";
      default:
        return s;
    }
  };

  return (
    <div className="owner-orders">
      <Navbar />

      <div className="orders-header">
        <h2>Gelen Siparişler</h2>
        <button className="refresh-btn" onClick={loadOrders}>
          Güncel Siparişleri Getir
        </button>
      </div>

      {orders.length === 0 ? (
        <p>Henüz sipariş bulunmuyor.</p>
      ) : (
        <table className="orders-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Müşteri</th>
              <th>Durum</th>
              <th>Toplam</th>
              <th>Güncelle</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((o) => (
              <tr key={o.id}>
                <td>{o.id}</td>
                <td>{o.customerEmail}</td>
                <td>
                  <span className={`status-badge ${o.status}`}>
                    {formatStatus(o.status)}
                  </span>
                </td>
                <td>{o.total} ₺</td>
                <td>
                  {o.status !== "DELIVERED" && (
                    <select
                      className="status-select"
                      onChange={(e) => updateStatus(o.id, e.target.value)}
                      defaultValue={o.status}
                    >
                      <option value="PREPARING">Hazırlanıyor</option>
                      <option value="ON_THE_WAY">Yolda</option>
                      <option value="DELIVERED">Teslim Edildi</option>
                    </select>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
