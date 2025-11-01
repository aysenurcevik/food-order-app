import { useEffect, useState } from "react";
import { getAuth } from "../api";
import "./AdminOrders.css";
import Navbar from "../components/Navbar";

export default function AdminOrders() {
  const [orders, setOrders] = useState([]);

  async function loadOrders() {
    try {
      const d = await getAuth("/orders/admin/all");
      setOrders(d);
    } catch (err) {
      console.error(err);
      alert("Siparişler alınamadı!");
    }
  }

  useEffect(() => {
    loadOrders();
  }, []);

  return (
    <div className="admin-orders">
      <Navbar />
      <div className="orders-header">
        <h2> Tüm Siparişler (Admin)</h2>
        <button onClick={loadOrders}>🔄 Yenile</button>
      </div>

      {orders.length === 0 ? (
        <p>Henüz sipariş bulunmuyor.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Müşteri</th>
              <th>Restoran ID</th>
              <th>Durum</th>
              <th>Toplam</th>
              <th>Tarih</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((o) => (
              <tr key={o.id}>
                <td>{o.id}</td>
                <td>{o.customerEmail}</td>
                <td>{o.restaurantId}</td>
                <td>{o.status}</td>
                <td>{o.total} ₺</td>
                <td>{new Date(o.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
