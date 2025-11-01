import { useEffect, useState } from "react";
import { getAuth } from "../api";
import "./MyOrders.css";
import Navbar from "../components/Navbar";

export default function MyOrders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    (async () => {
      try {
        const d = await getAuth("/orders/my");
        setOrders(d);
      } catch (err) {
        console.error(err);
      }
    })();
  }, []);

  return (
    <div className="my-orders">
      <Navbar />
      <h2>Geçmiş Siparişlerim</h2>
      {orders.length === 0 ? (
        <p>Henüz siparişiniz bulunmuyor.</p>
      ) : (
        <div className="orders-grid">
          {orders.map((o) => (
            <div key={o.id} className="order-card">
              <p>
                <b>Sipariş ID:</b> {o.id}
              </p>
              <p>
                <b>Durum:</b> {o.status}
              </p>
              <p>
                <b>Tarih:</b> {new Date(o.createdAt).toLocaleString()}
              </p>
              <p>
                <b>Toplam:</b> {o.total}₺
              </p>
              <ul>
                {o.items.map((i) => (
                  <li key={i.productId}>
                    {i.name} × {i.quantity}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
