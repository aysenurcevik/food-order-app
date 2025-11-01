import { useState } from "react";
import { post } from "../api";
import { useNavigate } from "react-router-dom";
import "./OrderCreate.css";

export default function OrderCreate({ restaurantId, cartItems, clearCart }) {
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  async function submitOrder() {
    if (!cartItems.length) {
      alert("Sepet boş!");
      return;
    }
    setLoading(true);
    try {
      const payload = {
        restaurantId,
        items: cartItems.map((i) => ({
          productId: i.id,
          quantity: i.quantity,
        })),
      };
      await post("/orders", payload);
      alert("Siparişiniz başarıyla oluşturuldu!");
      clearCart();
      nav("/orders/my");
    } catch (err) {
      alert("Hata: " + err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="order-create">
      <h2>Sipariş Özeti</h2>
      {cartItems.length === 0 ? (
        <p>Sepetiniz boş.</p>
      ) : (
        <>
          <ul>
            {cartItems.map((i) => (
              <li key={i.id}>
                {i.name} × {i.quantity} — {(i.price * i.quantity).toFixed(2)}₺
              </li>
            ))}
          </ul>
          <h3>
            Toplam:{" "}
            {cartItems.reduce((a, b) => a + b.price * b.quantity, 0).toFixed(2)}
            ₺
          </h3>
          <button disabled={loading} onClick={submitOrder}>
            {loading ? "Gönderiliyor..." : "Siparişi Onayla"}
          </button>
        </>
      )}
    </div>
  );
}
