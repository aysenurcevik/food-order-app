import { Routes, Route, Navigate, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getAuth } from "./api";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Me from "./pages/Me";
import Home from "./pages/Home";
import Restaurants from "./pages/Restaurants";
import RestaurantDetail from "./pages/RestaurantDetail";
import Cart from "./pages/Cart";
import Profile from "./pages/Profile";
import { CartProvider } from "./pages/CartContext";
import OwnerDashboard from "./pages/OwnerDashboard";
import OrderCreate from "./pages/OrderCreate";
import MyOrders from "./pages/MyOrder";
import OwnerOrders from "./pages/OwnerOrders";
import OwnerRestaurantSelect from "./pages/OwnerRestaurantSelect";
import AdminOrders from "./pages/AdminOrders";
import ResetPassword from "./pages/ResetPassword";
import ForgotPassword from "./pages/ForgotPassword";

function RequireAuth({ children }) {
  const [checked, setChecked] = useState(false);
  const [authorized, setAuthorized] = useState(false);
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setChecked(true);
        setAuthorized(false);
        return;
      }
      try {
        await getAuth("/auth/me");
        setAuthorized(true);
      } catch {
        localStorage.clear();
        setAuthorized(false);
        nav("/login", { replace: true });
      } finally {
        setChecked(true);
      }
    })();
  }, [nav]);

  if (!checked)
    return (
      <p style={{ textAlign: "center", marginTop: 100 }}>Kontrol ediliyor...</p>
    );
  if (!authorized) return <Navigate to="/login" replace />;
  return children;
}

export default function App() {
  return (
    <>
      <CartProvider>
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route
            path="/home"
            element={
              <RequireAuth>
                <Home />
              </RequireAuth>
            }
          />
          <Route
            path="/restaurants"
            element={
              <RequireAuth>
                <Restaurants />
              </RequireAuth>
            }
          />
          <Route
            path="/restaurants/:id"
            element={
              <RequireAuth>
                <RestaurantDetail />
              </RequireAuth>
            }
          />
          <Route
            path="/cart"
            element={
              <RequireAuth>
                <Cart />
              </RequireAuth>
            }
          />
          <Route
            path="/profile"
            element={
              <RequireAuth>
                <Profile />
              </RequireAuth>
            }
          />
          <Route
            path="/me"
            element={
              <RequireAuth>
                <Me />
              </RequireAuth>
            }
          />
          <Route
            path="/owner"
            element={
              <RequireAuth>
                <OwnerDashboard />
              </RequireAuth>
            }
          />
          <Route
            path="/orders/create"
            element={
              <RequireAuth>
                <OrderCreate />
              </RequireAuth>
            }
          />
          <Route
            path="/orders/my"
            element={
              <RequireAuth>
                <MyOrders />
              </RequireAuth>
            }
          />
          <Route
            path="/owner/restaurants"
            element={<OwnerRestaurantSelect />}
          />
          <Route
            path="/owner/orders/:restaurantId"
            element={
              <RequireAuth>
                <OwnerOrders />
              </RequireAuth>
            }
          />

          <Route path="/admin/orders" element={<AdminOrders />} />

          <Route path="/reset-password" element={<ResetPassword />} />

          <Route path="/forgot-password" element={<ForgotPassword />} />

          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </CartProvider>
    </>
  );
}
