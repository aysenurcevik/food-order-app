import { useCart } from "../pages/CartContext";
import { useNavigate } from "react-router-dom";
import { post } from "../api";
import "./Cart.css";
import Navbar from "../components/Navbar";

export default function Cart() {
  const {
    cart,
    removeFromCart,
    decreaseQuantity,
    addToCart,
    total,
    clearCart,
  } = useCart();
  const nav = useNavigate();

  async function handleCheckout() {
    if (cart.length === 0) return alert("Sepetiniz boş!");

    const restaurantId = cart[0].restaurantId;
    const items = cart.map((i) => ({
      productId: i.id,
      quantity: i.quantity,
    }));

    try {
      await post("/orders", { restaurantId, items });
      alert("Sipariş verildi!");
      clearCart();
      nav("/orders/my");
    } catch (err) {
      alert("Sipariş verilemedi: " + err.message);
      console.error(err);
    }
  }

  if (cart.length === 0)
    return (
      <div className="cart-page empty">
        <h2>Sepetiniz boş 🛒</h2>
        <button onClick={() => nav("/restaurants")}>Restoranlara Göz At</button>
      </div>
    );

  return (
    <div className="cart-page">
      <Navbar onCartPage={true} />
      <h2>Sepetim</h2>
      <div className="cart-list">
        {cart.map((item) => (
          <div key={item.id} className="cart-item">
            <img
              src={item.imageUrl || "https://via.placeholder.com/80"}
              alt={item.name}
            />
            <div className="cart-info">
              <h4>{item.name}</h4>
              <p>{item.price} ₺</p>
              <div className="cart-actions">
                <button onClick={() => decreaseQuantity(item.id)}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => addToCart(item)}>+</button>
              </div>
            </div>
            <button
              className="remove-btn"
              onClick={() => removeFromCart(item.id)}
            >
              Sil
            </button>
          </div>
        ))}
      </div>

      <div className="cart-summary">
        <h3>Toplam: {total.toFixed(2)} ₺</h3>

        <button className="checkout-btn" onClick={handleCheckout}>
          Siparişi Tamamla
        </button>

        <button className="clear-btn" onClick={clearCart}>
          Sepeti Temizle
        </button>
      </div>
    </div>
  );
}
